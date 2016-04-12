package install.java.accountcheck;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

class AccountCheckLogger extends Logger {
	
	private final String pluginName;
	
	protected AccountCheckLogger(Plugin plugin) {
		super(plugin.getClass().getCanonicalName(), null);
		   pluginName = ChatColor.GOLD + "[" + ChatColor.GREEN + plugin.getDescription().getName() + ChatColor.GOLD + "] ";
	       setParent(plugin.getProxy().getLogger());
	}

	@Override
	public void log(LogRecord logRecord) {
	    logRecord.setMessage(pluginName + logRecord.getMessage());
	    super.log(logRecord);
	}
}
