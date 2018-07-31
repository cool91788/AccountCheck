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

package install.java.accountcheck.account;

public enum AccountInfo {
	PIRATED_ACCOUNT,	//盜版帳號 
	PIRATED_ACCOUNT_CASE_INSENSITIVE,	//盜版帳號，不區分大小寫狀態下跟正版帳號拼寫相同
	GENUINE_ACCOUNT,	//正版帳號
	HTTP_ERROR,
	UNKNOWN_ERROR;
}
