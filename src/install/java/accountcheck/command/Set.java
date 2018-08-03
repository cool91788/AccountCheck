/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2018  Install
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

package install.java.accountcheck.command;

import java.io.FileNotFoundException;
import java.io.IOException;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.yaml.Config;
import install.java.accountcheck.yaml.ConfigEntry;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

// Set commands
class Set {
	
	private final Config config = AccountCheck.getInstance().getConfig();
	private CommandSender sender;
	
	void set(CommandSender sender, String[] args) {
		this.sender = sender;
		if(args.length > 1) {
			switch(args[1]) {
				case "genuinelogin":
					genuineLogin(args[2]);
					break;
				case "piracylogin":
					piracyLogin(args[2]);
					break;
				case "piratedaccess":
					if(args[2].equalsIgnoreCase("true"))
						piratedAccess(true);
					else if(args[2].equalsIgnoreCase("false"))
						piratedAccess(false);
					else
						sender.sendMessage(new ComponentBuilder(ChatColor.RED + "只限定\"true\"或\"false\"").create());
					break;
				case "checkinterval":
					if(args[2].matches("^\\d+$"))
						checkInterval(Integer.parseInt(args[2]));
					else
						sender.sendMessage(new ComponentBuilder(ChatColor.RED + "只限定純阿拉伯數字、正整數。").create());
					break;
				case "help":
					help(sender);
					break;
				default:
					sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "未知的參數，輸入/ac set help取得幫助").create());
			}// end of switch
		}else
			sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "輸入/ac set help取得幫助").create());
	}
	
	void help(CommandSender sender) {
		sender.sendMessage(new ComponentBuilder(ChatColor.RED + ChatColor.BOLD.toString()
		+ "-->半正版驗證插件 " + AccountCheck.getInstance().getDescription().getVersion() + " 說明檔<--").create());
		sender.sendMessage(new ComponentBuilder("---").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set help").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->幫助說明。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set genuinelogin <servername>").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定正版登入處。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set piracylogin <servername>").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定盜版登入處。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set piratedaccess [true|false]").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定盜版進入許可。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set checkinterval [second]").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定檢查盜版登入伺服器狀態的時間間隔。").create());
		sender.sendMessage(new ComponentBuilder("---").create());
		sender.sendMessage(new ComponentBuilder("第 1 頁，共 1 頁。").create());
	}

	private boolean setter(ConfigEntry entry, Object newValue) {
		try {
			AccountCheck.getInstance().getConfig().set(entry, newValue);
			sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "成功").create());
			return true;
		}catch (FileNotFoundException exception) {
			sender.sendMessage(new ComponentBuilder(ChatColor.RED + "失敗").create());
			return false;
		}catch (IOException exception) {
			sender.sendMessage(new ComponentBuilder(ChatColor.RED + "失敗").create());
			return false;
		}
	}
	
	private void genuineLogin(String server) {
		if(setter(ConfigEntry.GENUINE_LOGIN_SERVER, server))
			config.setGenuineLoginServer(server);
	}
	
	private void piracyLogin(String server) {
		if(setter(ConfigEntry.PIRATED_LOGIN_SERVER, server)) {
			config.setPiratedLoginServer(server);
			AccountCheck.getInstance().updatePingTask(config.getPingInterval());
		}
	}
	
	private void piratedAccess(boolean value) {
		if(setter(ConfigEntry.PIRATED_ACCESSIBLE, value))
			config.setPiratedAccessible(value);
	}
	
	private void checkInterval(int interval) {
		if(setter(ConfigEntry.PING_INTERVAL, interval)) {
			config.setPingInterval(interval);
			AccountCheck.getInstance().updatePingTask(interval);
		}
	}
	
}
