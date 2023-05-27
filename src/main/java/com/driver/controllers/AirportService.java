package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportService {
    private AirportRepository airportRepository = new AirportRepository();

    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        List<Airport> airportList = airportRepository.getAllAirports();
        int maxTerminals = Integer.MIN_VALUE;
        String airportName = "";
        for(Airport airport : airportList) {
            if(airport.getNoOfTerminals() > maxTerminals) {
                maxTerminals = airport.getNoOfTerminals();
                airportName = airport.getAirportName();
            } else if(airport.getNoOfTerminals() == maxTerminals && airport.getAirportName().compareTo(airportName) < 0) {
                airportName = airport.getAirportName();
            }
        }
        return airportName;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flightList = airportRepository.getAllFlights();
        double shortestFlightDuration = Double.MAX_VALUE;
        for(Flight flight : flightList) {
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity) && shortestFlightDuration > flight.getDuration()) {
                shortestFlightDuration = flight.getDuration();
            }
        }
        return shortestFlightDuration == Double.MAX_VALUE ? -1 : shortestFlightDuration;
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"
        Flight flight = airportRepository.getFlightById(flightId);
        int maxCapacity = flight.getMaxCapacity();
        List<Integer> list= new ArrayList<>();
        if(airportRepository.isValid(flightId)){
            list = airportRepository.getPassengers(flightId);
        }
        int capacity=list.size();
        if(capacity==maxCapacity) return "FAILURE";
        else if(list.contains(passengerId)) return "FAILURE";
        int fare=calculateFare(flightId);
        airportRepository.addPayment(passengerId,fare);
        airportRepository.updateRevenue(flightId, fare);
        list.add(passengerId);

        airportRepository.addflightWithPassenger(flightId, list);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        List<Integer> list = airportRepository.getPassengers(flightId);
        if(list.contains(passengerId)){
            list.remove(passengerId);
            int fare = airportRepository.getFareFromPayment(passengerId);
            airportRepository.removePassengerFromPayment(passengerId);
            int revenue = airportRepository.getRevenueFromRevanue(flightId);
            airportRepository.addRevenue(flightId, revenue, fare);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        return airportRepository.getNumberOfPeopleOn(date, airportName);
    }

    public int calculateFare(Integer flightId) {
        return airportRepository.calculateFare(flightId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public String getAirportName(Integer flightId) {
        return airportRepository.getAirportNmae(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }
}