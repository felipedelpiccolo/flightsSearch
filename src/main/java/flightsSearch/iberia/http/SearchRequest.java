package flightsSearch.iberia.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;

public class SearchRequest {

	private static final Logger logger = LoggerFactory
			.getLogger(SearchRequest.class);

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36";
	private static HttpClientBuilder clientBuilder;
	private static HttpClient client;
	private static String cookie;

	private static String homeUrl = "http://www.iberia.com/";
	private static final String searchServiceUrl = "http://www.iberia.com/web/dispatchComplexSearch.do";

	public SearchRequest(String country, String language) {
		homeUrl = homeUrl + country + "/?language=" + language;
		
		contructClient();
		
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
	}

	private void contructClient() {
		clientBuilder = HttpClientBuilder.create();
		clientBuilder.setUserAgent(USER_AGENT);
		client = clientBuilder.build();
	}
	
	public Document search(final Itinerary itinerary) {
		
		if (cookie == null) {
			this.getSession();
		}
		
		final HttpPost post = new HttpPost(searchServiceUrl);
  	
		final List<NameValuePair> urlParameters = getSearchParameters(itinerary);
		
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (final UnsupportedEncodingException e) {
			logger.error("Error setting parameters in encoded form " + urlParameters);
			throw new RuntimeException("Error setting search paramters", e);
		}
		
		post.setHeader("Host", "www.iberia.com");
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Accept-Language", "en-US,en;q=0.8,es-419;q=0.6,es;q=0.4");
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Referer", "http://www.iberia.com/web/program.do?menuId=IBMUSE");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		post.setHeader("Cookie", cookie);
		
		try {
			HttpResponse response = client.execute(post);
			InputStream responseContent = response.getEntity().getContent();
			contructClient();
			return Jsoup.parse(responseContent, "UTF-8", searchServiceUrl);
		} catch (final IOException e) {
			throw new RuntimeException("Error searching for itinerary", e);
		} finally {
			//contructClient();
			post.releaseConnection();
		}

		
		
//		if(logger.isDebugEnabled()) {
//		
//			logger.debug("Sending 'POST' request to URL : " + searchServiceUrl);
//			logger.debug("Post parameters : " + post.getEntity());
//			logger.debug("Response Code : " + 
//	                                    response.getStatusLine().getStatusCode());
//	 		
//			try {
//				final BufferedReader rd = new BufferedReader(
//				                new InputStreamReader(response.getEntity().getContent()));
//				final StringBuffer result = new StringBuffer();
//				String line = "";
//				while ((line = rd.readLine()) != null) {
//					result.append(line);
//				}
//				logger.debug(result.toString());
//			} catch (IllegalStateException | IOException e) {
//				throw new RuntimeException("Error reading response content", e);
//			}
//		}
		
	}

	private List<NameValuePair> getSearchParameters(final Itinerary itinerary) {
		final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		getHeadParameters(urlParameters);
		
		setRouteParameters(urlParameters, itinerary.getRouteFrom(), 1);
		setRouteParameters(urlParameters, itinerary.getRouteTo(), 2);
		
		urlParameters.add(new BasicNameValuePair("ADT", "1"));
		urlParameters.add(new BasicNameValuePair("CHD", "0"));
		urlParameters.add(new BasicNameValuePair("INF", "0"));
		urlParameters.add(new BasicNameValuePair("FARE_TYPE", "R"));
		urlParameters.add(new BasicNameValuePair("boton", "Find it"));
		urlParameters.add(new BasicNameValuePair("flexible", "true"));
		
		return urlParameters;
	}

	private void setRouteParameters(final List<NameValuePair> urlParameters, final Route route, final int routeNumber) {
		
		final String routeNumberPlus1 = String.valueOf(routeNumber+1);
		final String routeNumberStr = String.valueOf(routeNumber);
		urlParameters.add(new BasicNameValuePair("text-from"+routeNumberPlus1+"-visible", ""));
		urlParameters.add(new BasicNameValuePair("text-to"+routeNumberPlus1+"-visible", ""));
		urlParameters.add(new BasicNameValuePair("text-date-from"+routeNumberPlus1, ""));
		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_0"+routeNumberStr, route.getFrom()));
		urlParameters.add(new BasicNameValuePair("END_CITY_0"+routeNumberStr, route.getTo()));
		final int departureDay = route.getDepartureDate().getDayOfMonth();
		final int departureMonth = route.getDepartureDate().getMonthOfYear();
		final int departureYear = route.getDepartureDate().getYear();
		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_0"+routeNumberStr, String.valueOf(departureDay)));
		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_0"+routeNumberStr, String.valueOf(departureYear)+String.valueOf(departureMonth)));
		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR2_0"+routeNumberStr, String.valueOf(departureYear)));
		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_0"+routeNumberStr, "0000"));
	}

	private void getHeadParameters(final List<NameValuePair> urlParameters) {
		urlParameters.add(new BasicNameValuePair("menuId", "IBMUSE"));
		urlParameters.add(new BasicNameValuePair("splitEndCity", "true"));
		urlParameters.add(new BasicNameValuePair("numTrayectos", "2"));
		urlParameters.add(new BasicNameValuePair("TRIP_TYPE", "3"));
		urlParameters.add(new BasicNameValuePair("bookingSearch", "true"));
		urlParameters.add(new BasicNameValuePair("firstLoad", "1"));
		urlParameters.add(new BasicNameValuePair("method", "first"));
		urlParameters.add(new BasicNameValuePair("appliesOMB", "false"));
		urlParameters.add(new BasicNameValuePair("js", "false"));
		urlParameters.add(new BasicNameValuePair("loading", "Loading..."));
		urlParameters.add(new BasicNameValuePair("bookingMarket", "GB"));
		urlParameters.add(new BasicNameValuePair("currency", "GBP"));
		urlParameters.add(new BasicNameValuePair("MAX_DAYS", "360"));
		urlParameters.add(new BasicNameValuePair("MIN_HOURS", "6"));
		urlParameters.add(new BasicNameValuePair("maxPax", "9"));
		urlParameters.add(new BasicNameValuePair("origenBusqueda", "IBMUSE"));
	}

	private void getSession() {
		final HttpGet get = new HttpGet(homeUrl);

		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Language", "en-US,en;q=0.5");

		HttpResponse response;
		try {
			response = client.execute(get);
		} catch (final IOException e) {
			throw new RuntimeException("Error executing get request to obtain new session", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Sending 'GET' request to URL : " + homeUrl);
			logger.debug("Response Code : "
					+ response.getStatusLine().getStatusCode());
			logger.debug(getResponseContent(response));
		}

		cookie = getCookie(response);

		logger.info("New Session Cookie set: " + cookie);
	}

	private String getCookie(final HttpResponse response) {
		String cookie = "";

		final Header[] cookies = response.getHeaders("Set-Cookie");

		for (final Header header : cookies) {
			final String cookieValue = header.getValue().split(";")[0];
			cookie = cookie + cookieValue + "; ";
		}

		return cookie;
	}

	private String getResponseContent(final HttpResponse response) {
		try {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
	
			final StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch(final IOException e) {
			throw new RuntimeException("Error reading response content", e);
		}
	}

	
}
