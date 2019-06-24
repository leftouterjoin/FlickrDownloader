package flickrdownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

public class FlickrDownloader {
	private static final int PER_PAGE = 500;

	private static final SearchParameters searchParameters;
	static {
		searchParameters = new SearchParameters() {
			{
				setSort(SearchParameters.DATE_POSTED_ASC);
				setUserId(FlickrAuth.F_USER_ID);
				try {
//					setMinUploadDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("2019/05/30 00:00:00"));
					setMinTakenDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("0000/01/01 00:00:00"));
//					setMaxTakenDate(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("2008/12/31 23:59:59"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				setExtras(new HashSet<String>() {
					{
						add("date_upload");
						add("date_taken");
						add("url_o");
					}
				});
			}
		};
	}

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

		int pages = 1;
		int pageNo = 1;

		while(pageNo <= pages) {
			PhotoList<Photo> pl = pi.search(searchParameters, PER_PAGE, pageNo);
			System.out.println(pl.size() + " / " + pl.getTotal() + ", " + pl.getPage() + " / " + pl.getPages());
			pages = pl.getPages();
			pageNo++ ;
//			pl.forEach(p ->
//			{
//				try {
//					System.out.println(p.getId() + ", "
//							+ p.getDatePosted() + ", "
//							+ p.getDateTaken() + ", "
//							+ p.getOriginalUrl());
//				} catch (FlickrException e) {
//					// TODO 自動生成された catch ブロック
//					e.printStackTrace();
//				}
//			});
		};
	}
}
