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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private Map<String, Book> idToBook;
	// Hash from book filename to book.
	private Map<String, Book> filenameToBook;
	// Hash from collection name to collection.
	private Map<String, KindleCollection> collections;
	
	private static final Path DOCUMENTS_PATH = Paths.get("documents");
	private static final Path COLLECTIONS_PATH = Paths.get("system/collections.json");
	
	public CollectionManager(String kindleRoot) {
		this.kindleRoot = Paths.get(kindleRoot);
		idToBook = new HashMap<String, Book>();
		filenameToBook = new HashMap<String, Book>();
		collections = new HashMap<String, KindleCollection>();
		
		buildCollections();
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
	
	public Set<String> getCollections() {
		return collections.keySet();
	}
	public Set<String> getBooks() {
		return filenameToBook.keySet();
	}
	
	public ArrayList<String> getBooksInCollection(String collectionName) {
		ArrayList<String> books = null;
		KindleCollection collection = collections.get(collectionName);
		if(collection != null) {
			books = new ArrayList<String>(collection.getBooks().size());
			for(Book book : collection.getBooks()) {
				books.add(book.getTitle());
			}
		}
		
		return books;
	}
	
	// Builds the books list from the Kindle directories.
	private void buildBookList() {
		try(DirectoryStream<Path> dir = Files.newDirectoryStream(kindleRoot.resolve(DOCUMENTS_PATH))) {
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
			Logger.getInstance().log(LogLevel.FATAL_ERROR, e.toString());
		}
	}
	
	// Builds the collections from the Kindle collections file.
	private void buildCollections() {
		buildBookList();
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			// This is going to be fairly type unsafe, but we don't really have a choice.
			// Because the json collections file contains arbitrary names for the key-value pairs,
			// we can't use a json to Java Object mapping, and all Java json libraries are
			// type unsafe when dealing with things this way. Checking the type of collection
			// prevents a mismatch there at least.
			
			@SuppressWarnings("unchecked")
			Map<String, Object> json = mapper.readValue(kindleRoot.resolve(COLLECTIONS_PATH).toFile(), Map.class);
			
			for(Map.Entry<String, Object> entry : json.entrySet()) {
				String collectionName = entry.getKey().substring(0, entry.getKey().lastIndexOf("@"));
				KindleCollection collection = new KindleCollection(collectionName);
				collections.put(collectionName, collection);
				
				Map<String, Object> collections = null;
				if(entry.getValue() instanceof Map<?, ?>) {
					collections = ((Map<String, Object>) entry.getValue());
				}
				
				ArrayList<String> ids = null;
				if(collections != null) {
					ids = (ArrayList<String>) collections.get("items");
				}
				
				if(ids != null) {
					for(String id : ids) {
						Book book = idToBook.get(id);
						// If we can't find the book, it may be that the Kindle
						// still have the entry in the collections file, as well
						// as the .pdr file, but not the book file itself.
						// For this reason, we'll just ignore missing the book 
						// for now.
						if(book != null) {
							collection.addBook(book);
						} else {
							Logger.getInstance().log(LogLevel.ERROR, 
									"Book specified in collection not in directory with ID:",
									id);
						}
					}
				}
			}
		} catch (JsonParseException e) {
			Logger.getInstance().log(LogLevel.FATAL_ERROR, e.toString());
		} catch (JsonMappingException e) {	
			Logger.getInstance().log(LogLevel.FATAL_ERROR, "Could not parse collection file.", e.toString());
		} catch (IOException e) {
			Logger.getInstance().log(LogLevel.FATAL_ERROR, "Could not open collection file.", e.toString());
		}
	}
}
