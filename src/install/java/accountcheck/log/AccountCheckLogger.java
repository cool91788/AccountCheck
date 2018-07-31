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

package install.java.accountcheck.log;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

class AccountCheckLogger extends Logger {
	
	private final String pluginName;
	
	protected AccountCheckLogger(Plugin plugin) {
		super(plugin.getClass().getCanonicalName(), null);
		   pluginName = ChatColor.GREEN + "[" + ChatColor.GOLD +
				   plugin.getDescription().getName() + ChatColor.GREEN + "] " + ChatColor.RESET;
	       setParent(plugin.getProxy().getLogger());
	}

	@Override
	public void log(LogRecord logRecord) {
	    logRecord.setMessage(pluginName + logRecord.getMessage());
	    super.log(logRecord);
	}
}
