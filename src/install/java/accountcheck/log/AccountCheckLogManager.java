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

package install.java.accountcheck.log;

import java.util.logging.Logger;

import install.java.accountcheck.AccountCheck;
import net.md_5.bungee.api.ChatColor;

public class AccountCheckLogManager {
	
	private final AccountCheckLogger logger;
	
	public Logger getLogger() {return logger;}
	
	public AccountCheckLogManager(AccountCheck plugin) {logger = new AccountCheckLogger(plugin);}
	
	public void log(LogType logtype) {log(logtype, null);}
	
	public void log(LogType logtype, String logArgs[]) {
		switch(logtype) {
			case PIRATED_ACCOUNT_LOGIN:
				//LogArgs format String[] {name, ip}
				logger.info(ChatColor.YELLOW + "盜版玩家：" + ChatColor.AQUA + logArgs[0] + ChatColor.RESET + "，來自：" 
						+ ChatColor.RED + logArgs[1] + ChatColor.YELLOW + "。登入！");
				break;
			case GENUINE_ACCOUNT_LOGIN:
				// LogArgs format String[] {name, ip}
				logger.info(ChatColor.YELLOW + "正版玩家：" + ChatColor.AQUA + logArgs[0] + ChatColor.RESET + "，來自：" 
						+ ChatColor.RED + logArgs[1] + ChatColor.YELLOW + "。登入！");
				break;
			case PLAYER_CONNECT:
				// LogArgs format String[] {name, ip}
				logger.info("玩家：" + ChatColor.AQUA + logArgs[0] + ChatColor.RESET + "，來自：" 
						+ ChatColor.RED + logArgs[1]  + ChatColor.RESET + "。嘗試連結。");
				break;
			case REJECT_LOGIN:
				// LogArgs format String[] {reason}
				logger.info("拒絕登入原因： " + ChatColor.RED + logArgs[0]);
				break;
			case HTTP_ERROR:
				// LogArgs format {null}
				logger.warning(ChatColor.RED + " 網頁查詢錯誤！");
				break;
			default:
				// LogArgs format {null}
				logger.severe(ChatColor.RED + " 發生未知錯誤！");
		}
	}
}
