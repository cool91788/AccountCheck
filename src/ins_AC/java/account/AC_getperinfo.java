package ins_AC.java.account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class AC_getperinfo{
 	
	public int gethttp(String http) {
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
