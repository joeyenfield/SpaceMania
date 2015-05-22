package com.emptypockets.spacemania.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtils {
	public static String getErrorMessage(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
