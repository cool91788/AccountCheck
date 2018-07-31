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

package install.java.accountcheck.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.account.AccountInfo;
import install.java.accountcheck.yaml.Config;
import install.java.accountcheck.yaml.ConfigEntry;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class AccountCheckCommand extends Command{
	
	public AccountCheckCommand() {
		super("accountcheck", "accountcheck.command", "ac");
	}
	
	private boolean reloadConfig() {
		try {
			AccountCheck.getInstance().getConfig().loadConfig();
			return true;
		}catch (FileNotFoundException exception) {
			return false;
		}
	}
	
	private boolean isGenuine(String playername, CommandSender commandSender) {
		AccountInfo accountInfo = AccountCheck.getInstance().getAccountManager().getInfo(playername, false);
		switch(accountInfo) {
			case PIRATED_ACCOUNT:
			case PIRATED_ACCOUNT_CASE_INSENSITIVE:
				return false;
			case GENUINE_ACCOUNT:
				return true;
			default:
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "發生錯誤，以下查詢失敗！預設給予盜版！").create());
				return false;
		}
	}
	
	private boolean setter(CommandSender commandSender, ConfigEntry entry, Object newValue) {
		try {
			AccountCheck.getInstance().getConfig().setConfig(entry, newValue);
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "成功").create());
			return true;
		}catch (FileNotFoundException exception) {
			commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "失敗").create());
			return false;
		}catch (IOException exception) {
			commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "失敗").create());
			return false;
		}
	}
	
	private void set(CommandSender commandSender, String[] cmdargs) {
		if(cmdargs.length>2) {
			Config config = AccountCheck.getInstance().getConfig();
			switch(cmdargs[1]) {
				case "genuinelogin":
					if(setter(commandSender, ConfigEntry.GENUINE_LOGIN_SERVER, cmdargs[2]))
						config.setGenuineLoginServer(cmdargs[2]);
					break;
				case "piracylogin":
					if(setter(commandSender, ConfigEntry.PIRATED_LOGIN_SERVER, cmdargs[2]))
						config.setPiratedLoginServer(cmdargs[2]);
					break;
				case "piratedaccess":
					if(cmdargs[2].equalsIgnoreCase("true")) {
						if(setter(commandSender, ConfigEntry.PIRATED_ACCESSIBLE, true))
							config.setPiratedAccessible(true);
					}else if(cmdargs[2].equalsIgnoreCase("false")) {
						if(setter(commandSender, ConfigEntry.PIRATED_ACCESSIBLE, false))
							config.setPiratedAccessible(false);
					}else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "只限定\"true\"或\"false\"").create());
					break;
				case "checkinterval":
					if(cmdargs[2].matches("^\\d+$")) {
						int interval = Integer.parseInt(cmdargs[2]);
						if(setter(commandSender, ConfigEntry.PING_INTERVAL, interval)) {
							config.setPingInterval(interval);
							AccountCheck.getInstance().modifyPingTask(interval);
						}
					}else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "只限定純阿拉伯數字、正整數。").create());
					break;
				default:
					commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "未知的參數，輸入/ac set查閱？").create());
			}
		}else {
			commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set genuinelogin <servername>").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定正版登入處。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set piracylogin <servername>").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定盜版登入處。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set piratedaccess [true|false]").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定盜版進入許可。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set checkinterval [second]").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定檢查盜版登入伺服器狀態的時間間隔。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
		}
	}

	private void info(CommandSender commandSender){
		Config config = AccountCheck.getInstance().getConfig();
		commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件 " + AccountCheck.getInstance().getDescription().getVersion() + " 狀態表<--")
				.color(ChatColor.RED).bold(true).create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "正版登入處: " 
				+ ChatColor.AQUA + config.getGenuineLoginServer()).create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版登入處: " 
				+ ChatColor.AQUA + config.getPiratedLoginServer()).create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版進入許可: " 
				+ (config.isPiratedAccessible() ? ChatColor.GREEN : ChatColor.RED) 
				+ config.isPiratedAccessible()).create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "檢查間隔: " 
				+ ChatColor.AQUA + config.getPingInterval() + ChatColor.GOLD + " 秒").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
	}

	private void help(CommandSender commandSender) {
		commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件 " + AccountCheck.getInstance().getDescription().getVersion() + " 說明檔<--")
				.color(ChatColor.RED).bold(true).create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "前綴指令可以打："
				+ ChatColor.GREEN + "\"accountcheck\"" + ChatColor.YELLOW+ " or "
				+ ChatColor.GREEN + "\"ac\"" + ChatColor.YELLOW + "，示範以\"ac\"簡寫。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac reload").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->重新讀取設定檔。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac help").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->幫助說明。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac version").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查看目前運行版本。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac inquire <玩家名>").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查詢該玩家是正版還是盜版。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set [option] [value]").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->功能設定。詳細請輸入" 
				+ ChatColor.GREEN + "/ac set" + ChatColor.YELLOW + "取得相關說明文件。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac info").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查看目前狀態。").create());
		commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] cmdargs) {
		if(cmdargs.length!=0) {
			switch(cmdargs[0]) {
				case "reload":
					if(reloadConfig())
						commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "重新讀取設定檔完成！").create());
					else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "重新讀取設定檔失敗！").create());
					break;
				case "help":
					help(commandSender);
					break;
				case "version":
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "AccountCheck " + AccountCheck.getInstance().getDescription().getVersion()).create());
					break;
				case "inquire":
					if(cmdargs.length>1) {
						commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "[" + ChatColor.GREEN + "AccountCheck" 
								+ ChatColor.GOLD + "]  " + ChatColor.AQUA + "給我一點時間查一下唷～").create());
						commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "[" + ChatColor.GREEN + "AccountCheck" 
								+ ChatColor.GOLD + "]  " + ChatColor.DARK_RED + cmdargs[1]+ ChatColor.YELLOW 
								+ (isGenuine(cmdargs[1], commandSender) ?  " 是正版玩家。" : " 是盜版玩家。")).create());
					}else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "少了一些參數唷～請輸入/ac help查閱！").create());
					break;
				case "set":
					set(commandSender, cmdargs);
					break;
				case "info":
					info(commandSender);
					break;
				default:
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "無此指令！請輸入/ac help查閱！").create());
			}
		}
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "半正版驗證插件。欲知詳細請輸入/ac help.").create());
		
	}
	
}
