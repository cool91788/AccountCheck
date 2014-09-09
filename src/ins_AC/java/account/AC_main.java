package ins_AC.java.account;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AC_main extends Plugin {
	
	static String �����n�J;
	static String �s���n�J;

	@Override
	public void onEnable() {
		
		�t�m�w�]�]�w��();
		Ū���]�w��();
		getProxy().getPluginManager().registerListener(this, new AC_check());
		getLogger().info("�P�¨ϥΥb�������Ҵ���I�����G1.2.6");
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