package flightsSearch.iberia;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import flightsSearch.model.RouteFare;
import flightsSearch.utils.Utils;

public class FlightsResult {

	private final WebDriver driver;

	public FlightsResult(final WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public RouteFare getBestPrice() {
		final WebElement outboundFlights = driver.findElement(By.className("from-fly-table"));
		final Integer outboundBestFare = selectJourneyBestPrice(outboundFlights);
		
		driver.findElement(By.className("from-to-breakpoint")).click();
		Utils.waitTimeToLoad(1);
		
		final WebElement inboundFlights = driver.findElement(By.className("return-fly-table"));
		final Integer inboundBestFare = selectJourneyBestPrice(inboundFlights);
		
		final RouteFare routeBestFare = new RouteFare();
		routeBestFare.setInboundFare(inboundBestFare);
		routeBestFare.setOutboundFare(outboundBestFare);
		return routeBestFare;
	}
	
	private Integer selectJourneyBestPrice(final WebElement journeyFlights) {
		final List<WebElement> fares = journeyFlights.findElements(By.cssSelector("li.odd"));
		int minFare = 9999;
		
		for (final WebElement fareElement : fares) {
			if(fareElement.getAttribute("class").contains("not-available")) {
				continue;
			}
			final WebElement priceLabel = fareElement.findElement(By.cssSelector("label"));
			final Integer fare = getPriceFromString(priceLabel.getText());
			if (fare < minFare ) {
				minFare = fare;
			}
		}
		
		
		return minFare;
	}
	
	private Integer getPriceFromString(String priceAndCurrency) {
		//Remove last 2 digits for decimals
		priceAndCurrency = priceAndCurrency.replaceAll("\\.\\d\\d", "");
		//Remove any non number character 
		final String priceStr = priceAndCurrency.replaceAll("[^\\d]", "");
		return Integer.valueOf(priceStr);
	}
}
