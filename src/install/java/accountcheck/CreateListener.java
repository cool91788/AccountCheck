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

package install.java.accountcheck;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CreateListener implements Listener {
	
	@EventHandler
	public void chooseServer(PostLoginEvent loginevent) {
		
		//嘗試建立一個新的執行緒來執行，並等待6秒鐘
		AccountCheck.mainPluginObj.getProxy().getScheduler().runAsync(AccountCheck.mainPluginObj, 
				new ListenerThread(loginevent));
		try {Thread.sleep(6000);}
		catch (InterruptedException e) {e.printStackTrace();}
	}

	@EventHandler
	public void chooseOnlineMode(PreLoginEvent loginevent) {
		
		//嘗試建立一個新的執行緒來執行，並等待6秒鐘
		AccountCheck.mainPluginObj.getProxy().getScheduler().runAsync(AccountCheck.mainPluginObj, 
				new ListenerThread(loginevent));
		try {Thread.sleep(6000);}
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
}