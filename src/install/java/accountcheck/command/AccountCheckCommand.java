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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.accountinfo.AccountInfo;
import install.java.accountcheck.accountinfo.GetAccountInfo;
import install.java.accountcheck.fileprocess.FileConfigure;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AccountCheckCommand extends Command{
	
	private File config;		//設定檔的目錄
	
	public AccountCheckCommand(File config) {
		super("accountcheck", "accountcheck.command", "ac");
		this.config = config;
	}
	
	boolean reloadConfig() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new InputStreamReader(new FileInputStream(config), "UTF-8"));
			AccountCheck.getMainPluginObj().setGenuineLoginServer(configuration.getString("正版登入處", "GenuineLoginServer"));
			AccountCheck.getMainPluginObj().setPiratedLoginServer(configuration.getString("盜版登入處", "PiratedLoginServer"));
			AccountCheck.getMainPluginObj().setEnablePirated(configuration.getBoolean("盜版進入許可", false));
			AccountCheck.getMainPluginObj().setCaseSensitive(configuration.getBoolean("區分大小寫", true));
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	boolean isGenuine(String playername, CommandSender commandSender) {
		AccountInfo accountInfo = GetAccountInfo.getInfo(playername);
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
	
	private boolean setConfigHelper(CommandSender commandSender, String entry, boolean value) {
		return setConfigHelper(commandSender, entry, Boolean.toString(value));
	}
	
	private boolean setConfigHelper(CommandSender commandSender, String entry, String value) {
		boolean returnValue = new FileConfigure(config).setConfig(entry, value);
		if(returnValue)
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "成功").create());
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "失敗").create());
		return returnValue;
	}
	
	void setConfigCmd(CommandSender commandSender, String[] cmdargs) {
		if(cmdargs.length>2) {
			switch(cmdargs[1]) {
				case "genuinelogin":
					if(setConfigHelper(commandSender, "正版登入處", cmdargs[2]))
						AccountCheck.getMainPluginObj().setGenuineLoginServer(cmdargs[2]);
					break;
				case "piracylogin":
					if(setConfigHelper(commandSender, "盜版登入處", cmdargs[2]))
						AccountCheck.getMainPluginObj().setPiratedLoginServer(cmdargs[2]);
					break;
				case "piracyaccess":
					if(cmdargs[2].equals("true")) {
						if(setConfigHelper(commandSender, "盜版進入許可", true))
							AccountCheck.getMainPluginObj().setEnablePirated(true);
					}else if(cmdargs[2].equals("false")) {
						if(setConfigHelper(commandSender, "盜版進入許可", false))
							AccountCheck.getMainPluginObj().setEnablePirated(false);
					}else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "參數錯誤，輸入/ac set查閱？").create());
					break;
				case "casesensitive":
					if(cmdargs[2].equals("true")) {
						if(setConfigHelper(commandSender, "區分大小寫", true))
							AccountCheck.getMainPluginObj().setCaseSensitive(true);
					}else if(cmdargs[2].equals("false")) {
						if(setConfigHelper(commandSender, "區分大小寫", false))
							AccountCheck.getMainPluginObj().setCaseSensitive(false);
					}else
						commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "參數錯誤，輸入/ac set查閱？").create());
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
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set piracyaccess [true|false]").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->設定盜版進入許可。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac set casesensitive [true|false]").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW+ "--->當值為 \"true\" 時，若玩家名稱與正版名稱").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW+ "拼寫相同（大小寫不同）時，則視為盜版玩家且「關閉」正版驗證。").create());
			commandSender.sendMessage(new ComponentBuilder("").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW+ "--->當值為 \"false\" 時，若玩家名稱與正版名稱").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW+ "拼寫相同（大小寫不同）時，則視為盜版玩家且「開啟」正版驗證（拒絕進入）。").create());
			commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
		}
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] cmdargs) {
		if(cmdargs.length!=0) {
			switch(cmdargs[0]) {
			case "reload":
				if(reloadConfig())
					commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "重新讀取設定檔完成！").create());
				else
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "重新讀取設定檔失敗！請檢查是否有無其他錯誤！").create());
				break;
			case "help":
				commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件v" + AccountCheck.VERSION + "說明檔<--")
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
				break;
			case "version":
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "AccountCheck v" + AccountCheck.VERSION).create());
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
				setConfigCmd(commandSender, cmdargs);
				break;
			case "info":
				commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件v" + AccountCheck.VERSION + "狀態表<--")
						.color(ChatColor.RED).bold(true).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "正版登入處: " 
						+ ChatColor.AQUA + AccountCheck.getMainPluginObj().getGenuineLoginServer()).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版登入處: " 
						+ ChatColor.AQUA + AccountCheck.getMainPluginObj().getPiratedLoginServer()).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "盜版進入許可: " 
						+ (AccountCheck.getMainPluginObj().isEnablePirated() ? ChatColor.GREEN : ChatColor.RED) 
						+ AccountCheck.getMainPluginObj().isEnablePirated()).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GOLD + "是否區分大小寫: " 
						+ (AccountCheck.getMainPluginObj().isCaseSensitive() ? ChatColor.GREEN : ChatColor.RED) 
						+ AccountCheck.getMainPluginObj().isCaseSensitive()).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				break;
			default:
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "無此指令！請輸入/ac help查閱！").create());
			}
		}
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "半正版驗證插件。欲知詳細請輸入/ac help.").create());
		
	}
	
}
