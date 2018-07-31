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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum LogType {
	PLAYER_CONNECT(1),	// LogArgs format String[] {name, ip}
	PIRATED_ACCOUNT_LOGIN(10),	// LogArgs format String[] {name, ip}
	GENUINE_ACCOUNT_LOGIN(11),	// LogArgs format String[] {name, ip}
	REJECT_LOGIN(12),	// LogArgs format String[] {reason}
	HTTP_ERROR(100),	// LogArgs format {null}
	SERVER_DOWN(101),	// LogArgs format String[] {reason}
	UNKNOWN_ERROR(-1);	// LogArgs format {null}
	
	@Getter
	private final int errorCode;
}
