package flightsSearch.model;

import org.joda.time.LocalDate;

public class Route {

	private String from;
	private String to;
	private LocalDate departureDate;
	
	public Route(final Route route) {
		this.from = route.from;
		this.to = route.to;
		this.departureDate = route.departureDate;
	}
	
	public Route() {
		super();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}
	
}
