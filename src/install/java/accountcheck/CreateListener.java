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

import install.java.accountcheck.listener.PostLoginListener;
import install.java.accountcheck.listener.PreLoginListener;
import install.java.accountcheck.log.AccountCheckLog;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CreateListener implements Listener {
	
	private AccountCheckLog accountCheckLog = new AccountCheckLog();
	
	@EventHandler
	public void chooseServer(PostLoginEvent loginevent) {
		new PostLoginListener().postLoginCheck(loginevent, accountCheckLog);
	}

	@EventHandler
	public void chooseOnlineMode(PreLoginEvent loginevent) {
		new PreLoginListener().preLoginCheck(loginevent, accountCheckLog);
	}
	
}