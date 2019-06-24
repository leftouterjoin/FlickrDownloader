package flickrdownloader;

import java.io.File;

import org.junit.Test;

public class Test2 {
	private static final String PATH = System.getenv("PATH");
	private static final String F_API_KEY = System.getenv("F_API_KEY");

	@Test
	public void test1() {
		System.out.println(PATH);
		System.out.println(F_API_KEY);
	}

	@Test
	public void test2() {
		(new File("C:\\Users\\lefto\\OneDrive\\Flickr\\2019\\04")).mkdirs();
	}
}
