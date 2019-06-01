package flickrdownloader;

import java.text.SimpleDateFormat;
import java.util.HashSet;

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

public class FlickrDownloader {
	public static void main(String ... args) throws Throwable {
		final Flickr flickr = new Flickr(
				FlickrAuth.F_API_KEY,
				FlickrAuth.F_API_SECRET,
				new REST());

		Auth auth = new Auth();
		auth.setToken(FlickrAuth.F_OAUTH_TOKEN);
		auth.setTokenSecret(FlickrAuth.F_OAUTH_SECRET);
		RequestContext requestContext = RequestContext.getRequestContext();
		requestContext.setAuth(auth);

		PhotosInterface pi = flickr.getPhotosInterface();
		PhotoList<Photo> pl = pi.search(new SearchParameters() {
			{
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
				System.out.println(p.getUrl() + ", "
						+ p.getDatePosted() + ", "
						+ p.getDateTaken() + ", "
						+ p.getOriginalUrl());
			} catch (FlickrException e) {
				throw new RuntimeException(e);
			}
		});

		PhotosetsInterface psi = flickr.getPhotosetsInterface();
		Photosets ps = psi.getList(FlickrAuth.F_USER_ID);
		System.out.println(ps);
	}
}
