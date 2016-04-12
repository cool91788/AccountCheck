package install.java.accountcheck.log;

import install.java.accountcheck.AccountCheck;
import net.md_5.bungee.api.ChatColor;

public class AccountCheckLog {
	public void log(int msgnum, String name, String ip) {
		ip = ip.substring(1, ip.indexOf(':'));
		switch(msgnum) {
		case 0:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"�T��" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "�s�����a�G" + name + ChatColor.GREEN + "�A�ӦۡG" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "�C�n�J�I");
			break;
		case 1:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"�T��" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "�������a�G" + name + ChatColor.GREEN + "�A�ӦۡG" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "�C�n�J�I");
			break;
		case 2:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"�T��" 
					+ ChatColor.GREEN + "] " + "�s�����a�G" + name + "�A�ӦۡG" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "�C���ճs���C");
			break;
		case 3:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"�T��" 
					+ ChatColor.GREEN + "] " + "�������a�G" + name + "�A�ӦۡG" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "�C���ճs���C");
			break;
		case 4:
			AccountCheck.getMainPluginObj().getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"�T��" 
					+ ChatColor.GREEN + "] " + "�s�����a�G" + name + "�A�ӦۡG" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "�C�ھڳW�h�A�Ȯɩڵ��s���i�J�C");
			break;
		case 100:
			AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "���~" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " �o�Ϳ��~�N�X��\"100\"�����~�I");
			break;
		case 101:
			AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "���~" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " �����}�ҩ��������~�I");
			break;
		default:
			AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "���~" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " �o�ͥ������~�I");
		}
	}
}
