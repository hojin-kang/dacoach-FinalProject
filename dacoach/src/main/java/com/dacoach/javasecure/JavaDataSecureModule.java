package com.dacoach.javasecure;

import java.security.*;

public class JavaDataSecureModule {
	public static String getSHA256(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			byte bytes[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte temp : bytes) {
				sb.append(String.format("%02x", temp));
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
