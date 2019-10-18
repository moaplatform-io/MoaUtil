package moa.io.msg;

public interface ReceiveCallback {

	void processMsg(byte[] body,Exception e)throws Exception;
	void setSearchPacke(String searchPackage);
}
