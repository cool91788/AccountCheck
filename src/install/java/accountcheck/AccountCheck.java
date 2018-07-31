/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) (2014-2018)  Install
 *
 *   This file is part of AccountCheck.
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

package install.java.accountcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import install.java.accountcheck.account.AccountManager;
import install.java.accountcheck.command.AccountCheckCommand;
import install.java.accountcheck.listener.PostLoginListener;
import install.java.accountcheck.listener.PreLoginListener;
import install.java.accountcheck.log.AccountCheckLogManager;
import install.java.accountcheck.yaml.Config;
import lombok.Getter;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class AccountCheck extends Plugin {
	
	@Getter
	private static AccountCheck instance;
	@Getter
	private AccountCheckLogManager logManager;
	@Getter
	private Config config;
	@Getter
	private AccountManager accountManager;
	private ScheduledTask autoPingTask;
	
	public AccountCheck() {instance = this;}
	
	@Override
	public void onEnable() {
		//自訂log格式
		logManager = new AccountCheckLogManager(this);
		
		//第一次使用訊息
		firstTimeToUse();
		
		//檢查與讀取設定檔
		config = new Config(new File(getDataFolder(), "config.yml"));
		config.checkConfigFile();
		try {
			config.loadConfig();
		} catch (FileNotFoundException exception) {
			throw new RuntimeException("Load config fail!", exception);
		}
		
		accountManager = new AccountManager();
		
		getProxy().getPluginManager().registerListener(this, new PreLoginListener());
		getProxy().getPluginManager().registerListener(this, new PostLoginListener());
		getProxy().getPluginManager().registerCommand(this, new AccountCheckCommand());
		createPingTask(config.getPingInterval());
		logManager.getLogger().info(ChatColor.RED + "版本：" + getDescription().getVersion());
	}
	
	private void firstTimeToUse() {
		if (!getDataFolder().exists()) {
			logManager.getLogger().info(ChatColor.GOLD
									+ "\n<==========================================>\n"
									+ "此訊息是給第一次使用的使用者：\n"
									+ "強烈建議使用本插件的指令\"accountcheck set\"來設定。\n"
									+ "不建議非進階使用者直接更動設定檔。\n"
									+ "<==========================================>");
		}
	}
	
	private void cancelPingTask() {
		if(autoPingTask != null)
			autoPingTask.cancel();
	}
	
	public void modifyPingTask(int interval) {
		cancelPingTask();
		createPingTask(interval);
	}
	
	private void createPingTask(int interval) {
		autoPingTask = ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			
			@Override
			public void run() {
				ProxyServer.getInstance().getServers().get(config.getPiratedLoginServer()).ping(new Callback<ServerPing>() {
					@Override
					public void done(ServerPing resault, Throwable error) {
						config.setPiratedLoginServerOffline((error != null));
					}
				});
			}
			
		}, 1, interval, TimeUnit.SECONDS);
	}
	
	@Override
	public void onDisable() {
		cancelPingTask();
	}
}