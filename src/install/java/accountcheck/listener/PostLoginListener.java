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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener{

	@EventHandler
	public void chooseServer(PostLoginEvent postloginevent) {
		String playername = postloginevent.getPlayer().getName();
		String ip = postloginevent.getPlayer().getAddress().toString();
		ip = ip.substring(1, ip.lastIndexOf(':'));
		Config config = AccountCheck.getInstance().getConfig();
		AccountCheckLogManager logManager = AccountCheck.getInstance().getLogManager();
		
		AccountInfo accountInfo = (!config.isPiratedAccessible() || config.isForceOnlineMode()) ? AccountInfo.GENUINE_ACCOUNT :
			AccountCheck.getInstance().getAccountManager().getInfo(postloginevent.getPlayer().getUniqueId(), playername, true);
		
		switch(accountInfo) {
			case PIRATED_ACCOUNT:
				postloginevent.getPlayer().setReconnectServer(ProxyServer.getInstance().getServers()
						.get(config.getPiratedLoginServer()));
				ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
				logManager.log(LogType.PIRATED_ACCOUNT_LOGIN, new String[] {playername, ip});
				break;
			case PIRATED_ACCOUNT_CASE_INSENSITIVE:
				logManager.log(LogType.REJECT_LOGIN, new String[] {playername, ip, "與正版名稱相同(不區分大小寫)"});
				postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.GOLD + "._."));
				break;
			case GENUINE_ACCOUNT:
				postloginevent.getPlayer().setReconnectServer(ProxyServer.getInstance().getServers()
						.get(config.getGenuineLoginServer()));
				ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
				logManager.log(LogType.GENUINE_ACCOUNT_LOGIN, new String[] {playername, ip});
				postloginevent.getPlayer().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ChatColor.GREEN + "歡迎正版玩家登入！"));
				break;
			case HTTP_ERROR:
				logManager.log(LogType.HTTP_ERROR);
				postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(
						ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼："+ LogType.HTTP_ERROR.getErrorCode()));
				break;
			default:
				logManager.log(LogType.UNKNOWN_ERROR);
				postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
		}
	}
}
