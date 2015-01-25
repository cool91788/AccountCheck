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
			AC_main.�����n�J = new String(configuration.getString("�����n�J�B"));
			AC_main.�s���n�J = new String(configuration.getString("�s���n�J�B"));
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
			case "���sŪ��":
				if(reloadconfig())
					commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "���sŪ���]�w�ɧ����I").create());
				else
					commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "���sŪ���]�w�ɥ��ѡI���ˬd�O�_���L��L���~�I").create());
				break;
			case "help":
			case "?":
			case "����":
				commandSender.sendMessage(new ComponentBuilder("-->�b�������Ҵ���v1.4_BETA������<--").color(ChatColor.RED).bold(true).create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "�e����O�i�H���G"
						+ ChatColor.GREEN + "\"accountcheck\"" + ChatColor.YELLOW+ " or "
						+ ChatColor.GREEN + "\"ac\"" + ChatColor.YELLOW + "�A�ܽd�H\"ac\"²�g�C").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac reload" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac ���sŪ��").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->���sŪ���]�w�ɡC").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac help" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac ?" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac ����").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->���U�����C").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/ac version" + ChatColor.YELLOW + " or "
						+ ChatColor.GREEN + "/ac v").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "--->�d�ݥثe�B�檩���C").create());
				commandSender.sendMessage(new ComponentBuilder(ChatColor.BLUE + "<===================================>").create());
				break;
			case "version":
			case "v":
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "v1.4_BETA").create());
				break;
			default:
				commandSender.sendMessage(new ComponentBuilder(ChatColor.RED + "�L�����O�I�п�J/ac help�d�\�I").create());
			}
		}
		else
			commandSender.sendMessage(new ComponentBuilder(ChatColor.YELLOW + "�b�������Ҵ���C�����Բӽп�J/ac help.").create());
		
	}
}
