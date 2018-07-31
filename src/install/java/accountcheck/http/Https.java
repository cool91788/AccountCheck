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

package install.java.accountcheck.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Https {
	
	private final String url;
	
	public HttpResponse getInfo() {
			
			HttpsURLConnection https = null;
			BufferedReader bf = null;
			HttpResponse httpsResponse = null;
			
			try {
				https = (HttpsURLConnection)new URL(url).openConnection();
				https.setConnectTimeout(2000);
				https.setReadTimeout(2000);
				if(https.getResponseCode() == 204)
					httpsResponse = new HttpResponse(null, false);
				else {
					bf = new BufferedReader(new InputStreamReader(https.getInputStream(), "UTF-8"));
					String response = null;
					response = bf.readLine();
					httpsResponse = new HttpResponse(response, false);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				httpsResponse = new HttpResponse(null, true);
			}finally {
				if(bf != null) {
					try {
						bf.close();
					} catch (IOException e) {
						//do nothing
					}
				}
				if(https != null)
					https.disconnect();
	
			}
			return httpsResponse;
		}
}
