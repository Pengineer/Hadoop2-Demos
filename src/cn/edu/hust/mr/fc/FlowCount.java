package cn.edu.hust.mr.fc;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import cn.edu.hust.tools.HadoopTools;

/**
 * 流量统计：Hadoop序列化机制
 * 
 * @author pengliang
 *
 */
public class FlowCount {
	public static class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
		FlowBean flowBean = new FlowBean();
		protected void map(LongWritable key, Text value, Context context) throws IOException ,InterruptedException {
			try {
				String line = value.toString();
				String[] fields = StringUtils.split(line, "\t");
				String phoneNbr = fields[1];
				long up_flow = Long.parseLong(fields[2]);
				long d_flow = Long.parseLong(fields[3]);
				flowBean.set(phoneNbr, up_flow, d_flow);
				context.write(new Text(phoneNbr), flowBean);
			} catch (Exception e) {
				// skip the unnormal data
			}
		}
	}
	
	public static class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
		FlowBean flowBean = new FlowBean();
		protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException ,InterruptedException {
			long sum_up_flow = 0;
			long sum_d_flow = 0;
			for(FlowBean bean : values) {
				sum_up_flow += bean.getUp_flow();
				sum_d_flow += bean.getD_flow();
			}
			flowBean.set(key.toString(), sum_up_flow, sum_d_flow);
			context.write(key, flowBean);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = HadoopTools.getClusterConf();
		
		Job job = Job.getInstance(conf, "flowJob");
		job.setJarByClass(FlowCount.class);
		
		job.setMapperClass(FlowCountMapper.class);
		job.setReducerClass(FlowCountReducer.class);
		
		/**
		 * 加入自定义分区：AreaPartition
		 */
		job.setPartitionerClass(AreaPartition.class);
		/**
		 * 设置reduce task的数量，要跟AreaPartition返回的partition个数匹配
		 * 如果Reduce task的数量比partition分组数多，就会产生多余的几个空文件
		 * 如果Reduce task的数量比partition分组数少，就会发生异常，因为有一些key没有对应reducetask接收
		 * 	（但是如果Reduce task的数量如果为1，那么就不存在partition分组问题，程序可正常运行）
		 */
		job.setNumReduceTasks(6);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
}
