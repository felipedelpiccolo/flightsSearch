Feature: Search flights best fares in a range of dates and destinations

  Background: 
    Given Things are setup

  Scenario Outline: Search Flights with two routes departing and arriving between dates
    Given An itinerary with the route from <route1From> to <route1To>
    And the route from <route2From> to <route2To>
    And departure date is between <departingFromDate> and <departingToDate>
    And arrival date is between <arrivalFromDate> and <arrivalToDate>
    Then I search fares between dates

    Examples: 
      | route1From | route1To | route2From | route2To | departingFromDate | departingToDate | arrivalFromDate | arrivalToDate |
      | BUE        | LON      | MAD        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | LON      | BCN        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | LON      | ROM        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | LON      | PAR        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | LON      | MIL        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | MAD      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | BCN      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | ROM      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | PAR      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | MIL      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | MAD      | MAD        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | LON      | LON        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |
      | BUE        | ROM      | ROM        | BUE      | 22/05/2015        | 24/05/2015      | 12/06/2015      | 14/06/2015    |