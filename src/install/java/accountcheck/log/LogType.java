package install.java.accountcheck.log;

public enum LogType {
	PIRATED_ACCOUNT_LOGIN(0),
	PIRATED_ACCOUNT_CONNECT(1),
	GENUINE_ACCOUNT_LOGIN(2),
	GENUINE_ACCOUNT_CONNECT(3),
	REJECT_PIRATED_ACCOUNT_LOGIN(4),
	HTTP_ERROR(100),
	UNKNOWN_ERROR(-1);
	private int errorCode;
	public int getErrorCode() {return errorCode;}
	LogType(int errorCode) {this.errorCode = errorCode;}
}
