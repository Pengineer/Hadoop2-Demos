package cn.edu.hust.mr.fc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * Writable ： 实现序列化
 * Comparable： 实现排序
 * @author pengliang
 *
 */
public class FlowBean implements WritableComparable<FlowBean> {
	
	private String phoneNbr;
	private long up_flow;
	private long d_flow;
	private long sum_flow;

	public void set(String phoneNbr, long up_flow, long d_flow) {
		this.phoneNbr = phoneNbr;
		this.up_flow = up_flow;
		this.d_flow = d_flow;
		this.sum_flow = up_flow + d_flow;
	}

	public String getPhoneNbr() {
		return phoneNbr;
	}

	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public long getUp_flow() {
		return up_flow;
	}

	public void setUp_flow(long up_flow) {
		this.up_flow = up_flow;
	}

	public long getD_flow() {
		return d_flow;
	}

	public void setD_flow(long d_flow) {
		this.d_flow = d_flow;
	}
	
	public long getSum_flow() {
		return sum_flow;
	}

	public void setSum_flow(long sum_flow) {
		this.sum_flow = sum_flow;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.phoneNbr);
		out.writeLong(this.up_flow);
		out.writeLong(this.d_flow);
		out.writeLong(this.sum_flow);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.phoneNbr = in.readUTF();
		this.up_flow = in.readLong();
		this.d_flow = in.readLong();
		this.sum_flow = in.readLong();
	}

	@Override
	public String toString() {
		return up_flow + "\t" + d_flow + "\t" + sum_flow;
	}

	@Override
	public int compareTo(FlowBean o) {
		return sum_flow > o.getSum_flow() ? -1 : 1;
	}
	
	

}
