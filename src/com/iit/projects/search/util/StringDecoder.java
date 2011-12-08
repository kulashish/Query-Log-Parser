package com.iit.projects.search.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class StringDecoder {
	public static String decode(String in) throws UnsupportedEncodingException {
		String out = null;
		out = URLDecoder.decode(in, "UTF-8");
		return out;
	}
}
