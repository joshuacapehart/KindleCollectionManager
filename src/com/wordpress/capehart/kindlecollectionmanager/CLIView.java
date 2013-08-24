package com.wordpress.capehart.kindlecollectionmanager;

public class CLIView {
	public static void main(String[] args) {
		CollectionManager manager = new CollectionManager("D:\\");
		
		for(String collection : manager.getBooks()) {
			System.out.println(collection);
		}
		
		for(String book : manager.getBooksInCollection("make")) {
			System.out.println(book);
		}
	}
}
