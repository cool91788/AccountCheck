package ins_AC.java.account;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AC_main extends Plugin {
	
	static String �����n�J;
	static String �s���n�J;
	static String executefolder;

	@Override
	public void onEnable() {
		
		�t�m�w�]�]�w��();
		Ū���]�w��();
		if(�ˬd�γ]�w���|())
			getLogger().info(ChatColor.RED + "�ѩ󥼳q�L�����ˬd�A�G������Ȯɰ���B�@�C" + ChatColor.YELLOW + "�ץ����~�᭫�_��i�ϥΡC");
		else {
			getProxy().getPluginManager().registerListener(this, new AC_checkListener(this));
			getProxy().getPluginManager().registerCommand(this, new AC_command(getDataFolder()));
		}
		getLogger().info(ChatColor.RED + "�����G1.4_BETA");
	}
	
	public boolean �ˬd�γ]�w���|() {
		getLogger().info("�ˬd���Ҥ��C�C�C");
		
		File executefile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") 
				+ "plugins", "AccountCheck.jar");
		executefolder = "java -jar " + System.getProperty("user.dir") + System.getProperty("file.separator")
				+ getDataFolder() + ".jar ";
		if(!executefile.exists()) {
			getLogger().warning(ChatColor.RED + "�бN������jar�ɩR�W��" + ChatColor.YELLOW + "AccountCheck.jar"+ ChatColor.RED + "�I�_�h�N�L�k���`�B��I");
			return true;
		}
		getLogger().info("�����I");
		executefile = null;
		return false;
	}
	
	public void �t�m�w�]�]�w��() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			try {
				getLogger().info("�䤣��]�w�ɡA�w�]�]�w�ɰt�m���C�C");
				Files.copy(getResourceAsStream("config.yml"), file.toPath());
				getLogger().info("�w�]�]�w�ɰt�m�����I");
			} catch (IOException e) {
				throw new RuntimeException("Can't create the config file! ", e);
			}
		}
		file = null;
	}
	
	public void Ū���]�w��() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			�����n�J = new String(configuration.getString("�����n�J�B"));
			�s���n�J = new String(configuration.getString("�s���n�J�B"));
			configuration = null;
		} catch (IOException e) {
			throw new RuntimeException("load config error!", e);
		}
	}
}