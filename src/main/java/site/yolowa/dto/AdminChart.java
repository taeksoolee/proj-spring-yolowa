package site.yolowa.dto;

// reservation ���̺��� ���� �ŷ����� ���̺� ����� ���� dto
public class AdminChart {
	private String month;
	private int count;
	private int tatalPriceSum;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTatalPriceSum() {
		return tatalPriceSum;
	}
	public void setTatalPriceSum(int tatalPriceSum) {
		this.tatalPriceSum = tatalPriceSum;
	}
	
	@Override
	public String toString() {
		return "AdminChart [month=" + month + ", count=" + count + ", tatalPriceSum=" + tatalPriceSum + "]";
	}
}
