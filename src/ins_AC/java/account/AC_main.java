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
	
	static String 正版登入;
	static String 盜版登入;
	static String executefolder;

	@Override
	public void onEnable() {
		
		配置預設設定檔();
		讀取設定檔();
		if(檢查及設定路徑())
			getLogger().info(ChatColor.RED + "由於未通過環境檢查，故本插件暫時停止運作。" + ChatColor.YELLOW + "修正錯誤後重起方可使用。");
		else {
			getProxy().getPluginManager().registerListener(this, new AC_checkListener(this));
			getProxy().getPluginManager().registerCommand(this, new AC_command(getDataFolder()));
		}
		getLogger().info(ChatColor.RED + "版本：1.4_BETA");
	}
	
	public boolean 檢查及設定路徑() {
		getLogger().info("檢查環境中。。。");
		
		File executefile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") 
				+ "plugins", "AccountCheck.jar");
		executefolder = "java -jar " + System.getProperty("user.dir") + System.getProperty("file.separator")
				+ getDataFolder() + ".jar ";
		if(!executefile.exists()) {
			getLogger().warning(ChatColor.RED + "請將本插件jar檔命名成" + ChatColor.YELLOW + "AccountCheck.jar"+ ChatColor.RED + "！否則將無法正常運行！");
			return true;
		}
		getLogger().info("完成！");
		executefile = null;
		return false;
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