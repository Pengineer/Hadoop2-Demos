package cn.edu.hust.mr.fc;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
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
 * 具有排序功能的流量统计
 * 输入为FlowCount的输出
 * 
 * @author pengliang
 *
 */
public class FlowCountSort {
	public static class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable> {
		FlowBean flowBean = new FlowBean();
		protected void map(LongWritable key, Text value, Context context) throws IOException ,InterruptedException {
			try {
				String line = value.toString();
				String[] fields = StringUtils.split(line, " ");
				String phoneNbr = fields[1];
				long up_flow = Long.parseLong(fields[2]);
				long d_flow = Long.parseLong(fields[3]);
				flowBean.set(phoneNbr, up_flow, d_flow);
				context.write(flowBean, NullWritable.get());
			} catch (Exception e) {
				// skip the unnormal data
			}
		}
	}
	
	public static class FlowCountSortReducer extends Reducer<FlowBean, NullWritable, Text, FlowBean> {
		protected void reduce(FlowBean bean, Iterable<NullWritable> values, Context context) throws IOException ,InterruptedException {
			context.write(new Text(bean.getPhoneNbr()), bean);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = HadoopTools.getClusterConf();
		conf.set("mapreduce.job.jar", "fc.jar");
		
		Job job = Job.getInstance(conf, "flowJob");
		job.setJarByClass(FlowCountSort.class);
		
		job.setMapperClass(FlowCountSortMapper.class);
		job.setReducerClass(FlowCountSortReducer.class);
		
		job.setMapOutputKeyClass(FlowBean.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
}
