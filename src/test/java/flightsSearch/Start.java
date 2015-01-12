package flightsSearch;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import flightsSearch.core.FaresSearchEngine;
import flightsSearch.iberia.IberiaFlightSearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;

public class Start {

	private final String chromeDriverPath = this.getClass()
			.getResource("/chromedriver.exe").getPath();

	private static ChromeDriverService chromeService;

	private WebDriver driver;

	private IberiaFlightSearch iberiaFlightSearch;
	
	private FaresSearchEngine faresSearchEngine;

	@Before
	public void setUp() throws Exception {
		chromeService = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(chromeDriverPath))
				.usingAnyFreePort().build();

		chromeService.start();

		driver = new RemoteWebDriver(chromeService.getUrl(),
				DesiredCapabilities.chrome());

		// driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		iberiaFlightSearch = new IberiaFlightSearch(driver);
		
		faresSearchEngine = new FaresSearchEngine(iberiaFlightSearch);

	}

	@Test
	public void start() throws Exception {

		final Route routeFrom = new Route();
		routeFrom.setTo("Buenos Aires");
		routeFrom.setFrom("Madrid");
		routeFrom.setDepartureDate(new LocalDate().plusDays(15));

		final Route routeTo = new Route();
		routeTo.setTo("Berlin");
		routeTo.setFrom("Buenos Aires");
		routeTo.setDepartureDate(routeFrom.getDepartureDate().plusMonths(1));
		
		final Itinerary itinerary = new Itinerary();
		itinerary.setRouteFrom(routeFrom);
		itinerary.setRouteTo(routeTo);
		
		final DateTime start = new DateTime().plusMonths(1);
		final DateTime end = start.plusMonths(1);
		
		faresSearchEngine.searchFares(new Interval(start, end), itinerary);

	}

	@After
	public void tearDown() {
		driver.close();
	}

}
