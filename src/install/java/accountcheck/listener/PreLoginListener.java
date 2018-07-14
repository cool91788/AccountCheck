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
import install.java.accountcheck.accountinfo.AccountInfo;
import install.java.accountcheck.accountinfo.GetAccountInfo;
import install.java.accountcheck.log.AccountCheckLog;
import install.java.accountcheck.log.LogType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;

public class PreLoginListener {
	public void preLoginCheck(PreLoginEvent preloginevent, AccountCheckLog log) {
		String playername = preloginevent.getConnection().getName();
		String ip = preloginevent.getConnection().getAddress().toString();
		AccountInfo accountInfo = GetAccountInfo.getInfo(playername);
		switch(accountInfo) {
		case PIRATED_ACCOUNT:
			log.log(LogType.PIRATED_ACCOUNT_CONNECT, playername, ip);
			if(AccountCheck.getMainPluginObj().isEnablePirated())
				preloginevent.getConnection().setOnlineMode(false);
			else {
				log.log(LogType.REJECT_PIRATED_ACCOUNT_LOGIN, playername, ip);
				preloginevent.getConnection().setOnlineMode(true);
			}
			break;
		case PIRATED_ACCOUNT_CASE_INSENSITIVE:
			log.log(LogType.PIRATED_ACCOUNT_CONNECT, playername, ip);
			if(AccountCheck.getMainPluginObj().isEnablePirated() && AccountCheck.getMainPluginObj().isCaseSensitive())
				preloginevent.getConnection().setOnlineMode(false);
			else {
				log.log(LogType.REJECT_PIRATED_ACCOUNT_LOGIN, playername, ip);
				preloginevent.getConnection().setOnlineMode(true);
			}
			break;
		case GENUINE_ACCOUNT:
			log.log(LogType.GENUINE_ACCOUNT_CONNECT, playername, ip);
			preloginevent.getConnection().setOnlineMode(true);
			break;
		case HTTP_ERROR:
			log.log(LogType.HTTP_ERROR, playername, ip);
			preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(
					ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：" + LogType.HTTP_ERROR.getErrorCode()));
			break;
		default:
			log.log(LogType.UNKNOWN_ERROR, playername, ip);
			preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
		}
	}
}
