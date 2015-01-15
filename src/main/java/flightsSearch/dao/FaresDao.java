package flightsSearch.dao;

import java.util.Map;

import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;

public interface FaresDao {

	public void saveFaresByItinerary(final Map<Itinerary, RouteFare> faresByItinerary);
	
}
