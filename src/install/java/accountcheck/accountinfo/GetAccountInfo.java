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

package install.java.accountcheck.accountinfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import install.java.accountcheck.AccountCheck;
import net.md_5.bungee.api.ChatColor;

public class GetAccountInfo{
 	
	void getHttp(String http) {
		try {
			HttpsURLConnection url = (HttpsURLConnection)new URL(http).openConnection();
			url.setConnectTimeout(2000);
			url.setReadTimeout(2000);
			BufferedReader bf = new BufferedReader(new InputStreamReader(url.getInputStream(), "UTF-8"));
			if(url.getResponseCode()==204) {
				// do nothing...
			}else {
				String message = bf.readLine();
				if(message != null)
					System.out.println(message);
			}
			((HttpsURLConnection)url).disconnect();
			bf.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	public static AccountInfo getInfo(String playername) {
		try {
			Process process = Runtime.getRuntime().exec(AccountCheck.getMainPluginObj().getExecuteFolder() 
					+ "https://api.mojang.com/users/profiles/minecraft/" + playername);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String errMsg, msg;
			AccountInfo returnValue = AccountInfo.UNKNOWN_ERROR;
			if((errMsg = brErr.readLine()) != null) {
				do {
					AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "網頁查詢錯誤" 
							+ ChatColor.GREEN + "] " + ChatColor.RESET + errMsg);
				}while((errMsg = brErr.readLine()) != null);
				returnValue = AccountInfo.HTTP_ERROR;
			}else {
				if((msg = br.readLine()) == null) {
					returnValue = AccountInfo.PIRATED_ACCOUNT;
				}else {
					StringTokenizer st = new StringTokenizer(msg, "{},:");
					while(st.hasMoreTokens()) {
						if(st.nextToken().equals("\"name\"")) {
							String returnedName = st.nextToken().replaceAll("\"", "");
							returnValue = returnedName.toLowerCase().equals(playername.toLowerCase()) ? 
									AccountInfo.PIRATED_ACCOUNT_CASE_INSENSITIVE : AccountInfo.PIRATED_ACCOUNT;
							if(returnedName.equals(playername))
								returnValue = AccountInfo.GENUINE_ACCOUNT;
						}
					}
				}
			}
			br.close();
			brErr.close();
			process.waitFor(); 
			return returnValue;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return AccountInfo.UNKNOWN_ERROR;
		}
	}
}
