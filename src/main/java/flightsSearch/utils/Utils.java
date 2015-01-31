package flightsSearch.utils;

import java.util.Iterator;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;

public class Utils {

	public static final DateTimeFormatter dateFormatter = DateTimeFormat
			.forPattern("MM/dd/yyyy");

	
	public static void waitTimeToLoad(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void printFares(final Map<Itinerary, RouteFare> faresByItinerary, final Logger logger) {

		for (final Iterator<Itinerary> itineraries = faresByItinerary.keySet()
				.iterator(); itineraries.hasNext();) {
			final Itinerary itinerary = itineraries.next();
			logger.info("Fare found for itinerary: ");
			printItinerary(itinerary, logger);
			logger.info("Route From Fare: " + faresByItinerary.get(itinerary).getOutboundFare());
			logger.info("Route To Fare: " + faresByItinerary.get(itinerary).getInboundFare());
			logger.info("-------------------------------------------");

		}
	}

	public static void printItinerary(final Itinerary itinerary, final Logger logger) {

		logger.info("Segment 1: ");
		logger.info("From: " + itinerary.getRouteFrom().getFrom());
		logger.info("To: " + itinerary.getRouteFrom().getTo());
		logger.info("Date: "
				+ dateFormatter.print(itinerary.getRouteFrom()
						.getDepartureDate()));

		logger.info("Segment 2: ");
		logger.info("From: " + itinerary.getRouteTo().getFrom());
		logger.info("To: " + itinerary.getRouteTo().getTo());
		logger.info("Date: "
				+ dateFormatter
						.print(itinerary.getRouteTo().getDepartureDate()));
	}
	
}
