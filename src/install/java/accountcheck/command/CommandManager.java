/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2014-2018  Install
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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class CommandManager extends Command{
	
	private final Basic basic = new Basic();
	private final Set set = new Set();
	
	public CommandManager() {
		super("accountcheck", null, "ac");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length != 0) {
			permission_check: {
				switch(args[0]) {
					case "reload":
						if(basic.hasPermission(sender, Basic.Permission.RELOAD)) {
							basic.reload(sender);
							break permission_check;
						}
						break;
					case "help":
						basic.help(sender);
						break permission_check;
					case "version":
						basic.version(sender);
						break permission_check;
					case "inquire":
						if(basic.hasPermission(sender, Basic.Permission.INQUIRE)) {
							if(args.length > 1)
								basic.inquire(sender, args[1]);
							else
								sender.sendMessage(new ComponentBuilder(ChatColor.RED + "少了一些參數唷～請輸入/ac help查閱！").create());
							break permission_check;
						}
						break;
					case "set":
						if(basic.hasPermission(sender, Basic.Permission.SET)) {
							set.set(sender, args);
							break permission_check;
						}
						break;
					case "info":
						if(basic.hasPermission(sender, Basic.Permission.INFO)) {
							basic.info(sender);
							break permission_check;
						}
						break;
					default:
						sender.sendMessage(new ComponentBuilder(ChatColor.RED + "無此指令！請輸入/ac help查閱！").create());
						break permission_check;
				}// end of switch
				sender.sendMessage(new ComponentBuilder(ChatColor.RED + "您沒有權限執行此命令。").create());
			}// end of commands
		}else {
			basic.version(sender);
			sender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "半正版驗證插件。欲知詳細請輸入/ac help.").create());
		}
	}
}
