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
	public static int getInfo(String playername) {
		try {
			Process process = Runtime.getRuntime().exec(AccountCheck.getMainPluginObj().getExecuteFolder() 
					+ "https://api.mojang.com/users/profiles/minecraft/" + playername);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String errMsg, msg;
			int returnValue = -1;
			if((errMsg = brErr.readLine()) != null) {
				do {
					AccountCheck.getMainPluginObj().getLogger().warning(ChatColor.GREEN + "[" + ChatColor.RED  + "網頁查詢錯誤" 
							+ ChatColor.GREEN + "] " + ChatColor.RESET + errMsg);
				}while((errMsg = brErr.readLine()) != null);
				returnValue = 100;
			}else {
				if((msg = br.readLine()) == null) {
					returnValue = 0;
				}else {
					StringTokenizer st = new StringTokenizer(msg, "{},:");
					while(st.hasMoreTokens()) {
						if(st.nextToken().equals("\"name\""))
							returnValue = st.nextToken().replaceAll("\"", "").equals(playername) ? 1 : 0;
					}
				}
			}
			br.close();
			brErr.close();
			process.waitFor(); 
			return returnValue;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return 1289616;
		}
	}
}
