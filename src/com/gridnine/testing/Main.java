package com.gridnine.testing;

import java.util.List;

public class Main {
    /**
     * Output of the filter result
     */
    public static void printFlight(List<Flight> flights, FilterFlight filter) {
        List<Flight> filteredList = filter.filteringRule(flights);
        StringBuilder print = new StringBuilder();

        filteredList.forEach(f -> print.append(f).append('\n'));
        print.append("//////////");
        System.out.println(print);
    }

    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("Filter the departure to the current time:");
        printFlight(flights, new DepartureBeforeCurrentTime());

        System.out.println("Arrival date before departure date:");
        printFlight(flights, new ArrivalDateBeforeDepartureDate());

        System.out.println("The total time spent on earth exceeds two hours:");
        printFlight(flights, new WaitingAtLandMoreThanTwoHours());
    }
}