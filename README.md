此插件目前只提供繁體中文界面。   
This plugin is only provide Chinese(Traditional) interface now.   
   
原始碼可以在 [GitHub](https://github.com/cool91788/AccountCheck) 取得   
Source code is currently available on [GitHub](https://github.com/cool91788/AccountCheck).   

- - -

此插件為Install所撰寫，英文名稱為"AccountCheck"，中文名稱為「半正版驗證插件」。   
讓正版和盜版玩家用不同的方式登入伺服器。   
本程式受到GPLv3協議保障，如有任何疑問請洽cool9035963@gmail.com   
   
This plugin is write by Install , it call "AccountCheck". Chinese name is "半正版驗證插件".   
It make the online-mode/offline-mode players use different way to login the server.   
This program use GPLv3 , if any problem please mail to cool9035963@gmail.com   
   
My English is very poor :(   
If want mail to me , Chinese is better  :P   

- - -
   
   
# Todo   
- 當config file欄位有更動時，自動補足或是把舊有的備份起來用新的代替
- config file設計盡可能考慮保持向下相容（或許不向下相容）
- 使用資料庫來達成快速搜索，但必須考慮官方的更名系統。（可選選項）
- 考慮新增proxy server清單選擇流量方向，不再單一限制兩個
- <構想>新增管制內部跳proxy server的正盜版驗證（基於玩家ID，因登錄時已透過online mode篩選正版ID）
   
   
# How to build  如何編譯   
   
1. Need library : Bungeecord   
   需要程式庫：Bungeecord   

2. Choose the main class to install.java.accountcheck.accountinfo.ExecuteMain   
   設定 Main class 為 install.java.accountcheck.accountinfo.ExecuteMain   

3. Output the jar file named "AccountCheck.jar"   
   輸出 jar 檔，名稱「AccountCheck.jar」   
