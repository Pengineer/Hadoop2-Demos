package cn.edu.hust.tools;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class HadoopTools {
	
	public static Configuration getConf() {
		Configuration conf = new Configuration();
		conf.addResource("conf/core-default.xml");conf.addResource("conf/core-site.xml");
		conf.addResource("conf/hdfs-default.xml");conf.addResource("conf/hdfs-site.xml");
		conf.addResource("conf/mapred-default.xml");conf.addResource("conf/mapred-site.xml");
		conf.addResource("conf/yarn-default.xml");conf.addResource("conf/yarn-site.xml");
		return conf;
	} 
	
	public static FileSystem getFileSystem() throws IOException {
		return FileSystem.get(HadoopTools.getConf());
	}
	
	public static FileSystem getFileSystem(Configuration conf) throws IOException {
		return FileSystem.get(conf);
	}
	
	public static FileSystem getFileSystem(URI uri, Configuration conf, String user) throws IOException, InterruptedException  {
		return FileSystem.get(uri, conf, user);
	}
}
