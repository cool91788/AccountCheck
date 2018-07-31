/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) (2014-2018)  Install
 *
 *   This file is part of AccountCheck.
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
import install.java.accountcheck.log.AccountCheckLogManager;
import install.java.accountcheck.log.LogType;
import install.java.accountcheck.yaml.Config;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLoginListener implements Listener{
	
	@EventHandler
	public void chooseOnlineMode(PreLoginEvent preloginevent) {
		Config config = AccountCheck.getInstance().getConfig();
		String playername = preloginevent.getConnection().getName();
		String ip = preloginevent.getConnection().getAddress().toString();
		ip = ip.substring(1, ip.lastIndexOf(':'));
		
		AccountCheck.getInstance().getLogManager().log(LogType.PLAYER_CONNECT, new String[] {playername, ip});
		
		if(!config.isPiratedAccessible() || config.isPiratedLoginServerOffline()) {
			preloginevent.getConnection().setOnlineMode(true);
			return;
		}
		preloginevent.registerIntent(AccountCheck.getInstance());
		ProxyServer.getInstance().getServers()
				.get(AccountCheck.getInstance().getConfig().getPiratedLoginServer())
				.ping(new Callback<ServerPing>() {
					@Override
					public void done(ServerPing resault, Throwable error) {
						if(error == null) {
							config.setForceOnlineMode(false);
							ProxyServer.getInstance().getScheduler().runAsync(AccountCheck.getInstance(),
									new Task(preloginevent, playername));
						}else {
							config.setPiratedLoginServerOffline(true);
							config.setForceOnlineMode(true);
							preloginevent.getConnection().setOnlineMode(true);
							preloginevent.completeIntent(AccountCheck.getInstance());
						}
					}
				});
	}
	
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	private class Task implements Runnable {
		private final PreLoginEvent preloginevent;
		private final String playername;
		
		@Override
		public void run() {
			// 這個階段還無法取得UUID，故直接使用username.
			AccountInfo accountInfo = AccountCheck.getInstance().getAccountManager().getInfo(playername, true);
			AccountCheckLogManager logManager = AccountCheck.getInstance().getLogManager();
			switch(accountInfo) {
				case PIRATED_ACCOUNT:
					preloginevent.getConnection().setOnlineMode(false);
					break;
				case PIRATED_ACCOUNT_CASE_INSENSITIVE:
					logManager.log(LogType.REJECT_LOGIN, new String[] {"與正版名稱相同(不區分大小寫)"});
					preloginevent.getConnection().setOnlineMode(true);
					break;
				case GENUINE_ACCOUNT:
					preloginevent.getConnection().setOnlineMode(true);
					break;
				case HTTP_ERROR:
					logManager.log(LogType.HTTP_ERROR);
					preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(
							ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：" + LogType.HTTP_ERROR.getErrorCode()));
					break;
				default:
					logManager.log(LogType.UNKNOWN_ERROR);
					preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
			}
			preloginevent.completeIntent(AccountCheck.getInstance());
		}
	}
}
