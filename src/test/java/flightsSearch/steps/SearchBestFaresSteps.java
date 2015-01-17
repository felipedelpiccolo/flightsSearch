package flightsSearch.steps;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import flightsSearch.core.FaresSearchEngine;
import flightsSearch.dao.FileBasedFaresDao;
import flightsSearch.iberia.IberiaFlightSearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;

public class SearchBestFaresSteps {

	private FaresSearchEngine faresSearchEngine;

	private Itinerary itinerary;
	private Interval interval;

	//TODO to injected via spring IOD
	private final String chromeDriverPath = this.getClass()
			.getResource("/chromedriver.exe").getPath();

	private static ChromeDriverService chromeService;

	private WebDriver driver;

	private IberiaFlightSearch iberiaFlightSearch;
	
	@Given("^Things are setup$")
	public void setUp() throws Exception {
		chromeService = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(chromeDriverPath))
				.usingAnyFreePort().build();

		chromeService.start();

		driver = new RemoteWebDriver(chromeService.getUrl(),
				DesiredCapabilities.chrome());

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		iberiaFlightSearch = new IberiaFlightSearch(driver);
		
		faresSearchEngine = new FaresSearchEngine(iberiaFlightSearch, new FileBasedFaresDao());

	}

	
	@Given("^An itinerary with the route from (.+) to (.+)$")
	public void createItinerayWithRouteFrom(String from, String to) {
		Route routeFrom = new Route();
		routeFrom.setFrom(from);
		routeFrom.setTo(to);
		itinerary = new Itinerary();
		itinerary.setRouteFrom(routeFrom);
	}

	@And("^the route from (.+) to (.+)$")
	public void addRouteToToItinerary(String from, String to) {
		Route routeTo = new Route();
		routeTo.setFrom(from);
		routeTo.setTo(to);
		itinerary.setRouteTo(routeTo);
	}

	@And("^the interval of time is from today plus (\\d+) months to that date plus (\\d+)$")
	public void createTheIntervalOfTime(Integer fromPlusMonths,
			Integer toPlusMonths) {
		DateTime fromDate = new DateTime().plusMonths(fromPlusMonths);
		DateTime toDate = fromDate.plusMonths(toPlusMonths);
		interval = new Interval(fromDate, toDate);
	}

	@Then("^I search fares$")
	public void searchFares() {
		faresSearchEngine.searchFares(interval, itinerary);
	}

	public void setFaresSearchEngine(FaresSearchEngine faresSearchEngine) {
		this.faresSearchEngine = faresSearchEngine;
	}

}
