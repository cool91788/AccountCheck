/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2018  Install
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

package install.java.accountcheck.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import install.java.accountcheck.AccountCheck;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@RequiredArgsConstructor
public class Config {
	
	@Getter@Setter
	private String genuineLoginServer = "GenuineLoginServer";	// 正版登入伺服器
	@Getter@Setter
	private String piratedLoginServer = "PiratedLoginServer";	// 盜版登入伺服器
	@Getter@Setter
	private boolean piratedAccessible = false;	// 是否開放盜版玩家進入，預設關閉。
	@Getter@Setter
	private int pingInterval = 5;	// 檢查盜版登入伺服器的時間間隔，單位秒。
	
	// dynamic settings, used by listener.
	@Getter@Setter
	private boolean forceOnlineMode = false;
	@Getter@Setter
	private boolean piratedLoginServerOffline = false;
	
	private final File configFile;
	
	public void load() throws FileNotFoundException{
		try(InputStreamReader is = new InputStreamReader(new FileInputStream(configFile), "UTF-8");) {
			
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(is);
			genuineLoginServer = configuration.getString(ConfigEntry.GENUINE_LOGIN_SERVER.toString(), "GenuineLoginServer");
			piratedLoginServer = configuration.getString(ConfigEntry.PIRATED_LOGIN_SERVER.toString(), "PiratedLoginServer");
			piratedAccessible = configuration.getBoolean(ConfigEntry.PIRATED_ACCESSIBLE.toString(), false);
			pingInterval = configuration.getInt(ConfigEntry.PING_INTERVAL.toString(), 5);
		}catch (UnsupportedEncodingException exception) {
			// do nothing
		}catch (FileNotFoundException exception) {
			throw exception;
		}catch (IOException exception) {
			// do nothing
		}
	}
	
	public void set(ConfigEntry entry, Object newValue) throws FileNotFoundException, IOException {
		try (InputStreamReader is = new InputStreamReader(new FileInputStream(configFile), "UTF-8");
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");) {
			
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(is);
			
			configuration.set(ConfigEntry.GENUINE_LOGIN_SERVER.toString(), genuineLoginServer);
			configuration.set(ConfigEntry.PIRATED_LOGIN_SERVER.toString(), piratedLoginServer);
			configuration.set(ConfigEntry.PIRATED_ACCESSIBLE.toString(), piratedAccessible);
			configuration.set(ConfigEntry.PING_INTERVAL.toString(), pingInterval);
			
			configuration.set(entry.toString(), newValue);
			
			os.write("# =============================================================================== #\n");
			os.write("# 強烈建議使用指令 \"accountcheck set\" 來更動設定。\n");
			os.write("# It's highly recommended to use command \"accountcheck set\" to change settings.\n");
			os.write("# 不建議非進階使用者直接更動設定檔。\n");
			os.write("# It's NOT RECOMMENDED non advances user to change configuration file directly.\n");
			os.write("# =============================================================================== #\n");
			os.write('\n');
			os.flush();
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, os);
		}catch (UnsupportedEncodingException exception) {
			// do nothing
		}
	}
	
	public void find() {
		AccountCheck accountCheck = AccountCheck.getInstance();
		if (!accountCheck.getDataFolder().exists()) {
			accountCheck.getDataFolder().mkdir();
		}
		
		File file = new File(accountCheck.getDataFolder(), "config.yml");
		if (!file.exists()) {
			try {
				accountCheck.getLogManager().getLogger().info("找不到設定檔，預設設定檔配置中。。");
				Files.copy(accountCheck.getResourceAsStream("config.yml"), file.toPath());
				accountCheck.getLogManager().getLogger().info("預設設定檔配置完成！");
			} catch (IOException exception) {
				throw new RuntimeException("Can't create the config file! ", exception);
			}
		}
	}
	
	
}
