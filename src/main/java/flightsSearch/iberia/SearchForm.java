package flightsSearch.iberia;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import flightsSearch.Itinerary;
import flightsSearch.Route;
import flightsSearch.Utils;

public class SearchForm {

	private final WebDriver driver;
	private DatePicker datePickerHelper;
	
	public SearchForm(final WebDriver driver) {
		this.driver = driver;
		this.datePickerHelper = new DatePicker(driver);
	}
	
	public void search(final Itinerary itinerary) {
		setFrom(itinerary.getRouteFrom(), 2);
		setTo(itinerary.getRouteFrom(), 2);
		
		setFrom(itinerary.getRouteTo(), 3);
		setTo(itinerary.getRouteTo(), 3);
	
		datePickerHelper.setDepartureDate(itinerary.getRouteFrom().getDepartureDate());
		datePickerHelper.setReturnDate(itinerary.getRouteTo().getDepartureDate());
		
		submit();
	}
	
	private void submit() {
		final WebElement searchForm = driver.findElement(By
				.cssSelector("#search-form form input[type='submit']"));
		searchForm.click();
	}

	private void setTo(final Route route, final int routeNumber) {
		final WebElement toInput = driver.findElement(By.id("text-to" + routeNumber
				+ "-visible"));
		toInput.clear();
		toInput.sendKeys(route.getTo());
		selectDestination(toInput);
	}

	private void setFrom(final Route route, final int routeNumber) {
		final WebElement fromInput = driver.findElement(By.id("text-from"
				+ routeNumber + "-visible"));
		fromInput.clear();
		fromInput.sendKeys(route.getFrom());
		selectDestination(fromInput);
	}

	private void selectDestination(final WebElement input) {
		Utils.waitTimeToLoad(2);
		final WebElement destinationLink = input.findElement(By
				.xpath("../ul[1]/li[1]/a[1]"));
		destinationLink.click();
	}
	
}
