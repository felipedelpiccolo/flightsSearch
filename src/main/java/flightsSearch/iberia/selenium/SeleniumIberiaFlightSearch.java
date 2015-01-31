package flightsSearch.iberia.selenium;

import org.openqa.selenium.WebDriver;

import flightsSearch.CompanySearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;

public class SeleniumIberiaFlightSearch implements CompanySearch {

	private final WebDriver driver;

	private static final String baseSearchUrl = "http://www.iberia.com/web/obsmenu.do?language=en&country=AR&quadrigam=IBMUSE&quickaccess=true";

	private final SearchForm searchForm;

	private final FlightsResult flightsResult;

	public SeleniumIberiaFlightSearch(final WebDriver driver) {
		this.driver = driver;
		this.searchForm = new SearchForm(driver);
		this.flightsResult = new FlightsResult(driver);
	}

	public RouteFare searchBestFares(final Itinerary itinerary) {
		driver.get(baseSearchUrl);

		searchForm.search(itinerary);

		return flightsResult.getBestPrice();
	}

}
