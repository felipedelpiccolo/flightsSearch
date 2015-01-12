package flightsSearch.core;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import flightsSearch.CompanySearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;
import flightsSearch.model.RouteFare;
import flightsSearch.utils.Utils;

public class FaresSearchEngine {

	private CompanySearch companySearch;

	public FaresSearchEngine(final CompanySearch companySearch) {
		this.companySearch = companySearch;
	}

	public void searchFares(final Interval timeInterval,
			final Itinerary itinerary) {

		itinerary.getRouteFrom().setDepartureDate(
				timeInterval.getStart().toLocalDate());
		itinerary.getRouteTo().setDepartureDate(
				timeInterval.getEnd().toLocalDate());

		final Map<Itinerary, RouteFare> faresByItinerary = searchFares(
				itinerary.getRouteFrom(), itinerary.getRouteTo());

		Utils.printFares(faresByItinerary);
		
	}

	private Map<Itinerary, RouteFare> searchFares(final Route routeFrom,
			final Route routeTo) {

		final LocalDate originalDD = routeFrom.getDepartureDate();
		
		final Map<Itinerary, RouteFare> faresByItinerary = new HashMap<Itinerary, RouteFare>();

		while (Days.daysBetween(routeFrom.getDepartureDate(),
				routeTo.getDepartureDate()).getDays() > 7) {

			getBestFare(routeFrom, routeTo, faresByItinerary);

			routeFrom
					.setDepartureDate(routeFrom.getDepartureDate().plusDays(1));

		}

		routeFrom.setDepartureDate(originalDD);

		while (Days.daysBetween(routeTo.getDepartureDate(),
				routeFrom.getDepartureDate()).getDays() < -7) {

			getBestFare(routeFrom, routeTo, faresByItinerary);

			routeTo.setDepartureDate(routeTo.getDepartureDate().minusDays(1));

		}

		return faresByItinerary;

	}

	private void getBestFare(Route routeFrom, Route routeTo,
			Map<Itinerary, RouteFare> faresByItinerary) {
		final Itinerary itinerary = new Itinerary();
		itinerary.setRouteFrom(routeFrom);
		itinerary.setRouteTo(routeTo);

		try {
			final RouteFare routeBestFare = companySearch
					.searchBestFares(itinerary);

			faresByItinerary.put(itinerary, routeBestFare);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("!!!!---Error searching for itinerary: ");
			Utils.printItinerary(itinerary);
			System.out.println("!!!--------------------------------!!!");
		}
	}

}
