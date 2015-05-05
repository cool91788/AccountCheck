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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class GetAccountInfo{
 	
	public int getHttp(String http) {
		try {
			Object obj = new URL(http).openConnection();
			((URLConnection)obj).setConnectTimeout(2000);
			((URLConnection)obj).setReadTimeout(2000);
			Object obj2 = new BufferedReader(new InputStreamReader(
					((URLConnection)obj).getInputStream(), "UTF-8"));
			String message = ((BufferedReader)obj2).readLine();
			((HttpsURLConnection)obj).disconnect();
			((BufferedReader)obj2).close();
			
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
}
