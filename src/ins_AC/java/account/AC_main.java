package ins_AC.java.account;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AC_main extends Plugin {
	
	static String 正版登入;
	static String 盜版登入;

	@Override
	public void onEnable() {
		
		配置預設設定檔();
		讀取設定檔();
		getProxy().getPluginManager().registerListener(this, new AC_check());
		getLogger().info("感謝使用半正版驗證插件！版本：1.2.7_U");
	}
	
	public void 配置預設設定檔() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		File file = new File(getDataFolder(), "config.yml");
		
		if (!file.exists()) {
			try {
				getLogger().info("找不到設定檔，預設設定檔配置中。。");
				Files.copy(getResourceAsStream("config.yml"), file.toPath());
				getLogger().info("預設設定檔配置完成！");
			} catch (IOException e) {
				throw new RuntimeException("Can't create the config file! ", e);
			}
		}
		file = null;
	}
	
	public void 讀取設定檔() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			正版登入 = new String(configuration.getString("正版登入處"));
			盜版登入 = new String(configuration.getString("盜版登入處"));
			configuration = null;
		} catch (IOException e) {
			throw new RuntimeException("load config error!", e);
		}
	}
}