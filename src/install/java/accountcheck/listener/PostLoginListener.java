/*
 * 	AccountCheck (半正版驗證) - A BungeeCord plugin
 *	Copyright (C) 2016  Install
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package install.java.accountcheck.listener;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.accountinfo.GetAccountInfo;
import install.java.accountcheck.log.AccountCheckLog;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;

public class PostLoginListener {
	public void postLoginCheck(PostLoginEvent postloginevent, AccountCheckLog log) {
		String playername = postloginevent.getPlayer().getName();
		String ip = postloginevent.getPlayer().getAddress().toString();
		int postchecknumber = GetAccountInfo.getInfo(playername);
		switch(postchecknumber) {
		case 0:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers()
					.get(AccountCheck.getMainPluginObj().getPiracyLoginServer()));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log.log(0, playername, ip);
			break;
		case 1:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers()
					.get(AccountCheck.getMainPluginObj().getGenuineLoginServer()));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log.log(1, playername, ip);
			postloginevent.getPlayer().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ChatColor.GREEN + "歡迎正版玩家登入！"));
			break;
		case 100:
			log.log(100, playername, ip);
			postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：100"));
			break;
		default:
			log.log(1000, playername, ip);
			postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
		}
	}
}
