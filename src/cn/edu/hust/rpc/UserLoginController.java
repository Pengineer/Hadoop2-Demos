package cn.edu.hust.rpc;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

/**
 * Hadoop RPC 实现：客户端
 * 所需jar包：hadoop-common-2.7.2.jar
 * 
 * RPC远程过程调用的过程：
 * 1，客户端通过RPC工具，获取接口的代理对象（动态代理）
 * 2，客户端通过代理对象调用接口中的方法（显然这个方法的方法体是空的）
 * 3，客户端的socket拦截上述请求，将该请求发送到代理对象封装好的服务主机
 * 4，服务端监听到该请求，交给对应的服务处理程序，服务处理程序根据请求生成接口实现类的代理对象
 * 5，服务端的实现类的代理对象（反射）执行具体的业务方法，获取结果并通过服务端的socket请求返回给客户端
 * 6，客户端获取返回结构，整个远程调用过程结束。
 * 
 * RPC涉及到的技术：动态代理、反射、socket。
 * 
 * @author pengliang
 *
 */
public class UserLoginController {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		LoginServiceInterface proxy = RPC.getProxy(LoginServiceInterface.class, LoginServiceInterface.versionID, new InetSocketAddress("192.168.88.5", 10000), conf);
		String res = proxy.Login("xingyu", "alibaba");
		System.out.println(res);
	}
}
