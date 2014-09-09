package ins_AC.java.account;

import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class AC_check extends Plugin implements Listener {

	int checknumber = 2;
	
	private final static Logger logger = Logger.getLogger("AccountCheck");
	
	@EventHandler
	public void 選擇onlinemode(PreLoginEvent loginevent) {
		
		String httpcheck =  new String(AC_getperinfo.getIdData("https://minecraft.net/haspaid.jsp?user=" + loginevent.getConnection().getName()));
		while (httpcheck == null) {
			httpcheck = new String(AC_getperinfo.getIdData("https://minecraft.net/haspaid.jsp?user=" + loginevent.getConnection().getName()));
		}
		while(httpcheck.equals("") || httpcheck.contains("Σ(ﾟДﾟ；≡；ﾟдﾟ)")) {
            if (httpcheck.equals("Σ(ﾟДﾟ；≡；ﾟдﾟ)HTTP ERROR")) {
				logger.warning("[半正版驗證] 網頁開啟錯誤！");
				httpcheck = new String(AC_getperinfo.getIdData("https://minecraft.net/haspaid.jsp?user=" + loginevent.getConnection().getName()));
			} else if (httpcheck.equals("Σ(ﾟДﾟ；≡；ﾟдﾟ)HTTP ERROR AT CLOSE")) {
				logger.warning("[半正版驗證] 網頁關閉錯誤！");
				httpcheck = new String(AC_getperinfo.getIdData("https://minecraft.net/haspaid.jsp?user=" + loginevent.getConnection().getName()));
			} else {
				httpcheck = new String(AC_getperinfo.getIdData("https://minecraft.net/haspaid.jsp?user=" + loginevent.getConnection().getName()));
			}
		}
		if (httpcheck.equals("true")) {
			loginevent.getConnection().setOnlineMode(true);
			checknumber = 1;
		} else {
			loginevent.getConnection().setOnlineMode(false);
			checknumber = 0;
		}
		httpcheck = null;
	}
	

	@EventHandler
	public void 正盜版分流(PostLoginEvent loginevent) {

		if (checknumber == 1) {
			loginevent.getPlayer().setReconnectServer((ServerInfo)ProxyServer.getInstance().getServers().get(AC_main.正版登入));
			ProxyServer.getInstance().getReconnectHandler().setServer(loginevent.getPlayer());
			loginevent.getPlayer().sendMessage(ChatColor.GREEN + "歡迎正版玩家登入！");
			checknumber = 2;
		} else if (checknumber == 0){
			loginevent.getPlayer().setReconnectServer((ServerInfo)ProxyServer.getInstance().getServers().get(AC_main.盜版登入));
			ProxyServer.getInstance().getReconnectHandler().setServer(loginevent.getPlayer());
			checknumber = 2;
		} else {
			checknumber = 2;
		}
	}
}