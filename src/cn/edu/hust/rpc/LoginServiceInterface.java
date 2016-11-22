package cn.edu.hust.rpc;

public interface LoginServiceInterface {
	final static long versionID = 1L;
	public String Login(String username, String passwords);
}
