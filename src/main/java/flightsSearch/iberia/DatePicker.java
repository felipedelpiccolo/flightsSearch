package flightsSearch.iberia;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import flightsSearch.utils.Utils;

public class DatePicker {

	private final WebDriver driver;

	public DatePicker(final WebDriver driver) {
		this.driver = driver;
	}

	public void setDepartureDate(final LocalDate date) {
		final WebElement dateFromInput = driver
				.findElement(By.id("diaSalida2"));
		openDatePickerOnInput(dateFromInput);

		setDateOnDatePicker(date);

	}

	public void setReturnDate(final LocalDate date) {
		final WebElement dateFromInput = driver
				.findElement(By.id("diaSalida3"));
		openDatePickerOnInput(dateFromInput);

		setDateOnDatePicker(date);
	}

	private void openDatePickerOnInput(
			final WebElement inputContainingDatePicker) {
		inputContainingDatePicker.sendKeys("");
		Utils.waitTimeToLoad(2);
	}

	private void setDateOnDatePicker(final LocalDate date) {
		final WebElement datePicker = driver.findElement(By
				.id("calendar_selector2"));

		final WebElement datePickerGroupContainingDate = getDatePickerGroupContainingDate(
				datePicker, date);

		final WebElement dayElement = datePickerGroupContainingDate
				.findElement(By.partialLinkText(String.valueOf(date
						.getDayOfMonth())));

		dayElement.click();
		// wait datepicker to close
		Utils.waitTimeToLoad(1);
	}

	private WebElement getDatePickerGroupContainingDate(
			final WebElement datePicker, final LocalDate date) {

		final WebElement leftGroupDates = datePicker.findElement(By
				.className("ui-datepicker-group-first"));

		final int datePickerDistanceToDate = calculateDistanceToDate(date,
				leftGroupDates);

		if (datePickerDistanceToDate == 0) {
			return leftGroupDates;
		} else if (datePickerDistanceToDate == 1) {
			return datePicker.findElement(By
					.className("ui-datepicker-group-last"));
		} else if (datePickerDistanceToDate < 0) {
			moveDatePickerBackward(datePicker, datePickerDistanceToDate * -1);
			return datePicker.findElement(By
					.className("ui-datepicker-group-first"));
		} else {
			moveDatePickerForward(datePicker, datePickerDistanceToDate);
			return datePicker.findElement(By
					.className("ui-datepicker-group-first"));
		}
	}

	private void moveDatePickerBackward(final WebElement datePicker, int nTimes) {

		while (nTimes > 0) {
			final WebElement prevButton = datePicker
					.findElement(By
							.cssSelector(".ui-datepicker-group-first .ui-datepicker-prev"));
			prevButton.click();
			nTimes--;
		}
	}

	private void moveDatePickerForward(final WebElement datePicker, int nTimes) {

		while (nTimes > 0) {
			final WebElement nextButton = datePicker
					.findElement(By
							.cssSelector(".ui-datepicker-group-last .ui-datepicker-next"));
			nextButton.click();
			nTimes--;
		}
	}

	private int calculateDistanceToDate(final LocalDate date,
			final WebElement datePickerGroupElement) {

		final String actualMonthName = datePickerGroupElement.findElement(
				By.className("ui-datepicker-month")).getText();
		final String actualYear = datePickerGroupElement.findElement(
				By.className("ui-datepicker-year")).getText();

		final LocalDate actualDate = new LocalDate(Integer.valueOf(actualYear),
				convertMonthNameToNumber(actualMonthName), date.getDayOfMonth());

		return Months.monthsBetween(actualDate, date).getMonths();	
	}

	private int convertMonthNameToNumber(final String monthName) {
		final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM");
		return dtf.parseDateTime(monthName).getMonthOfYear();
	}

}
