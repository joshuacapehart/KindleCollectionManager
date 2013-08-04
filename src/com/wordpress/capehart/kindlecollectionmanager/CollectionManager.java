package com.wordpress.capehart.kindlecollectionmanager;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Controller for managing the collections on an Amazon kindle.
 * 
 * @author Joshua Capehart
 * @version 0.1
 * @since 2013-08-04 
 *
 */
public class CollectionManager {
	// The root directory of the Kindle.
	private String kindleRoot;
	// Hash from kindleID to book.
	private HashMap<String, Book> books;
	// Hash from collection name to collection.
	private HashMap<String, KindleCollection> collections;
	
	public CollectionManager(String kindleRoot) {
		this.kindleRoot = kindleRoot;
		books = new HashMap<String, Book>();
		collections = new HashMap<String, KindleCollection>();
	}
	
	// Builds the books list from the Kindle directories.
	private void buildBookList() {
		
	}
	
	// Builds the collections from the Kindle collections file.
	private void buildCollections() {
		
	}
}
