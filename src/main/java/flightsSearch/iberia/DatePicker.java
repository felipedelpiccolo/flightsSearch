package flightsSearch.iberia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import flightsSearch.Utils;

public class DatePicker {

	private final WebDriver driver;
	
	public DatePicker(final WebDriver driver) {
		this.driver = driver;
	}
	
	public void setDepartureDate(final Date date) {
		final WebElement dateFromInput = driver.findElement(By.id("diaSalida2"));
		openDatePickerOnInput(dateFromInput);

		setDateOnDatePicker(date);

	}

	public void setReturnDate(final Date date) {
		final WebElement dateFromInput = driver.findElement(By.id("diaSalida3"));
		openDatePickerOnInput(dateFromInput);

		setDateOnDatePicker(date);
	}

	private void openDatePickerOnInput(final WebElement inputContainingDatePicker) {
		inputContainingDatePicker.sendKeys("");
		Utils.waitTimeToLoad(2);
	}

	private void setDateOnDatePicker(final Date date) {
		final WebElement datePicker = driver.findElement(By.id("calendar_selector2"));

		final WebElement datePickerGroupContainingDate = getDatePickerGroupContainingDate(
				datePicker, date);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		final WebElement dayElement = datePickerGroupContainingDate.findElement(By
				.partialLinkText(String.valueOf(calendar
						.get(Calendar.DAY_OF_MONTH))));

		dayElement.click();
		//wait datepicker to close
		Utils.waitTimeToLoad(1);
	}

	private WebElement getDatePickerGroupContainingDate(final WebElement datePicker,
			final Date date) {

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
			moveDatePickerBackward(datePicker, datePickerDistanceToDate);
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
			final WebElement prevButton = datePicker.findElement(By
					.cssSelector(".ui-datepicker-group-first .ui-datepicker-prev"));
			prevButton.click();
			nTimes--;
		}
	}

	private void moveDatePickerForward(final WebElement datePicker, int nTimes) {
		
		while (nTimes > 0) {
			final WebElement nextButton = datePicker.findElement(By
					.cssSelector(".ui-datepicker-group-last .ui-datepicker-next"));
			nextButton.click();
			nTimes--;
		}
	}

	private int calculateDistanceToDate(final Date date,
			final WebElement datePickerGroupElement) {
		final String actualMonthName = datePickerGroupElement.findElement(
				By.className("ui-datepicker-month")).getText();
		final String actualYear = datePickerGroupElement.findElement(
				By.className("ui-datepicker-year")).getText();

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		final int yearDifference = (calendar.get(Calendar.YEAR) - Integer
				.valueOf(actualYear)) * 12;
		final int monthDifference = (calendar.get(Calendar.MONTH) - convertMonthNameToNumber(actualMonthName));

		return yearDifference + monthDifference;
	}

	private int convertMonthNameToNumber(final String monthName) {

		try {
			final Date monthDate = new SimpleDateFormat("MMMM").parse(monthName);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(monthDate);
			return calendar.get(Calendar.MONTH);
		} catch (final ParseException e) {
			e.printStackTrace();
			return -1;
		}

	}

	
}
