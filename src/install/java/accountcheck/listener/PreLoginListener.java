package install.java.accountcheck.listener;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.accountinfo.GetAccountInfo;
import install.java.accountcheck.log.AccountCheckLog;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;

public class PreLoginListener {
	public void preLoginCheck(PreLoginEvent preloginevent, AccountCheckLog log) {
		String playername = preloginevent.getConnection().getName();
		String ip = preloginevent.getConnection().getAddress().toString();
		int prechecknumber = GetAccountInfo.getInfo(playername);
		switch(prechecknumber) {
		case 0:
			log.log(2, playername, ip);
			if(AccountCheck.getMainPluginObj().isEnablePiracy())
				preloginevent.getConnection().setOnlineMode(false);
			else {
				preloginevent.getConnection().setOnlineMode(true);
				log.log(4, playername, ip);
			}
			break;
		case 1:
			log.log(3, playername, ip);
			preloginevent.getConnection().setOnlineMode(true);
			break;
		case 100:
			log.log(100, playername, ip);
			preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：100"));
			break;
		case 101:
			log.log(101, playername, ip);
			preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "登入失敗！請稍後再嘗試。 錯誤代碼：101"));
			break;
		default:
			log.log(1000, playername, ip);
			preloginevent.getConnection().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "發生不明錯誤！"));
		}
	}
}
