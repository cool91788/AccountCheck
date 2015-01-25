package ins_AC.java.account;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AC_command extends Command{
	
	private static File folder;
	
	public AC_command(File folder) {
		super("accountcheck", "accountcheck.command", "ac");
		AC_command.folder = folder;
	}
	
	public boolean reloadconfig() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(folder, "config.yml"));
			AC_main.正版登入 = new String(configuration.getString("正版登入處"));
			AC_main.盜版登入 = new String(configuration.getString("盜版登入處"));
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
				if(reloadconfig())
					commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "重新讀取設定檔完成！").create());
				else
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "重新讀取設定檔失敗！請檢查是否有無其他錯誤！").create());
				break;
			case "help":
			case "?":
			case "說明":
				commandSender.sendMessage(new ComponentBuilder("-->半正版驗證插件v1.4_BETA說明檔<--").color(ChatColor.RED).bold(true).create());
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
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "v1.4_BETA").create());
				break;
			default:
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "無此指令！請輸入/ac help查閱！").create());
			}
		}
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "半正版驗證插件。欲知詳細請輸入/ac help.").create());
		
	}
}
