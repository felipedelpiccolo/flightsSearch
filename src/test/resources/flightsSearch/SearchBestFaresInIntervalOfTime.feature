Feature: Search flights best fares in a range of dates and destinations

  Background: 
    Given Things are setup

  Scenario Outline: Search Flights with two routes on a Internal of time
    Given An itinerary with the route from <route1From> to <route1To>
    And the route from <route2From> to <route2To>
    And the interval of time is from <fromDate> to <toDate>
    Then I search fares

    Examples: 
      | route1From | route1To | route2From | route2To | fromDate   | toDate     |
      | MAD        | BUE      | BUE        | BER      | 20/06/2015 | 08/08/2015 |
      | PRG        | BUE      | BUE        | MAD      | 20/06/2015 | 08/08/2015 |
      | BUE        | LON      | MAD        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | BCN        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | PAR        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | AMS        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | FRA        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | BER        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | ROM        | BUE      | 01/05/2015 | 23/06/2015 |
      | BUE        | LON      | MIL        | BUE      | 01/05/2015 | 23/06/2015 |
