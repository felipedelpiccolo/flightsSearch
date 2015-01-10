package flightsSearch;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

	private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");

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

		final Route routeFrom = new Route();
		routeFrom.setTo("Buenos Aires");
		routeFrom.setFrom("Madrid");
		routeFrom.setDepartureDate(new LocalDate().plusDays(15));
		
		final Route routeTo = new Route();
		routeTo.setTo("Berlin");
		routeTo.setFrom("Buenos Aires");
		routeTo.setDepartureDate(routeFrom.getDepartureDate().plusMonths(1));
		
		final Map<Itinerary, Integer> faresByItinerary = new HashMap<Itinerary, Integer>(); 
		
		while (Days.daysBetween(routeFrom.getDepartureDate(), routeTo.getDepartureDate()).getDays() > 7) {
			
			final Itinerary itinerary = new Itinerary();
			itinerary.setRouteFrom(routeFrom);
			itinerary.setRouteTo(routeTo);
			
			try {
				final Integer fare = iberiaFlightSearch.searchBestFares(itinerary);
				faresByItinerary.put(itinerary, fare);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("!!!!---Error searching for itinerary: ");
				printItinerary(itinerary);
				System.out.println("!!!--------------------------------!!!");
			}
			
			routeFrom.setDepartureDate(routeFrom.getDepartureDate().plusDays(1));
						
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
		System.out.println("Date: " + dateFormatter.print(itinerary.getRouteFrom().getDepartureDate()));
		
		System.out.println("Segment 2: ");
		System.out.println("From: " + itinerary.getRouteTo().getFrom());
		System.out.println("To: " + itinerary.getRouteTo().getTo());
		System.out.println("Date: " + dateFormatter.print(itinerary.getRouteTo().getDepartureDate()));
	}
	
	@After
	public void tearDown() {
		driver.close();
	}

}
