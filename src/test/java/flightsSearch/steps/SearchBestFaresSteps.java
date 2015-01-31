package flightsSearch.steps;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.Format;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import flightsSearch.CompanySearch;
import flightsSearch.core.FaresSearchEngine;
import flightsSearch.dao.FileBasedFaresDao;
import flightsSearch.iberia.http.HttpIberiaFlightSearch;
import flightsSearch.iberia.selenium.SeleniumIberiaFlightSearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;

public class SearchBestFaresSteps {

	private FaresSearchEngine faresSearchEngine;

	private Itinerary itinerary;
	private Interval interval;

	private Interval departureDateInterval;
	private Interval arrivalDateInterval;

	// TODO to injected via spring IOD
	private final String chromeDriverPath = this.getClass()
			.getResource("/chromedriver.exe").getPath();

	private static ChromeDriverService chromeService;

	private WebDriver driver;

	private CompanySearch iberiaFlightSearch;

	@Given("^Things are setup$")
	public void setUp() throws Exception {
		// chromeService = new ChromeDriverService.Builder()
		// .usingDriverExecutable(new File(chromeDriverPath))
		// .usingAnyFreePort().build();
		//
		// chromeService.start();
		//
		// driver = new RemoteWebDriver(chromeService.getUrl(),
		// DesiredCapabilities.chrome());
		//
		// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// iberiaFlightSearch = new SeleniumIberiaFlightSearch(driver);

		iberiaFlightSearch = new HttpIberiaFlightSearch("gb", "en");

		faresSearchEngine = new FaresSearchEngine(iberiaFlightSearch,
				new FileBasedFaresDao());

	}

	@Then("^tear down$")
	public void tearDown() throws Exception {
		driver.close();
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

	@And("^the interval of time is from (.+) to (.+)$")
	public void createTheIntervalOfTimeWithDates(
			@Format("dd/MM/yyyy") Date dateFrom,
			@Format("dd/MM/yyyy") Date dateTo) {

		interval = new Interval(new DateTime(dateFrom), new DateTime(dateTo));
	}

	@Then("^I search fares$")
	public void searchFares() {
		faresSearchEngine.searchFares(interval, itinerary);
	}

	@And("^departure date is between (.+) and (.+)$")
	public void createDepartureIntervalOfTime(
			@Format("dd/MM/yyyy") Date dateFrom,
			@Format("dd/MM/yyyy") Date dateTo) {
		departureDateInterval = new Interval(new DateTime(dateFrom),
				new DateTime(dateTo));
	}

	@And("^arrival date is between (.+) and (.+)$")
	public void createArrivalIntervalOfTime(
			@Format("dd/MM/yyyy") Date dateFrom,
			@Format("dd/MM/yyyy") Date dateTo) {
		arrivalDateInterval = new Interval(new DateTime(dateFrom),
				new DateTime(dateTo));
	}

	@Then("^I search fares between dates$")
	public void searchFaresBetweenDates() {
		faresSearchEngine.searchFaresInBetweenDates(departureDateInterval,
				arrivalDateInterval, itinerary);
	}

	public void setFaresSearchEngine(FaresSearchEngine faresSearchEngine) {
		this.faresSearchEngine = faresSearchEngine;
	}

}
