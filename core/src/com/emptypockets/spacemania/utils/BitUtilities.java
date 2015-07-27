package com.emptypockets.spacemania.utils;

public class BitUtilities {

	
	public static String toString(int bitMask){
		return String.format("%32s", Integer.toBinaryString(bitMask)).replace(" ", "0");
	}
	
}
