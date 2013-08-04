package com.wordpress.capehart.kindlecollectionmanager;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores information about a collection on a Amazon kindle.
 * 
 * @author Joshua Capehart
 * @version 0.1
 * @since 2013-08-04
 *
 */
public class KindleCollection {
	private String title;
	private HashSet<Book> books;
	
	public KindleCollection(String title) {
		this.title = title;
		books = new HashSet<Book>();
	}

	public String getTitle() {
		return title;
	}
	
	public void addBook(Book book) {
		books.add(book);
		if(!book.isInCollection(this)) {
			book.addToCollection(this);
		}
	}
	
	public boolean hasBook(Book book) {
		return books.contains(book);
	}
	
	public Set<Book> getBooks() {
		return books;
	}
}
