package flightsSearch.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flightsSearch.CompanySearch;
import flightsSearch.dao.FaresDao;
import flightsSearch.model.Itinerary;
import flightsSearch.model.Route;
import flightsSearch.model.RouteFare;
import flightsSearch.utils.Utils;

public class FaresSearchEngine {

	private final static Logger logger = LoggerFactory
			.getLogger(FaresSearchEngine.class);

	private CompanySearch companySearch;

	private FaresDao faresDao;

	public FaresSearchEngine(final CompanySearch companySearch,
			final FaresDao faresDao) {
		this.companySearch = companySearch;
		this.faresDao = faresDao;
	}

	public void searchFares(final Interval timeInterval,
			final Itinerary itinerary) {

		itinerary.getRouteFrom().setDepartureDate(
				timeInterval.getStart().toLocalDate());
		itinerary.getRouteTo().setDepartureDate(
				timeInterval.getEnd().toLocalDate());

		final Map<Itinerary, RouteFare> faresByItinerary = searchFares(
				itinerary.getRouteFrom(), itinerary.getRouteTo());

		faresDao.saveFaresByItinerary(faresByItinerary);

	}
	
	public void searchFaresInBetweenDates(final Interval departureInterval, final Interval arrivalInterval, final Itinerary itinerary)  {
		final Map<Itinerary, RouteFare> faresByItinerary = new LinkedHashMap<Itinerary, RouteFare>();
		
		int departureDaysBetween = Days.daysBetween(departureInterval.getStart(), departureInterval.getEnd()).getDays();
		while (departureDaysBetween >= 0) {
			LocalDate departureDate = departureInterval.getStart().plusDays(departureDaysBetween).toLocalDate();
			Route routeFrom = itinerary.getRouteFrom();
			routeFrom.setDepartureDate(departureDate);
			
			int arrivalDaysBetween = Days.daysBetween(arrivalInterval.getStart(), arrivalInterval.getEnd()).getDays();
			while (arrivalDaysBetween >= 0) {
				LocalDate arrivalDate = arrivalInterval.getStart().plusDays(arrivalDaysBetween).toLocalDate();
				
				Route routeTo = itinerary.getRouteTo();
				routeTo.setDepartureDate(arrivalDate);
				
				logger.info("searching for fares with dates : "+ departureDate + " -- " + arrivalDate);
				
				getBestFare(routeFrom, routeTo, faresByItinerary);
				arrivalDaysBetween--;
			}
			departureDaysBetween--;
		}
		
		faresDao.saveFaresByItinerary(faresByItinerary);
	}

	private Map<Itinerary, RouteFare> searchFares(final Route routeFrom,
			final Route routeTo) {

		final LocalDate originalDD = new LocalDate(routeFrom.getDepartureDate());

		final Map<Itinerary, RouteFare> faresByItinerary = new LinkedHashMap<Itinerary, RouteFare>();

		while (Days.daysBetween(routeFrom.getDepartureDate(),
				routeTo.getDepartureDate()).getDays() > 15) {

			getBestFare(routeFrom, routeTo, faresByItinerary);

			routeFrom
					.setDepartureDate(routeFrom.getDepartureDate().plusDays(1));

		}

		routeFrom.setDepartureDate(originalDD);

		while (Days.daysBetween(routeTo.getDepartureDate(),
				routeFrom.getDepartureDate()).getDays() < -15) {

			getBestFare(routeFrom, routeTo, faresByItinerary);

			routeTo.setDepartureDate(routeTo.getDepartureDate().minusDays(1));

		}

		return faresByItinerary;

	}

	private void getBestFare(final Route routeFrom, final Route routeTo,
			final Map<Itinerary, RouteFare> faresByItinerary) {
		final Itinerary itinerary = new Itinerary();
		itinerary.setRouteFrom(new Route(routeFrom));
		itinerary.setRouteTo(new Route(routeTo));

		try {
			
			final RouteFare routeBestFare = companySearch
					.searchBestFares(itinerary);

			faresByItinerary.put(itinerary, routeBestFare);
		} catch (final Exception e) {
				logger.error(e.getMessage());
				logger.error("!!!!---Error searching for itinerary: ");
				Utils.printItinerary(itinerary, logger);
				logger.error("!!!--------------------------------!!!");
		}
	}

}
