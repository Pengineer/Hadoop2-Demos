package cn.edu.hust.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountRunner {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.addResource("conf/core-default.xml");conf.addResource("conf/core-site.xml");
		conf.addResource("conf/hdfs-default.xml");conf.addResource("conf/hdfs-site.xml");
		conf.addResource("conf/mapred-default.xml");conf.addResource("conf/mapred-site.xml");
		conf.addResource("conf/yarn-default.xml");conf.addResource("conf/yarn-site.xml");
		
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
