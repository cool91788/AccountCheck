/*
 * 	AccountCheck (半正版驗證) - A BungeeCord plugin
 *	Copyright (C) 2014  Install
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

package install.java.accountcheck;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AccountCheckCommand extends Command{
	
	private static File folder;		//設定檔的目錄
	
	public AccountCheckCommand(File folder) {
		super("accountcheck", "accountcheck.command", "ac");
		AccountCheckCommand.folder = folder;
	}
	
	public boolean reloadConfig() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(folder, "config.yml"));
			AccountCheck.originalLoginServer = new String(configuration.getString("正版登入處"));
			AccountCheck.pirateLoginServer = new String(configuration.getString("盜版登入處"));
			configuration = null;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] cmdargs) {
		if(cmdargs.length!=0) {
			switch(cmdargs[0]) {
			case "reload":
			case "重新讀取":
				if(reloadConfig())
					commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "重新讀取設定檔完成！").create());
				else
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "重新讀取設定檔失敗！請檢查是否有無其他錯誤！").create());
				break;
			case "help":
			case "?":
			case "說明":
				commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件v" + AccountCheck.VERSION + "說明檔<--").color(ChatColor.RED).bold(true).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "前綴指令可以打："
						+ ChatColor.GREEN + "\"accountcheck\"" + ChatColor.YELLOW+ " or "
						+ ChatColor.GREEN + "\"ac\"" + ChatColor.YELLOW + "，示範以\"ac\"簡寫。").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac reload" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac 重新讀取").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->重新讀取設定檔。").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac help" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac ?" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac 說明").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->幫助說明。").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac version" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac v").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->查看目前運行版本。").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				break;
			case "version":
			case "v":
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "AccountCheck" + AccountCheck.VERSION).create());
				break;
			default:
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "無此指令！請輸入/ac help查閱！").create());
			}
		}
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "半正版驗證插件。欲知詳細請輸入/ac help.").create());
		
	}
}
