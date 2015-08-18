package com.emptypockets.spacemania.engine.systems.spatialpartition;

public class PartitionKey {
	int xS = 0;
	int xE = 0;
	int yS = 0;
	int yE = 0;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof PartitionKey) {
			PartitionKey key = (PartitionKey) obj;
			if (key.xS != xS) {
				return false;
			}
			if (key.xE != xE) {
				return false;
			}
			if (key.yS != yS) {
				return false;
			}
			if (key.yE != yE) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean isSet(){
		return xS >= 0 && yS >= 0 && xE >= 0 && yE >=0;
	}
	public void reset() {
		xS = -1;
		yS = -1;
		xE = -1;
		yE = -1;
	}

	@Override
	public String toString() {
		return "[" + xS + "," + xE + "] - [" + yS + "," + yE + "]";
	}

	public void set(PartitionKey key) {
		xE = key.xE;
		xS = key.xS;
		yS = key.yS;
		yE = key.yE;
	}
}
