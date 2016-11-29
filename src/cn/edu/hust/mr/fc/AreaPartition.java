package cn.edu.hust.mr.fc;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义分区
 * 
 * @author pengliang
 *
 * @param <KEY>
 * @param <VALUE>
 */
public class AreaPartition<KEY, VALUE> extends Partitioner<KEY, VALUE>{

	static Map<String, Integer> paraMap = new HashMap<String, Integer>();
	static {
		paraMap.put("131", 0);
		paraMap.put("132", 1);
		paraMap.put("133", 2);
		paraMap.put("134", 3);
		paraMap.put("135", 4);
	}
	
	@Override
	public int getPartition(KEY key, VALUE value, int numPartitions) {
		Integer result = paraMap.get(key.toString().substring(0,3));
		// 分6组
		return result == null ? 5 : result;
	}

}
