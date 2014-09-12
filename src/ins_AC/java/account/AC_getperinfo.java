package ins_AC.java.account;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.md_5.bungee.api.plugin.Plugin;

public class AC_getperinfo extends Plugin {

	public static String getIdData(String http) {

		URL url = null;
		URLConnection conn = null;
		InputStream input = null;
		InputStreamReader inputread = null;
		BufferedReader buffer = null;
		StringBuffer message = null;

		try {

			url = new URL(http);
			conn = url.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			input = conn.getInputStream();
			inputread = new InputStreamReader(input, "UTF-8");
			buffer = new BufferedReader(inputread);
			String tempstr = null;
			message = new StringBuffer();
			while ((tempstr = buffer.readLine()) != null) {
				message.append(tempstr);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			return ("Σ(ﾟДﾟ；≡；ﾟдﾟ)HTTP ERROR");
		} finally {
			try {
				url = null;
				if (input != null)
					input.close();
				if (inputread != null)
					inputread.close();
				if (buffer != null)
					buffer.close();
				conn = null;
				buffer = null;
				inputread = null;
				input = null;
			} catch (Exception exception) {
				exception.printStackTrace();
				return ("Σ(ﾟДﾟ；≡；ﾟдﾟ)HTTP ERROR AT CLOSE");
			}

		}
		if (message.toString() == null) {
			return ("Σ(ﾟДﾟ；≡；ﾟдﾟ)");
		} else {
			return message.toString();
		}
	}
}