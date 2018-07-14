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

public enum LogType {
	PIRATED_ACCOUNT_LOGIN(0),
	PIRATED_ACCOUNT_CONNECT(1),
	GENUINE_ACCOUNT_LOGIN(2),
	GENUINE_ACCOUNT_CONNECT(3),
	REJECT_PIRATED_ACCOUNT_LOGIN(4),
	HTTP_ERROR(100),
	UNKNOWN_ERROR(-1);
	private int errorCode;
	public int getErrorCode() {return errorCode;}
	LogType(int errorCode) {this.errorCode = errorCode;}
}
