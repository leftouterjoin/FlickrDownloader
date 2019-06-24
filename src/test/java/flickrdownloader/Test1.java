package flickrdownloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

public class Test1 {
	private Flickr flickr;

	@Before
	public void init() {
		flickr = new Flickr(
				FlickrAuth.F_API_KEY,
				FlickrAuth.F_API_SECRET,
				new REST());
		Auth auth = new Auth();
		auth.setToken(FlickrAuth.F_OAUTH_TOKEN);
		auth.setTokenSecret(FlickrAuth.F_OAUTH_SECRET);
		RequestContext requestContext = RequestContext.getRequestContext();
		requestContext.setAuth(auth);
	}

	@Test
	public void test2() throws FlickrException, ParseException {
		PhotosInterface pi = flickr.getPhotosInterface();
		PhotoList<Photo> pl = pi.search(new SearchParameters() {
			{
				setSort(SearchParameters.DATE_POSTED_ASC);
				setUserId(FlickrAuth.F_USER_ID);
//				setMinUploadDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("2019/05/30 00:00:00"));
				setMaxTakenDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("2007/01/01 00:00:00"));
				setExtras(new HashSet<String>(){{
					add("date_upload");
					add("date_taken");
					add("url_o");
				}});
			}
		}, 20, 1);

		pl.forEach(p ->
		{
			try {
				pi.getAllContexts(p.getId()).getPhotoSetList().forEach(ps -> {
					System.out.println("\t" + ps.getTitle());
				});
				System.out.println(p.getUrl() + ", "
						+ p.getDatePosted() + ", "
						+ p.getDateTaken() + ", "
						+ p.getOriginalUrl());
			} catch (FlickrException e) {
				throw new RuntimeException(e);
			}
		});

//		PhotosetsInterface photosetsInterface = flickr.getPhotosetsInterface();
//		Photosets photosets = photosetsInterface.getList(null);
//
//		photosets.getPhotosets().stream().map(Photoset::getId).flatMap(id -> {
//			try {
//				return photosetsInterface.getPhotos(id, 0, 0).stream();
//			} catch (FlickrException e) {
//				throw new RuntimeException();
//			}
//		}).forEach(photo -> System.out.println(
//				String.format(IMAGE_URL, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret(), "n")));
	}

	@Test
	public void test3() throws IOException {
		final String urlString = "https://live.staticflickr.com/5592/14719981070_36f6373ff4_o.jpg";
//		final String urlString = "https://live.staticflickr.com/65535/47970655111_58dc7d2baf_o.jpg";
		ReadableByteChannel readChannel = Channels
				.newChannel(new URL(urlString).openStream());
		try (FileOutputStream fileOS = new FileOutputStream("C://tmp/14719981070_36f6373ff4_o.jpg")) {
			FileChannel writeChannel = fileOS.getChannel();
			writeChannel
					.transferFrom(readChannel, 0, Long.MAX_VALUE);
		}
	}

	@Test
	public void test4() throws Throwable {
		PhotosetsInterface psi = flickr.getPhotosetsInterface();
		Photosets ps = psi.getList(FlickrAuth.F_USER_ID);
		ps.getPhotosets().forEach(p -> {
			System.out.println(p.getTitle());
		});
	}

	@Test
	public void test5() throws Throwable {
		PhotosInterface pi = flickr.getPhotosInterface();
		pi.getCounts(null, null);
	}
}
