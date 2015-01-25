package ins_AC.java.account;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class AC_checkListener implements Listener {
	
	private static Plugin mainPluginObj;
	
	AC_checkListener(Plugin mainPluginObj) {
		AC_checkListener.mainPluginObj = mainPluginObj;
	}
	
	@EventHandler
	public void 正盜版分流(PostLoginEvent loginevent) {
		mainPluginObj.getProxy().getScheduler().runAsync(mainPluginObj, 
				new AC_check_Thread(loginevent));
		try {Thread.sleep(6000);} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

	@EventHandler
	public void 選擇onlinemode(PreLoginEvent loginevent) {
		mainPluginObj.getProxy().getScheduler().runAsync(mainPluginObj, 
				new AC_check_Thread(loginevent));
		try {Thread.sleep(6000);}
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
}