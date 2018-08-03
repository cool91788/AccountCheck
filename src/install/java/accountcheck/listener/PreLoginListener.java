/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2014-2018  Install
 *
 *   This file is part of AccountCheck source code.
 *
 *   AccountCheck is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   AccountCheck is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with AccountCheck.  If not, see <http://www.gnu.org/licenses/>.
*/

package install.java.accountcheck.listener;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.account.AccountInfo;
import install.java.accountcheck.log.LogManager;
import install.java.accountcheck.log.LogType;
import install.java.accountcheck.util.IP6;
import install.java.accountcheck.yaml.Config;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLoginListener implements Listener {
	
	@EventHandler
	public void onPreLoginEvent(PreLoginEvent event) {
		chooseOnlineMode(event);
	}
	
	private void chooseOnlineMode(PreLoginEvent event) {
		Config config = AccountCheck.getInstance().getConfig();
		String playername = event.getConnection().getName();
		String ip = event.getConnection().getAddress().getAddress().getHostAddress();
		// 如果不是ipv4，則為ipv6（這裡不判斷合不合法）
		if(!ip.matches("^\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}\\.\\d{1,4}$"))
			ip = new IP6(ip).getCompressedIP6();
		
		AccountCheck.getInstance().getLogManager().log(LogType.PLAYER_CONNECT, new String[] {playername, ip});
		
		if(!config.isPiratedAccessible() || config.isPiratedLoginServerOffline()) {
			event.getConnection().setOnlineMode(true);
			return;
		}
		
		// 檢查盜版登入伺服器是否在線
		ServerInfo server = ProxyServer.getInstance().getServers()
				.get(AccountCheck.getInstance().getConfig().getPiratedLoginServer());
		if(server == null) {
			config.setPiratedLoginServerOffline(true);
			config.setForceOnlineMode(true);
			event.getConnection().setOnlineMode(true);
		}else {
			// 告訴Bungeecord將要執行非同步任務
			event.registerIntent(AccountCheck.getInstance());
			server.ping(new Callback<ServerPing>() {
				@Override
				public void done(ServerPing resault, Throwable error) {
					if(error == null) {
						// 	正常在線，進入正常檢測流程
						config.setForceOnlineMode(false);
						ProxyServer.getInstance().getScheduler().runAsync(AccountCheck.getInstance(), new CheckAccountTask(event));
					}else {
						config.setPiratedLoginServerOffline(true);
						config.setForceOnlineMode(true);
						event.getConnection().setOnlineMode(true);
						// 告訴Bungeecord已完成非同步任務
						event.completeIntent(AccountCheck.getInstance());
					}
				}
			});
		}
	}
	
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	private class CheckAccountTask implements Runnable {
		private final PreLoginEvent event;
		
		@Override
		public void run() {
			// 這個階段還無法取得UUID，故直接使用username.
			AccountInfo accountInfo = AccountCheck.getInstance().getAccountManager().getInfo(event.getConnection().getName(), true);
			LogManager logManager = AccountCheck.getInstance().getLogManager();
			
			switch(accountInfo) {
				case PIRATED_ACCOUNT:
					event.getConnection().setOnlineMode(false);
					break;
				case PIRATED_ACCOUNT_CASE_INSENSITIVE:
					logManager.log(LogType.REJECT_LOGIN, new String[] {"與正版名稱相同(不區分大小寫)"});
					event.getConnection().setOnlineMode(true);
					break;
				case GENUINE_ACCOUNT:
					event.getConnection().setOnlineMode(true);
					break;
				case HTTP_ERROR:
					logManager.log(LogType.HTTP_ERROR);
					event.getConnection().disconnect(TextComponent.fromLegacyText(
							ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：" + LogType.HTTP_ERROR.getErrorCode()));
					break;
				default:
					logManager.log(LogType.UNKNOWN_ERROR);
					event.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
			}
			// 告訴Bungeecord已完成非同步任務
			event.completeIntent(AccountCheck.getInstance());
		}
	}
}
