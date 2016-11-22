package cn.edu.hust.rpc.server;

public class LoginServiceImpl implements LoginServiceInterface {

	@Override
	public String Login(String username, String passwords) {
		return username + " " + passwords + " logged in successfully!";
	}

}
