package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import util.ImportFile;
import util.ImportFile.Type;

public class ImportTest {

	@Test
	public void test() {
		ImportFile importfile1 = new ImportFile("res/sample.txt",Type.PAIRFILE);
		try {
			assertTrue(ImportFile.readFile());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException bei Einlesen von Dateien");
		}
	}

}
