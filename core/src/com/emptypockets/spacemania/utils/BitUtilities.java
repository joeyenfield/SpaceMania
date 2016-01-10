package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.utils.Bits;

public class BitUtilities {

	public static String toString(int bitMask) {
		return String.format("%32s", Integer.toBinaryString(bitMask)).replace(" ", "0");
	}

	public static String toString(Bits bit) {
		StringBuilder rst = new StringBuilder();
		for (int i = 0; i < bit.length(); i++) {
			if (bit.get(i)) {
				rst.append('1');
			} else {
				rst.append('0');
			}
		}
		return rst.toString();
	}
}
