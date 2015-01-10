package flightsSearch;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import flightsSearch.iberia.IberiaFlightSearch;

public class Start {

	private final String chromeDriverPath = this.getClass()
			.getResource("/chromedriver.exe").getPath();

	private static ChromeDriverService chromeService;
	
	private WebDriver driver;

	private static final DateFormat timeInstance = new SimpleDateFormat("dd/mm/yyyy");

	@Before
	public void setUp() throws Exception {
		chromeService = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(chromeDriverPath))
				.usingAnyFreePort().build();

		chromeService.start();

		driver = new RemoteWebDriver(chromeService.getUrl(),
				DesiredCapabilities.chrome());

		//driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void start() throws Exception {
		final IberiaFlightSearch iberiaFlightSearch = new IberiaFlightSearch(driver);

		final Calendar calendarFrom = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendarFrom.setTime(new Date());
		
		final Calendar calendarTo = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendarTo.setTime(new Date());
		calendarTo.add(Calendar.DAY_OF_MONTH, 15);
		
		final Route routeFrom = new Route();
		routeFrom.setTo("Buenos Aires");
		routeFrom.setFrom("Madrid");
		
		final Route routeTo = new Route();
		routeTo.setTo("Berlin");
		routeTo.setFrom("Buenos Aires");
		
		final Map<Itinerary, Integer> faresByItinerary = new HashMap<Itinerary, Integer>(); 
		
		int i = 1;
		
		while (i <= 5) {
			calendarFrom.add(Calendar.DAY_OF_MONTH, 1);
			routeFrom.setDepartureDate(calendarFrom.getTime());
			
			calendarTo.add(Calendar.DAY_OF_MONTH, i);
			routeTo.setDepartureDate(calendarTo.getTime());
			
			i++;
			
			final Itinerary itinerary = new Itinerary();
			itinerary.setRouteFrom(routeFrom);
			itinerary.setRouteTo(routeTo);
			
			try {
				final Integer fare = iberiaFlightSearch.searchBestFares(itinerary);
				faresByItinerary.put(itinerary, fare);
			} catch (Exception e) {
				System.out.println("!!!!---Error searching for itinerary: ");
				printItinerary(itinerary);
				System.out.println("!!!--------------------------------!!!");
			}
			
			
		}
		
		printFares(faresByItinerary);
		
	}
	
	private void printFares(final Map<Itinerary, Integer> faresByItinerary) {
		
		for (final Iterator<Itinerary> itineraries = faresByItinerary.keySet().iterator(); itineraries.hasNext();) {
			final Itinerary itinerary = itineraries.next();
			System.out.println("Fare found for itinerary: ");
			printItinerary(itinerary);
			System.out.println("Fare: " + faresByItinerary.get(itinerary));
			System.out.println("-------------------------------------------");

		}
	}
	
	private void printItinerary(final Itinerary itinerary) {
		
		System.out.println("Segment 1: ");
		System.out.println("From: " + itinerary.getRouteFrom().getFrom());
		System.out.println("To: " + itinerary.getRouteFrom().getTo());
		System.out.println("Date: " + timeInstance.format(itinerary.getRouteFrom().getDepartureDate()));
		
		System.out.println("Segment 2: ");
		System.out.println("From: " + itinerary.getRouteTo().getFrom());
		System.out.println("To: " + itinerary.getRouteTo().getTo());
		System.out.println("Date: " + timeInstance.format(itinerary.getRouteTo().getDepartureDate()));
	}
	
	@After
	public void tearDown() {
		driver.close();
	}

}
