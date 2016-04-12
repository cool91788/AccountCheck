package install.java.accountcheck.listener;

import install.java.accountcheck.AccountCheck;
import install.java.accountcheck.accountinfo.GetAccountInfo;
import install.java.accountcheck.log.AccountCheckLog;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;

public class PostLoginListener {
	public void postLoginCheck(PostLoginEvent postloginevent, AccountCheckLog log) {
		String playername = postloginevent.getPlayer().getName();
		String ip = postloginevent.getPlayer().getAddress().toString();
		int postchecknumber = GetAccountInfo.getInfo(playername);
		switch(postchecknumber) {
		case 0:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers()
					.get(AccountCheck.getMainPluginObj().getPiracyLoginServer()));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log.log(0, playername, ip);
			break;
		case 1:
			postloginevent.getPlayer().setReconnectServer((ServerInfo) ProxyServer.getInstance().getServers()
					.get(AccountCheck.getMainPluginObj().getGenuineLoginServer()));
			ProxyServer.getInstance().getReconnectHandler().setServer(postloginevent.getPlayer());
			log.log(1, playername, ip);
			postloginevent.getPlayer().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ChatColor.GREEN + "�w�勵�����a�n�J�I"));
			break;
		case 100:
			log.log(100, playername, ip);
			postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "�n�J���ѡI�еy��A���աC ���~�N�X�G100"));
			break;
		case 101:
			log.log(101, playername, ip);
			postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "�n�J���ѡI�еy��A���աC ���~�N�X�G101"));
			break;
		default:
			log.log(1000, playername, ip);
			postloginevent.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "�o�ͤ������~�I"));
		}
	}
}
