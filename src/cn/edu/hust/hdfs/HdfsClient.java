package cn.edu.hust.hdfs;

import java.io.FileInputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsClient {
	
	private static FileSystem fs = null;
	static {
		/**
		 * Hadoop Common中定义了Configuration的基本实现，其他模块/系统，比如MapReduce，HDFS，YARN，HBase等，均在这个基础实现基础上，
		 * 实现了自己的Configuration，比如MapReduce中叫JobConf，HDFS中叫HdfsConfiguration，YARN中交YarnConfiguration，
		 * HBase中叫HBaseConfiguration，这些Configuration实现非常简单，他们继承基类Configuration，然后加载自己的xml配置文件，通常
		 * 这些配置文件成对出现，比如MapReduce分别为mapred-default.xml和mapred-site.xml，HDFS分别为hdfs-default.xml，hdfs-site.xml。
		 * 
		 * Configuraion允许你通过两种方式设置key/value格式的属性，一种是通过set方法，另一种通过addResouce(String name)，将一个xml文件加载到
		 * Configuration中。而且前者的优先级高与后者。
		 * 
		 */
		Configuration conf = new Configuration();
		conf.addResource("conf/core-default.xml");conf.addResource("conf/core-site.xml");
		conf.addResource("conf/hdfs-default.xml");conf.addResource("conf/hdfs-site.xml");
		conf.addResource("conf/mapred-default.xml");conf.addResource("conf/mapred-site.xml");
		conf.addResource("conf/yarn-default.xml");conf.addResource("conf/yarn-site.xml");
		try {
			/**
			 * 根据core-site.xml中的fs.defaultFS获取文件系统。（如果是file:\\，则返回本地文件系统）
			 */
//			fs = FileSystem.get(conf);
			fs = FileSystem.get(new URI("hdfs://csdc"), conf, "hadoop"); // 指定Client端用户（HDFS的权限检查机制并不严格）
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
//		uploadToHDFS();
		downloadFromHDFS();
//		deleteFile();
//		deleteDir();
//		mkdirs();
//		rename();
		System.out.println("over!");
	}
	
	public static boolean uploadToHDFS() {
		try {
			Path destFile = new Path("hdfs://csdc/test/upload/test.doc");
			FSDataOutputStream os = fs.create(destFile);
			FileInputStream is = new FileInputStream("c:\\test.doc");
			IOUtils.copy(is, os);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean downloadFromHDFS() {
		try {
			Path srcFile = new Path("hdfs://csdc/test/upload/test.doc");
			Path destFile = new Path("c:\\downloadfromhdfs.doc");
			fs.copyToLocalFile(srcFile, destFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean mkdirs() {
		try {
			Path destPath = new Path("hdfs://csdc/test/aa/bb/cc");
			return fs.mkdirs(destPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteFile() {
		try {
			Path srcFile = new Path("hdfs://csdctest/upload/test.doc");
			return fs.delete(srcFile, false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteDir() {
		try {
			Path srcFile = new Path("hdfs://csdc/test/upload");
			return fs.delete(srcFile, true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean rename() {
		try {
			Path srcPath = new Path("hdfs://csdc/test/upload/test2.doc");
			Path destPath = new Path("hdfs://csdc/test/upload/test.doc");
			return fs.rename(srcPath, destPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
