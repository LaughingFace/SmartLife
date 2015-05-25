package com.laughingFace.microWash.deviceControler.model;

public class Progress {

	private long total;
	private long remain;
	private long maxDeviation;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getRemain() {
		return remain;
	}

	public void setRemain(long remain) {
		this.remain = remain;
	}

	public long getMaxDeviation() {
		return maxDeviation;
	}

	public void setMaxDeviation(long maxDeviation) {
		this.maxDeviation = maxDeviation;
	}
}
