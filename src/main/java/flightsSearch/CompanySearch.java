package flightsSearch;

import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;

public interface CompanySearch {

	public RouteFare searchBestFares(final Itinerary itinerary);
	
}
