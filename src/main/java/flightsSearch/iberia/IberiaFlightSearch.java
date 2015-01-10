package flightsSearch.iberia;

import org.openqa.selenium.WebDriver;

import flightsSearch.Itinerary;

public class IberiaFlightSearch {

	private final WebDriver driver;

	private static final String baseSearchUrl = "http://www.iberia.com/web/obsmenu.do?language=en&country=GB&quadrigam=IBMUSE&quickaccess=true";

	private final SearchForm searchForm;

	private final FlightsResult flightsResult;

	public IberiaFlightSearch(final WebDriver driver) {
		this.driver = driver;
		this.searchForm = new SearchForm(driver);
		this.flightsResult = new FlightsResult(driver);
	}

	public Integer searchBestFares(final Itinerary itinerary) {
		driver.get(baseSearchUrl);
		
		searchForm.search(itinerary);
		
		return flightsResult.getBestPrice();
	}

	

}
