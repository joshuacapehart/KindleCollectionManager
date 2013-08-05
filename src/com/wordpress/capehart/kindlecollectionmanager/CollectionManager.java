package com.wordpress.capehart.kindlecollectionmanager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
	private Path kindleRoot;
	// Hash from kindleID to book.
	private HashMap<String, Book> idToBook;
	// Hash from book filename to book.
	private HashMap<String, Book> filenameToBook;
	// Hash from collection name to collection.
	private HashMap<String, KindleCollection> collections;
	
	private static final Path documentsPath = Paths.get("documents");
	
	public CollectionManager(String kindleRoot) {
		this.kindleRoot = Paths.get(kindleRoot);
		idToBook = new HashMap<String, Book>();
		filenameToBook = new HashMap<String, Book>();
		collections = new HashMap<String, KindleCollection>();
		
		buildBookList();
	}
	
	/**
	 * Gets the Kindle ID of a book.
	 * 
	 * @param filename The filename of the book to get the ID of.
	 * @return The ID of the given book, or null if the book was not found. 
	 */
	public String getBookID(String filename) {
		Book book = filenameToBook.get(filename);
		String id = null;
		if(book != null) {
			id = book.getKindleID();
		}
		
		return id;
	}
	
	// Builds the books list from the Kindle directories.
	private void buildBookList() {
		try(DirectoryStream<Path> dir = Files.newDirectoryStream(kindleRoot.resolve(documentsPath))) {
			for(Path file : dir) {
				String id = KindleIDBuilder.buildKindleID(file);
				if(id != null) {
					String filename = file.getFileName().toString();
					Book book = new Book(filename, id);
					filenameToBook.put(filename, book);
					idToBook.put(id, book);
				}
			}
		} catch(IOException | DirectoryIteratorException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	// Builds the collections from the Kindle collections file.
	private void buildCollections() {
		
	}
}
