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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import install.java.accountcheck.AccountCheck;

public class GetAccountInfo{
 	
	int getHttp(String http) {
		try {
			URLConnection url = new URL(http).openConnection();
			url.setConnectTimeout(2000);
			url.setReadTimeout(2000);
			BufferedReader bf = new BufferedReader(new InputStreamReader(url.getInputStream(), "UTF-8"));
			String message = bf.readLine();
			((HttpsURLConnection)url).disconnect();
			bf.close();
			
			if(message.equals("false")) {
				return 0;
			} else if(message.equals("true")) {
				return 1;
			} else {
				return 100;
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return 101;
		}
	}
	public static int getInfo(String playername) {
		try {
			Process process = Runtime.getRuntime().exec(AccountCheck.getMainPluginObj().getExecuteFolder() 
					+ "https://minecraft.net/haspaid.jsp?user=" + playername);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String getoutput = br.readLine();
			br.close();
			process.waitFor(); 
			return Integer.parseInt(getoutput);
			
		} catch (IOException e1) {e1.printStackTrace();
		} catch (InterruptedException e) {e.printStackTrace();}
			return 1289616;
	}
}
