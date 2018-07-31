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

import java.util.StringTokenizer;
import java.util.UUID;

import install.java.accountcheck.http.Https;
import install.java.accountcheck.http.HttpResponse;

public class AccountManager{
	
	private String lastPlayerName = null;	// last player name.
	private UUID lastGetsUUID = null;	// last get's UUID from mojang.(always be genuine user's)
	private AccountInfo lastGetsInfo = null;	// last get's AccountInfo.
	
	public AccountInfo getInfo(String name, boolean useCache) {return getInfo(null, name, useCache);}

	public AccountInfo getInfo(UUID uuid, String name, boolean useCache) {
		// 當有UUID時，先檢查快取內部有無資料。
		if(useCache) {
			if(uuid != null && lastGetsInfo != null) {
				//	優先以UUID識別，失敗再以name識別。
				if(uuid.equals(lastGetsUUID))
					return lastGetsInfo;
				else if(name.equals(lastPlayerName))
					return lastGetsInfo;
			}
			lastGetsInfo = null;
			lastGetsUUID = null;
			lastPlayerName = null;
		}
		
		// 向官方查詢
		HttpResponse response = new Https("https://api.mojang.com/users/profiles/minecraft/" + name).getInfo();
		if(response.isError())
			return AccountInfo.HTTP_ERROR;
		
		AccountInfo resault = AccountInfo.UNKNOWN_ERROR;
		
		if(response.getResponse() == null) {
			resault = AccountInfo.PIRATED_ACCOUNT;
			// 把盜版帳號資訊存入快取
			if(useCache) {
				lastGetsInfo = resault;
				lastGetsUUID = null;	// won't save uuid if player is in offline mode.
				lastPlayerName = name;
			}
			return resault;
		}
		
		StringTokenizer st = new StringTokenizer(response.getResponse(), "{},:");
		String getsName = "";
		UUID getsUUID = null;
		while(st.hasMoreTokens()) {
			String value = st.nextToken().replaceAll("\"", "");
			if(value.equals("id")) {
				getsUUID = UUID.fromString(st.nextToken().replaceAll("\"", "").replaceFirst(
						"^(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)$"
						, "$1-$2-$3-$4-$5"));
				continue;
			}else if(value.equals("name")) {
				getsName = st.nextToken().replaceAll("\"", "");
				break;
			}
		}
		resault = getsName.equalsIgnoreCase(name) ? 
				AccountInfo.PIRATED_ACCOUNT_CASE_INSENSITIVE : AccountInfo.PIRATED_ACCOUNT;
		if(getsName.equals(name)) {
			resault = AccountInfo.GENUINE_ACCOUNT;
			// 把正版帳號存入快取
			if(useCache) {
				lastGetsInfo = resault;
				lastGetsUUID = getsUUID;
				lastPlayerName = name;
			}
		}else {
			// 把盜版帳號資訊存入快取
			if(useCache) {
				lastGetsInfo = resault;
				lastGetsUUID = null;	// won't save uuid if player is in offline mode.
				lastPlayerName = name;
			}
		}
		return resault;
	}
}
