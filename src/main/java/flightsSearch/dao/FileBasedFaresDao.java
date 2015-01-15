package flightsSearch.dao;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;
import flightsSearch.utils.Utils;

public class FileBasedFaresDao implements FaresDao {

	public void saveFaresByItinerary(final Map<Itinerary, RouteFare> faresByItinerary) {
		
		PrintWriter fileWriter = null;
		
		try {
			fileWriter = new PrintWriter("fares-by-itinerary.txt", "UTF-8");
			
			for (final Iterator<Itinerary> itineraries = faresByItinerary.keySet()
					.iterator(); itineraries.hasNext();) {
				
				final Itinerary itinerary = itineraries.next();
				fileWriter.println("Fare found for itinerary: ");
				printItinerary(itinerary, fileWriter);
				fileWriter.println("Route From Fare: " + faresByItinerary.get(itinerary).getOutboundFare());
				fileWriter.println("Route To Fare: " + faresByItinerary.get(itinerary).getInboundFare());
				fileWriter.println("Itinerary Total Fare: " + (faresByItinerary.get(itinerary).getOutboundFare() + faresByItinerary.get(itinerary).getInboundFare()));
				fileWriter.println("-------------------------------------------");

			}
			
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
		
		
	}

	public static void printItinerary(final Itinerary itinerary, final PrintWriter fileWriter) {

		fileWriter.println("Segment 1: ");
		fileWriter.println("From: " + itinerary.getRouteFrom().getFrom());
		fileWriter.println("To: " + itinerary.getRouteFrom().getTo());
		fileWriter.println("Date: "
				+ Utils.dateFormatter.print(itinerary.getRouteFrom()
						.getDepartureDate()));

		fileWriter.println("Segment 2: ");
		fileWriter.println("From: " + itinerary.getRouteTo().getFrom());
		fileWriter.println("To: " + itinerary.getRouteTo().getTo());
		fileWriter.println("Date: "
				+ Utils.dateFormatter
						.print(itinerary.getRouteTo().getDepartureDate()));
	}
	
}
