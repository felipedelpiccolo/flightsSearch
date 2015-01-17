Feature: Search flights best fares in a range of dates and destinations
  
  Background:
  	Given Things are setup

  Scenario Outline: Search Flights with two routes on a Internal of time
    Given An itinerary with the route from <route1From> to <route1To>
    And the route from <route2From> to <route2To>
    And the interval of time is from today plus <fromPlusMonths> months to that date plus <toPlusMonths>
    Then I search fares

    Examples: 
      | route1From | route1To     | route2From   | route2To | fromPlusMonths | toPlusMonths |
      | Madrid     | Buenos Aires | Buenos Aires | Berlin   | 1              | 3            |
