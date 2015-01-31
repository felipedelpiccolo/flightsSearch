import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import flightsSearch.CompanySearch;
import flightsSearch.iberia.http.HttpIberiaFlightSearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;
import flightsSearch.utils.Utils;


public class TestRequest {
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36";
	private static HttpClientBuilder clientBuilder;
	private static HttpClient client;
	private static String cookie;
	
	public static void main(String[] args) throws Exception {
		clientBuilder = HttpClientBuilder.create();
		clientBuilder.setUserAgent(USER_AGENT);
		client = clientBuilder.build();
		
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
		
		sendGet();
		
		Utils.waitTimeToLoad(5);
		
		sendPost();
		
//		Itinerary itinerary = new Itinerary();
//		Route routeFrom = new Route();
//		routeFrom.setDepartureDate(new LocalDate().plusDays(5));
//		routeFrom.setFrom("LON");
//		routeFrom.setTo("BUE");
//		itinerary.setRouteFrom(routeFrom);
//		Route routeTo = new Route();
//		routeTo.setDepartureDate(new LocalDate().plusDays(30));
//		routeTo.setFrom("BUE");
//		routeTo.setTo("MAD");
//		
//		itinerary.setRouteTo(routeTo);
//		
//		CompanySearch companySearch = new HttpIberiaFlightSearch();
//		companySearch.searchBestFares(itinerary);
		
	}
	
	private static void sendGet() throws Exception {
		String url = "http://www.iberia.com/ar/?language=en";
		
		HttpGet get = new HttpGet(url);
		
		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Language", "en-US,en;q=0.5");
		
		HttpResponse response = client.execute(get);
				
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
	 
		BufferedReader rd = new BufferedReader(
	                new InputStreamReader(response.getEntity().getContent()));
	 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result.toString());
		
		cookie = getCookie(response);
		
//		cookie = response.getFirstHeader("Set-Cookie") == null ? "" : 
//			response.getFirstHeader("Set-Cookie").toString();
		
		System.out.println(cookie);
	}
	
	private static String getCookie(HttpResponse response) {
		String cookie = "";
		
		Header[] cookies = response.getHeaders("Set-Cookie");
		
		for (Header header : cookies) {
			String cookieValue = header.getValue().split(";")[0];
			//cookieValue = cookieValue.replace("ES|", "GB|").replace("|es|", "|en|");
			cookie = cookie + cookieValue + "; ";
		}
		
		return cookie;
	}

	private static void sendPost() throws Exception {
		 
		String url = "http://www.iberia.com/web/dispatchComplexSearch.do";
 
		
		HttpPost post = new HttpPost(url);
  	
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
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
		
		urlParameters.add(new BasicNameValuePair("text-from2-visible", ""));
		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_01", "LON"));
		urlParameters.add(new BasicNameValuePair("text-to2-visible", ""));
		urlParameters.add(new BasicNameValuePair("END_CITY_01", "BUE"));
		urlParameters.add(new BasicNameValuePair("text-date-from2", ""));
		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_01", "13"));
		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_01", "201502"));
		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR2_01", "2015"));
		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_01", "0000"));
		
		urlParameters.add(new BasicNameValuePair("text-from3-visible", ""));
		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_02", "BUE"));
		urlParameters.add(new BasicNameValuePair("text-to3-visible", ""));
		urlParameters.add(new BasicNameValuePair("END_CITY_02", "MAD"));
		urlParameters.add(new BasicNameValuePair("text-date-from3", ""));
		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_02", "21"));
		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_02", "201502"));
		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR3_01", "2015"));
		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_02", "0000"));
		
//		urlParameters.add(new BasicNameValuePair("text-from4-visible", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_03", ""));
//		urlParameters.add(new BasicNameValuePair("text-to4-visible", ""));
//		urlParameters.add(new BasicNameValuePair("END_CITY_03", ""));
//		urlParameters.add(new BasicNameValuePair("text-date-from4", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_03", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_03", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR4_01", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_03", "0000"));
//		
//		urlParameters.add(new BasicNameValuePair("text-from5-visible", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_04", ""));
//		urlParameters.add(new BasicNameValuePair("text-to5-visible", ""));
//		urlParameters.add(new BasicNameValuePair("END_CITY_04", ""));
//		urlParameters.add(new BasicNameValuePair("text-date-from5", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_04", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_04", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR5_01", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_04", "0000"));
//		
//		urlParameters.add(new BasicNameValuePair("text-from6-visible", ""));
//		urlParameters.add(new BasicNameValuePair("text-date-from6", ""));
//		urlParameters.add(new BasicNameValuePair("text-to6-visible", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR6_01", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_05", ""));
//		urlParameters.add(new BasicNameValuePair("END_CITY_05", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_05", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_05", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_05", "0000"));
//		
//		urlParameters.add(new BasicNameValuePair("text-from7-visible", ""));
//		urlParameters.add(new BasicNameValuePair("text-date-from7", ""));
//		urlParameters.add(new BasicNameValuePair("text-to6-visible", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_YEAR7_01", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_CITY_06", ""));
//		urlParameters.add(new BasicNameValuePair("END_CITY_06", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_DAY_06", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_MONTH_06", ""));
//		urlParameters.add(new BasicNameValuePair("BEGIN_HOUR_06", "0000"));

		urlParameters.add(new BasicNameValuePair("ADT", "1"));
		urlParameters.add(new BasicNameValuePair("CHD", "0"));
		urlParameters.add(new BasicNameValuePair("INF", "0"));
		urlParameters.add(new BasicNameValuePair("FARE_TYPE", "R"));
		urlParameters.add(new BasicNameValuePair("boton", "Find it"));
		urlParameters.add(new BasicNameValuePair("flexible", "true"));
		
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		
		post.setHeader("Host", "www.iberia.com");
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Accept-Language", "en-US,en;q=0.8,es-419;q=0.6,es;q=0.4");
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Referer", "http://www.iberia.com/web/program.do?menuId=IBMUSE");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		//post.setHeader("Cookie", "BIGipServerwww.iberia.com=1897638080.20480.0000; JSESSIONID=TQLHJF8QF3gZQVVSW48KJGmVMQyCVBfpt3sd7rxbMn91SM2pvZ0X!2040022907; IBERIACOM_IDENTIFICATION=14222102521761175726527354690424; IBERIACOM_PERSONALIZATION=GB|GB|en| | |GB|;");
		//post.setHeader("Cookie", "JSESSIONID=XRP2JFDpsGxQ2GylvJTgyyLcQ82WJysKWT5sQ4BQLhnwDnxFytHc!1525793893; IBERIACOM_IDENTIFICATION=1422214078196335258651974128011; IBERIACOM_PERSONALIZATION=GB|GB|en| | |GB|; BIGipServerwww.iberia.com=1864083648.20480.0000;");
		post.setHeader("Cookie", cookie);
		
		HttpResponse response = client.execute(post);
		
		
		//Document document = Jsoup.parse(response.getEntity().getContent(), "UTF-8", url);
		
		
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
 
	}

}
