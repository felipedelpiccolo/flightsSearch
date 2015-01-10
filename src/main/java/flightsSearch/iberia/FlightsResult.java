package flightsSearch.iberia;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import flightsSearch.Utils;

public class FlightsResult {

	private final WebDriver driver;

	public FlightsResult(final WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public Integer getBestPrice() {
		final WebElement outboundFlights = driver.findElement(By.className("from-fly-table"));
		selectJourneyBestPrice(outboundFlights);
		
		driver.findElement(By.className("from-to-breakpoint")).click();
		Utils.waitTimeToLoad(1);
		
		final WebElement inboundFlights = driver.findElement(By.className("return-fly-table"));
		selectJourneyBestPrice(inboundFlights);
		
		final String priceAndCurrency = driver.findElement(By.cssSelector("span.total-price")).getText();
		return getPriceFromSring(priceAndCurrency);
	}
	
	private void selectJourneyBestPrice(final WebElement journeyFlights) {
		final List<WebElement> fares = journeyFlights.findElements(By.cssSelector("li.odd"));
		int minFare = 9999;
		WebElement minFareElement = null;
		for (final WebElement fareElement : fares) {
			if(fareElement.getAttribute("class").contains("not-available")) {
				continue;
			}
			final WebElement priceLabel = fareElement.findElement(By.cssSelector("label"));
			final Integer fare = getPriceFromSring(priceLabel.getText());
			if (fare < minFare ) {
				minFare = fare;
				minFareElement = fareElement;
			}
		}
		
		
		minFareElement.click();
	}
	
	private Integer getPriceFromSring(String priceAndCurrency) {
		//Remove last 2 digits for decimals
		priceAndCurrency = priceAndCurrency.replaceAll("\\.\\d\\d", "");
		//Remove any non number character 
		final String priceStr = priceAndCurrency.replaceAll("[^\\d]", "");
		return Integer.valueOf(priceStr);
	}
}
