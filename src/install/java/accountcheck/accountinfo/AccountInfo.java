package install.java.accountcheck.accountinfo;

public enum AccountInfo {
	PIRATED_ACCOUNT,	//盜版帳號
	PIRATED_ACCOUNT_CASE_INSENSITIVE,	//盜版帳號，不區分大小寫狀態下跟正版帳號拼寫相同
	GENUINE_ACCOUNT,	//正版帳號
	HTTP_ERROR,
	UNKNOWN_ERROR;
}
