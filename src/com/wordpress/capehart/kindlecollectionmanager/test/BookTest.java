package com.wordpress.capehart.kindlecollectionmanager.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wordpress.capehart.kindlecollectionmanager.Book;
import com.wordpress.capehart.kindlecollectionmanager.KindleCollection;

public class BookTest {

	@Test
	public void testAddToCollection() {
		KindleCollection collection = new KindleCollection("foo");
		Book book = new Book("To kill a mockingbird", "foo");
		
		book.addToCollection(collection);
		assertTrue("Book did not add collection.", book.isInCollection(collection));
		assertTrue("Book was not added to collection", collection.hasBook(book));
	}

}
