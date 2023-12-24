package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFilters {
    private List<Flight> flights;
    private LocalDateTime oneDaysFromNow;

    @BeforeEach
    public void beforeEach() {
        oneDaysFromNow = LocalDateTime.now().plusDays(1);
        flights = new ArrayList<>(List.of(
                createFlight(oneDaysFromNow, oneDaysFromNow.plusHours(2))
        ));
    }

    @Test
    public void departureBeforeCurrentTimeTest() {
        flights.add(createFlight(oneDaysFromNow.minusDays(2), oneDaysFromNow));

        FilterFlight filter = new DepartureBeforeCurrentTime();
        List<Flight> answer = filter.filteringRule(flights);

        assertEquals(2, flights.size());
        assertEquals(1, answer.size());
        assertEquals('[' + flights.get(0).toString() + ']', answer.toString());
    }

    @Test
    public void arrivalDateBeforeDepartureDateTest() {
        flights.add(createFlight(oneDaysFromNow, oneDaysFromNow.minusHours(2)));

        FilterFlight filter = new ArrivalDateBeforeDepartureDate();
        List<Flight> answer = filter.filteringRule(flights);

        assertEquals(2, flights.size());
        assertEquals(1, answer.size());
        assertEquals('[' + flights.get(0).toString() + ']', answer.toString());
    }

    @Test
    public void waitingAtLandMoreThanTwoHoursTest() {
        flights.add(createFlight(oneDaysFromNow, oneDaysFromNow.plusHours(3),
                oneDaysFromNow.plusHours(4), oneDaysFromNow.plusHours(6)));
        flights.add(createFlight(oneDaysFromNow, oneDaysFromNow.plusHours(2),
                oneDaysFromNow.plusHours(5), oneDaysFromNow.plusHours(6)));

        FilterFlight filter = new WaitingAtLandMoreThanTwoHours();
        List<Flight> answer = filter.filteringRule(flights);

        assertEquals(3, flights.size());
        assertEquals(2, answer.size());
        assertEquals(flights.subList(0, 2).toString(), answer.toString());
    }

    private static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}
