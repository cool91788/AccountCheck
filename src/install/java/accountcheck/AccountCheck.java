/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2014-2018  Install
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

package install.java.accountcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import install.java.accountcheck.account.AccountManager;
import install.java.accountcheck.command.CommandManager;
import install.java.accountcheck.listener.ListenerManager;
import install.java.accountcheck.log.LogManager;
import install.java.accountcheck.yaml.Config;
import lombok.Getter;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class AccountCheck extends Plugin {
	
	@Getter
	private static AccountCheck instance;
	@Getter
	private LogManager logManager;
	@Getter
	private Config config;
	@Getter
	private AccountManager accountManager;
	@Getter
	private ListenerManager listenerManager;
	
	
	private ScheduledTask autoPingTask;
	
	public AccountCheck() {instance = this;}
	
	@Override
	public void onEnable() {
		//自訂log格式
		logManager = new LogManager(this);
		
		//第一次使用訊息
		firstTimeToUse();
		
		//檢查與讀取設定檔
		config = new Config(new File(getDataFolder(), "config.yml"));
		config.find();
		try {
			config.load();
		} catch (FileNotFoundException exception) {
			throw new RuntimeException("Load config fail!", exception);
		}
		
		accountManager = new AccountManager();
		listenerManager = new ListenerManager(this);
		
		listenerManager.registerAll();
		getProxy().getPluginManager().registerCommand(this, new CommandManager());
		
		createPingTask(config.getPingInterval());
		
		logManager.getLogger().info(ChatColor.GOLD + ChatColor.BOLD.toString() + "AccountCheck <半正版驗證> " +
				ChatColor.RED + ChatColor.BOLD.toString() + getDescription().getVersion());
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
	
	public void updatePingTask(int interval) {
		cancelPingTask();
		createPingTask(interval);
	}
	
	private void createPingTask(int interval) {
		cancelPingTask();
		ServerInfo server = ProxyServer.getInstance().getServers().get(config.getPiratedLoginServer());
		if(server == null) {
			config.setPiratedLoginServerOffline(true);
			config.setForceOnlineMode(true);
			return;
		}
		autoPingTask = ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			
			@Override
			public void run() {
				server.ping(new Callback<ServerPing>() {
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