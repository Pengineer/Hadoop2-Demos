package cn.edu.hust.mr.wc;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		/*
		 * input:
		 * 		key：输入行偏移量（简单理解为行号）
		 * 		value：行内容
		 * output：
		 * 		context.write(key, value)
		 */
		String line = value.toString();
		String[] words = StringUtils.split(line, ' ');
		for (String word : words) {
			context.write(new Text(word), new LongWritable(1));
		}
	}

}
