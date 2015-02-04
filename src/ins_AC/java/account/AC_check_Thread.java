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

package ins_AC.java.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;

public class AC_check_Thread implements Runnable{
	
	private PreLoginEvent preloginevent;
	private PostLoginEvent postloginevent;
	private int waynumber = 0;
	
	AC_check_Thread(PostLoginEvent loginevent) {
		postloginevent = loginevent;
		waynumber=1;
	}
	
	AC_check_Thread(PreLoginEvent loginevent) {
		preloginevent = loginevent;
		waynumber=2;
	}

	public int getinfo(String url) {
		try {
			Process process = Runtime.getRuntime().exec(AC_main.executefolder + url);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String getoutput = br.readLine();
			
			try {process.waitFor();}
			catch (InterruptedException e) {e.printStackTrace();}
			 
			return Integer.parseInt(getoutput);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			return 1289616;
	}

	public void run() {
		switch(waynumber) {
		case 1:
			postLoginCheck();
			break;
		case 2:
			preLoginCheck();
			break;
		default:
			log(1000, "", "");
		}
	}

	public void postLoginCheck() {
		//AC_getperinfo postgethttp = new AC_getperinfo();
		String postname = postloginevent.getPlayer().getName();
		String ip = postloginevent.getPlayer().getAddress().toString();
		int postchecknumber = getinfo("https://minecraft.net/haspaid.jsp?user=" + postname);
		//postgethttp.gethttp("https://minecraft.net/haspaid.jsp?user=" + postname);
		switch(postchecknumber) {
		case 0:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers().get(AC_main.盜版登入));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log(0, postname, ip);
			break;
		case 1:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers().get(AC_main.正版登入));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log(1, postname, ip);
			postloginevent.getPlayer().sendMessage(ChatColor.GREEN + "歡迎正版玩家登入！");
			break;
		case 100:
			log(100, postname, ip);
			postloginevent.getPlayer().disconnect(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：100");
			break;
		case 101:
			log(101, postname, ip);
			postloginevent.getPlayer().disconnect(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：101");
			break;
		default:
			log(1000, postname, ip);
			postloginevent.getPlayer().disconnect(ChatColor.RED + "發生不明錯誤！");
		}
	}

	public void preLoginCheck() {
		String prename = preloginevent.getConnection().getName();
		String ip = preloginevent.getConnection().getAddress().toString();
		//AC_getperinfo pregethttp = new AC_getperinfo();
		int prechecknumber = getinfo("https://minecraft.net/haspaid.jsp?user=" + prename);
		//pregethttp.gethttp("https://minecraft.net/haspaid.jsp?user=" + prename);
		switch(prechecknumber) {
		case 0:
			log(2, prename, ip);
			preloginevent.getConnection().setOnlineMode(false);
			break;
		case 1:
			log(3, prename, ip);
			preloginevent.getConnection().setOnlineMode(true);
			break;
		case 100:
			log(100, prename, ip);
			preloginevent.getConnection().disconnect(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：100");
			break;
		case 101:
			log(101, prename, ip);
			preloginevent.getConnection().disconnect(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：101");
			break;
		default:
			log(1000, prename, ip);
			preloginevent.getConnection().disconnect(ChatColor.RED + "發生不明錯誤！");
		}
	}

	public void log(int msgnum, String name, String ip) {
		ip = ip.substring(1, ip.indexOf(':'));
		switch(msgnum) {
		case 0:
			AC_main.mainPluginObj.getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "盜版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 1:
			AC_main.mainPluginObj.getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + ChatColor.YELLOW + "正版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 2:
			AC_main.mainPluginObj.getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + "盜版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 3:
			AC_main.mainPluginObj.getLogger().info(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "] " + "正版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 100:
			AC_main.mainPluginObj.getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " 發生錯誤代碼為\"100\"的錯誤！");
			break;
		case 101:
			AC_main.mainPluginObj.getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " 網頁開啟或關閉錯誤！");
			break;
		default:
			AC_main.mainPluginObj.getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "] " + ChatColor.RED + " 發生未知錯誤！");
		}
	}
}
