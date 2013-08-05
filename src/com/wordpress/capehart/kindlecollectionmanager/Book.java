package com.wordpress.capehart.kindlecollectionmanager;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores information about a book on an Amazon kindle.
 * 
 * @author Joshua Capehart
 * @version 0.1
 * @since 2013-08-04
 *
 */
public class Book {
	private String title;
	private String kindleID;
	private String path;
	private HashSet<KindleCollection> collections;
	
	public Book(String title, String kindleID) {
		this.title = title;
		this.kindleID = kindleID;
		path = "";
		collections = new HashSet<KindleCollection>();
	}

	public String getTitle() {
		return title;
	}

	public String getKindleID() {
		return kindleID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void addToCollection(KindleCollection collection) {
		collections.add(collection);
		if(!collection.hasBook(this)) {
			collection.addBook(this);
		}
	}
	
	public boolean isInCollection(KindleCollection collection) {
		return collections.contains(collection);
	}
	
	public Set<KindleCollection> getCollections() {
		return collections;
	}
}
