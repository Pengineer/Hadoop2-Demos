package cn.edu.hust.rpc.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Builder;
import org.apache.hadoop.ipc.Server;

/**
 * 将cn.edu.hust.rpc.server下的代码拷贝到服务器上运行192.168.88.5，报名中去掉server即可
 * 
 * @author pengliang
 *
 */

public class ServerStarter {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Builder builder = new RPC.Builder(conf);
		builder.setInstance(new LoginServiceImpl()).setBindAddress("192.168.88.5").setPort(10000).setProtocol(LoginServiceInterface.class);
		Server server = builder.build();
		server.start();
	}
}
