package cn.edu.hust.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 本地模式运行MapReduce（MR在本地执行）
 * 
 * 输入输出文件可以指定为本地，也可以指定为HDFS
 * 
 * @author pengliang
 *
 */
public class WordCountRunnerLocal {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		
		// 设置job中的资源所在的Jar包
		job.setJarByClass(WordCountRunnerLocal.class);
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		// 方式一
		FileInputFormat.setInputPaths(job, "c:/wc/input");
		FileOutputFormat.setOutputPath(job, new Path("c:/wc/output"));
		
		// 方式二
		/*
		 FileInputFormat.setInputPaths(job, "hdfs://192.168.88.104:8020/test/input");
		 FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.88.104:8020/test/output"));
		 */
		
		boolean res = job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}
