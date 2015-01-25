package ins_AC.java.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;

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
		switch(msgnum) {
		case 0:
			System.out.print(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.GREEN + "半正版驗證" + ChatColor.RESET + "] " 
					+ ChatColor.YELLOW + "盜版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 1:
			System.out.print(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.GREEN + "半正版驗證" + ChatColor.RESET + "] " 
					+ ChatColor.YELLOW + "正版玩家：" + name + ChatColor.GREEN + "，來自：" 
					+ ChatColor.RED + ip + ChatColor.YELLOW + "。登入！");
			break;
		case 2:
			System.out.print(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.GREEN + "半正版驗證" + ChatColor.RESET + "] " 
					+ ChatColor.GREEN + "盜版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 3:
			System.out.print(ChatColor.GREEN + "[" + ChatColor.YELLOW  +"訊息" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.GREEN + "半正版驗證" + ChatColor.RESET + "] " 
					+ ChatColor.GREEN + "正版玩家：" + name + "，來自：" 
					+ ChatColor.RED + ip  + ChatColor.GREEN + "。嘗試連結。");
			break;
		case 100:
			System.err.print(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.RED + "半正版驗證" + ChatColor.RESET + "]" 
					+ ChatColor.RED + " 發生錯誤代碼為\"100\"的錯誤！");
			break;
		case 101:
			System.err.print(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.RED + "半正版驗證" + ChatColor.RESET + "]" 
					+ ChatColor.RED + " 網頁開啟或關閉錯誤！");
			break;
		default:
			System.err.print(ChatColor.GREEN + "[" + ChatColor.RED  + "錯誤" 
					+ ChatColor.GREEN + "]" + ChatColor.RESET + " [" 
					+ ChatColor.RED + "半正版驗證" + ChatColor.RESET + "]" 
					+ ChatColor.RED + " 發生未知錯誤！");
		}
	}
}
