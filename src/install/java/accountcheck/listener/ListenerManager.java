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

package install.java.accountcheck.listener;

import java.util.EnumMap;

import install.java.accountcheck.AccountCheck;
import net.md_5.bungee.api.plugin.Listener;

public class ListenerManager {
	
	private final EnumMap<ListenerType, Listener> listeners = new EnumMap<ListenerType, Listener>(ListenerType.class);
	private final AccountCheck plugin;
	
	public ListenerManager(AccountCheck plugin) {
		this.plugin = plugin;
		listeners.put(ListenerType.PRELOGIN, new PreLoginListener());
		listeners.put(ListenerType.POSTLOGIN, new PostLoginListener());
	}
	
	
	public void unregisterAll() {
		plugin.getProxy().getPluginManager().unregisterListeners(plugin);
	}
	
	public void registerAll() {
		for(Listener listener : listeners.values())
			plugin.getProxy().getPluginManager().registerListener(plugin, listener);
	}
	
	public void register(ListenerType listenerType) {
		plugin.getProxy().getPluginManager().registerListener(plugin, listeners.get(listenerType));
	}
	
	public void unregister(ListenerType listenerType) {
		plugin.getProxy().getPluginManager().unregisterListener(listeners.get(listenerType));
	}
}
