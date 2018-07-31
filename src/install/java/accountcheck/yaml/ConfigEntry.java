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

package install.java.accountcheck.yaml;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConfigEntry {
	GENUINE_LOGIN_SERVER("genuine-login-server"),
	PIRATED_LOGIN_SERVER("pirated-login-server"),
	PIRATED_ACCESSIBLE("pirated-accessible"),
	PING_INTERVAL("ping-interval");

	@Getter
	private final String entryName;
}
