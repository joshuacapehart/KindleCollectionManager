package com.wordpress.capehart.kindlecollectionmanager.test;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import com.wordpress.capehart.kindlecollectionmanager.KindleIDBuilder;

public class KindleIDBuilderTest {

	@Test
	public void testBuildKindleID() {
		assertEquals("*c18726cfd23ecf53c911600405befb4bba25659d", 
				KindleIDBuilder.buildKindleID(Paths.get("./test"), "test.pdf"));
	}

}
