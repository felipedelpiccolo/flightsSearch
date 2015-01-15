package flightsSearch.utils;

import java.util.Iterator;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	
	public static void printFares(final Map<Itinerary, RouteFare> faresByItinerary) {

		for (final Iterator<Itinerary> itineraries = faresByItinerary.keySet()
				.iterator(); itineraries.hasNext();) {
			final Itinerary itinerary = itineraries.next();
			System.out.println("Fare found for itinerary: ");
			printItinerary(itinerary);
			System.out.println("Route From Fare: " + faresByItinerary.get(itinerary).getOutboundFare());
			System.out.println("Route To Fare: " + faresByItinerary.get(itinerary).getInboundFare());
			System.out.println("-------------------------------------------");

		}
	}

	public static void printItinerary(final Itinerary itinerary) {

		System.out.println("Segment 1: ");
		System.out.println("From: " + itinerary.getRouteFrom().getFrom());
		System.out.println("To: " + itinerary.getRouteFrom().getTo());
		System.out.println("Date: "
				+ dateFormatter.print(itinerary.getRouteFrom()
						.getDepartureDate()));

		System.out.println("Segment 2: ");
		System.out.println("From: " + itinerary.getRouteTo().getFrom());
		System.out.println("To: " + itinerary.getRouteTo().getTo());
		System.out.println("Date: "
				+ dateFormatter
						.print(itinerary.getRouteTo().getDepartureDate()));
	}
	
}
