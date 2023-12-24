package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface FilterFlight {
    List<Flight> filteringRule(List<Flight> flights);
}

//Filters

/**
 * Filter the departure to the current time
 */
class DepartureBeforeCurrentTime implements FilterFlight {
    @Override
    public List<Flight> filteringRule(List<Flight> flights) {
        LocalDateTime timeNow = LocalDateTime.now();
        return flights.stream()
                .parallel()
                .filter(f -> f.getSegments().get(0).getDepartureDate().isAfter(timeNow))
                .toList();
    }
}

/**
 * Arrival date before departure date
 */
class ArrivalDateBeforeDepartureDate implements FilterFlight {
    @Override
    public List<Flight> filteringRule(List<Flight> flights) {
        return flights.stream()
                .parallel()
                .filter(f -> {
                    LocalDateTime departureDate = f.getSegments().get(0).getDepartureDate();
                    LocalDateTime arrivalDate = f.getSegments().get(f.getSegments().size() - 1).getArrivalDate();
                    return departureDate.isBefore(arrivalDate);})
                .toList();
    }
}

/**
 * The total time spent on earth exceeds two hours
 */
class WaitingAtLandMoreThanTwoHours implements FilterFlight {
    @Override
    public List<Flight> filteringRule(List<Flight> flights) {
        return flights.stream()
                .parallel()
                .filter(f -> {
                    List<Segment> segments = f.getSegments();
                    if (segments.size() == 1) {
                        return true;
                    }
                    long minustes = 0;
                    for (int i = 1; i < segments.size(); i++) {
                        minustes += Duration.between(segments.get(i - 1).getArrivalDate(), segments.get(i).getDepartureDate())
                                .toMinutes();
                    }
                    return minustes <= 120;})
                .toList();
    }
}