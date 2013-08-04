package com.wordpress.capehart.kindlecollectionmanager;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KindleIDBuilder {
	private static final String HEXES = "0123456789abcdef";
	
	public static String buildKindleID(Path path, String filename) {
		String extension = filename.substring(filename.lastIndexOf('.'));
		String kindleID = "";
		switch(extension) {
		case ".pdf":
			kindleID = buildHash(filename);
			break;
		}
		return kindleID;
	}
	
	private static String buildHash(String filename) {
		String fullPath = "/mnt/us/documents/" + filename;
		StringBuilder hash = new StringBuilder("*");
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(fullPath.getBytes("UTF-8"));
			hash.append(getHex(crypt.digest()));
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return hash.toString();
	}
	
	/**
	 * Converts the given raw bytes to a hex string.
	 * 
	 * Kindly provided by http://rgagnon.com/javadetails/java-0596.html
	 * 
	 * @param raw An array of raw bytes to convert.
	 * @return A the given bytes as a hex String.
	 */
	private static String getHex( byte [] raw ) {
		if ( raw == null ) {
			return null;
		}
		final StringBuilder hex = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4))
			.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}
