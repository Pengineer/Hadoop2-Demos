package cn.edu.hust.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 集群模式运行MapReduce（MR在集群上运行）
 * 
 * 1，此模式中，mapred-site.xml文件的mapreduce.framework.name属性决定了MR是在本地还是在集群中运行
 * 	（1）值为local或者没有配置该属性：本地模式运行
 * 	（2）值为yarn：集群模式运行
 * 
 * 2，集群模式运行，需要指定客户端程序的jar包。
 * 
 * 3，集群模式运行时，注意权限问题，可以通过加运行参数解决：
 * -DHADOOP_USER_NAME=hadoop
 * 
 * 4，将job从Windows提交到集群，存在路径跨平台的问题（任务提交器会读取本地环境变量），解决方法：
 * 	 原因：http://www.aboutyun.com/thread-8498-1-1.html
 * 	 解决：修改mapred-site.xml文件：http://blog.csdn.net/ckfflyingdream/article/details/50112113
 * 
 * 5，执行时选择 Run As -> Run on Hadoop
 * 
 * @author pengliang
 *
 */
public class WordCountRunner {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.addResource("conf/core-default.xml");conf.addResource("conf/core-site.xml");
		conf.addResource("conf/hdfs-default.xml");conf.addResource("conf/hdfs-site.xml");
		conf.addResource("conf/mapred-default.xml");conf.addResource("conf/mapred-site.xml");
		conf.addResource("conf/yarn-default.xml");conf.addResource("conf/yarn-site.xml");
		
		// 分配资源时，NodeManager需要加载客户端程序jar包
		conf.set("mapreduce.job.jar", "wc.jar");
		
		Job job = Job.getInstance(conf);
		
		// 设置job中的资源所在的Jar包
		job.setJarByClass(WordCountRunner.class);
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		FileInputFormat.setInputPaths(job, "hdfs://csdc/test/input");
		FileOutputFormat.setOutputPath(job, new Path("hdfs://csdc/test/output"));
		
		boolean res = job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}
