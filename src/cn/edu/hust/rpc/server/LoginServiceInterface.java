package cn.edu.hust.rpc.server;

public interface LoginServiceInterface {
	final static long versionID = 1L;
	public String Login(String username, String passwords);
}
