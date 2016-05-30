package com.aki.bustool.adapter;

import android.annotation.SuppressLint;

import com.amap.api.services.route.BusStep;

@SuppressLint("ParcelCreator")
public class SchemeBusStep extends BusStep {

	private boolean isWalk = false;
	private boolean isBus = false;
	private boolean isStart = false;
	private boolean isEnd = false;

	public SchemeBusStep(BusStep step) {
		if (step != null) {
			this.setBusLine(step.getBusLine());
			this.setWalk(step.getWalk());
		}
	}

	public boolean isWalk() {
		return isWalk;
	}

	public void setWalk(boolean isWalk) {
		this.isWalk = isWalk;
	}

	public boolean isBus() {
		return isBus;
	}

	public void setBus(boolean isBus) {
		this.isBus = isBus;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

}
