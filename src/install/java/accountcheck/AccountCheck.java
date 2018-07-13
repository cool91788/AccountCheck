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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Logger;

import install.java.accountcheck.command.AccountCheckCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class AccountCheck extends Plugin {
	
	private AccountCheckLogger logger;//自訂log格式
	private String genuineLoginServer;	//正版登入伺服器
	private String piratedLoginServer;	//盜版登入伺服器
	private String executeFolder;		//這個jar的目錄
	private static AccountCheck mainPluginObj;		//指向這個class的物件
	private boolean piratedAccess = false;//是否開放盜版玩家進入，預設關閉。
	private boolean caseSensitive = true;	//當值為 "true" 時，若玩家名稱與正版名稱拼寫相同（大小寫不同）時，則視為盜版玩家且「關閉」正版驗證。
											//當值為 "false" 時，若玩家名稱與正版名稱拼寫相同（大小寫不同）時，則視為盜版玩家且「開啟」正版驗證（拒絕進入）。
											//預設為true。
	public final static String VERSION = "1.6";
	
	public static AccountCheck getMainPluginObj() {return mainPluginObj;}
	public String getGenuineLoginServer() {return genuineLoginServer;}
	public String getPiratedLoginServer() {return piratedLoginServer;}
	public String getExecuteFolder() {return executeFolder;}
	public boolean isEnablePirated() {return piratedAccess;}
	public boolean isCaseSensitive()  {return caseSensitive;}
	
	public void setEnablePirated(boolean piratedAccess) {this.piratedAccess = piratedAccess;}
	public void setCaseSensitive(boolean caseSensitive) {this.caseSensitive = caseSensitive;}
	public void setGenuineLoginServer(String genuineLoginServer) {this.genuineLoginServer = genuineLoginServer;}
	public void setPiratedLoginServer(String piratedLoginServer) {this.piratedLoginServer = piratedLoginServer;}
	
	@Override
	public Logger getLogger() {return logger;}
	

	@Override
	public void onEnable() {
		//把這個class的位址放到 mainPluginObj 儲存
		mainPluginObj = this;
		
		//自訂log格式
		logger = new AccountCheckLogger(this);
		
		//檢查以及設定設定檔
		setConfig();
		loadConfig();
		
		//檢查運行環境是否符合需求
		if(checkEnvironment())
			getLogger().info(ChatColor.RED + "由於未通過環境檢查，故本插件暫時停止運作。" + ChatColor.YELLOW + "修正錯誤後重起方可使用。");
		else {
			getProxy().getPluginManager().registerListener(this, new CreateListener());
			getProxy().getPluginManager().registerCommand(this, new AccountCheckCommand(new File(getDataFolder(), "config.yml")));
		}
		getLogger().info(ChatColor.RED + "版本：v" + VERSION);
	}
	
	boolean checkEnvironment() {
		getLogger().info("檢查環境中。。。");
		
		File executefile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") 
				+ "plugins", "AccountCheck.jar");
		executeFolder = "java -jar \"" + System.getProperty("user.dir") + System.getProperty("file.separator")
				+ getDataFolder() + ".jar\" ";
		if(!executefile.exists()) {
			getLogger().warning(ChatColor.RED + "請將本插件jar檔命名成" + ChatColor.YELLOW + "AccountCheck.jar"+ ChatColor.RED + "！否則將無法正常運行！");
			return true;
		}
		getLogger().info("完成！");
		return false;
	}
	
	void setConfig() {
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
	}
	
	void loadConfig() {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new InputStreamReader(new FileInputStream(new File(getDataFolder(), "config.yml")), "UTF-8"));
			genuineLoginServer = configuration.getString("正版登入處", "GenuineLoginServer");
			piratedLoginServer = configuration.getString("盜版登入處", "PiratedLoginServer");
			piratedAccess = configuration.getBoolean("盜版進入許可", false);
			caseSensitive = configuration.getBoolean("區分大小寫", true);
			
		} catch (IOException e) {
			throw new RuntimeException("load config error!", e);
		}
	}
	
}