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

package install.java.accountcheck.log;

import install.java.accountcheck.AccountCheck;
import net.md_5.bungee.api.ChatColor;

public class AccountCheckLog {
	public void log(int msgnum, String name, String ip) {
		ip = ip.substring(1, ip.indexOf(':'));
		switch(msgnum) {
		case 0:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "盜版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 1:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "正版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 2:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + "盜版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 3:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + "正版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 4:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + "盜版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。根據規則，暫時拒絕盜版進入。");
			break;
		case 100:
			AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " 網頁開啟或關閉錯誤！");
			break;
		default:
			AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " 發生未知錯誤！");
		}
	}
}
