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

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.account.AccountInfo;
import install.java.accountcheck.yaml.Config;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

// Basic commands
class Basic {
	
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	enum Permission {
		RELOAD("accountcheck.command.reload"),
		INQUIRE("accountcheck.command.inquire"),
		INFO("accountcheck.command.info"),
		SET("accountcheck.command.set"),
		ALL("accountcheck.command.all");	// has full of permission.
		
		private final String permission;

		@Override
		public String toString() {return permission;}
		
	}
	
	boolean hasPermission(CommandSender sender, Permission permission) {
		return (sender.hasPermission(Permission.ALL.toString()) || sender.hasPermission(permission.toString()));
	}
	
	void reload(CommandSender sender) {	
		try {
			AccountCheck.getInstance().getConfig().load();
			AccountCheck.getInstance().updatePingTask(AccountCheck.getInstance().getConfig().getPingInterval());
			sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "重新讀取設定檔完成！").create());
		}catch (FileNotFoundException exception) {
			sender.sendMessage(new ComponentBuilder(ChatColor.RED + "重新讀取設定檔失敗！").create());
		}
	}
	
	void inquire(CommandSender sender, String playername) {
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "[" + ChatColor.GREEN + "AccountCheck" 
				+ ChatColor.GOLD + "]  " + ChatColor.AQUA + "給我一點時間查一下唷～").create());
		AccountInfo accountInfo = AccountCheck.getInstance().getAccountManager().getInfo(playername, false);
		String resault = null;
		switch(accountInfo) {
			case PIRATED_ACCOUNT:
			case PIRATED_ACCOUNT_CASE_INSENSITIVE:
				resault = "盜版玩家";
				break;
			case GENUINE_ACCOUNT:
				resault = "正版玩家";
				break;
			default:
				sender.sendMessage(new ComponentBuilder(ChatColor.RED + "發生錯誤，以下查詢失敗！預設給予盜版！").create());
				resault = "盜版玩家";
		}
		sender.sendMessage(new ComponentBuilder(ChatColor.DARK_RED + playername + ChatColor.YELLOW 
				+ " 是 " + resault + "。").create());
	}
	
	void version(CommandSender sender) {
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + ChatColor.BOLD.toString() + "AccountCheck <半正版驗證> " +
				ChatColor.RED + ChatColor.BOLD.toString() + AccountCheck.getInstance().getDescription().getVersion()).create());
	}
	
	void help(CommandSender sender) {
		sender.sendMessage(new ComponentBuilder(ChatColor.RED + ChatColor.BOLD.toString()
				+ "-->半正版驗證插件 " + AccountCheck.getInstance().getDescription().getVersion() + " 說明檔<--").create());
		sender.sendMessage(new ComponentBuilder("---").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "前綴指令可以打："
				+ ChatColor.GREEN + "\"accountcheck\"" + ChatColor.YELLOW+ " or "
				+ ChatColor.GREEN + "\"ac\"" + ChatColor.YELLOW + "，示範以\"ac\"簡寫。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac reload").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->重新讀取設定檔。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac help").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->幫助說明。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac version").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查看目前運行版本。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac inquire <玩家名>").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查詢該玩家是正版還是盜版。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set [option] [value]").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->功能設定。詳細請輸入" 
				+ ChatColor.GREEN + "/ac set" + ChatColor.YELLOW + "取得相關說明文件。").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac info").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查看目前狀態。").create());
		sender.sendMessage(new ComponentBuilder("---").create());
		sender.sendMessage(new ComponentBuilder("第 1 頁，共 1 頁。").create());
	}
	
	void info(CommandSender sender) {
		Config config = AccountCheck.getInstance().getConfig();
		sender.sendMessage(new ComponentBuilder("-->半正版驗證插件 " + AccountCheck.getInstance().getDescription().getVersion() + " 狀態表<--")
				.color(ChatColor.RED).bold(true).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "正版登入處: " 
				+ ChatColor.AQUA + config.getGenuineLoginServer()).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版登入處: " 
				+ ChatColor.AQUA + config.getPiratedLoginServer()).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版進入許可: " 
				+ (config.isPiratedAccessible() ? ChatColor.GREEN : ChatColor.RED) 
				+ config.isPiratedAccessible()).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "檢查間隔: " 
				+ ChatColor.AQUA + config.getPingInterval() + ChatColor.GOLD + " 秒").create());
		sender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
	}
}
