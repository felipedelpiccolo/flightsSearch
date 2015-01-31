package flightsSearch.iberia.http;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flightsSearch.CompanySearch;
import flightsSearch.model.Itinerary;
import flightsSearch.model.RouteFare;

public class HttpIberiaFlightSearch implements CompanySearch {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpIberiaFlightSearch.class);

	private SearchRequest searchRequest;

	public HttpIberiaFlightSearch(String country, String language) {
		searchRequest = new SearchRequest(country, language);
	}

	@Override
	public RouteFare searchBestFares(final Itinerary itinerary) {

		Document document = searchRequest.search(itinerary);

		final RouteFare routeBestFare = new RouteFare();
		routeBestFare.setInboundFare(findBestPrice(document, "from"));
		routeBestFare.setOutboundFare(findBestPrice(document, "return"));
		return routeBestFare;
	}

	private Integer findBestPrice(Document document, String fromOrReturn) {
		Elements outboundBestPrices = document.select("div." + fromOrReturn
				+ "-fly-table span.lowest-price");
		Element minPriceElement = outboundBestPrices.first().parent()
				.getElementsByAttributeValue("name", "fly-min-price").first();
		//Remove any decimal and non-number character
		return Integer.valueOf(minPriceElement.val().replaceAll("\\.\\d\\d", "").replaceAll("[^\\d]", ""));
	}

}
