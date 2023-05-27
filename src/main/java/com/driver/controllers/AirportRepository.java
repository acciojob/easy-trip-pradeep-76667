package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    private Map<String, Airport> airports = new HashMap<>();
    private Map<Integer, Flight> flights = new HashMap<>();
    private Map<Integer, Passenger> passengers = new HashMap<>();
    private Map<Integer, List<Integer>> flightWithPassengerData = new HashMap<>();
    private Map<Integer,Integer> revenueMap= new HashMap<>();
    private Map<Integer,Integer> paymentMap= new HashMap<>();

    public void addAirport(Airport airport) {
        airports.put(airport.getAirportName(), airport);
    }

    public List<Airport> getAllAirports() {
        return new ArrayList<>(airports.values());
    }

    public void addFlight(Flight flight) {
        flights.put(flight.getFlightId(), flight);
    }

    public void addPassenger(Passenger passenger) {
        passengers.put(passenger.getPassengerId(), passenger);
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }

    public Flight getFlightById(Integer flightId) {
        return flights.get(flightId);
    }

    public Passenger getPassengerById(Integer passengerId) {
        return passengers.get(passengerId);
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Airport airport = airports.get(airportName);
        int count = 0;
        if(airport != null){
            City city = airport.getCity();
            for(Flight flight : flights.values()){
                if(date.equals(flight.getFlightDate())){
                    if(city.equals(flight.getToCity()) || city.equals(flight.getFromCity())){
                        Integer flightId = flight.getFlightId();
                        List<Integer> list = flightWithPassengerData.get(flightId);
                        if(list != null){
                            count += list.size();
                        }
                    }
                }
            }
        }
        return count;
    }

    public int calculateFare(Integer flightId) {
        int fare = 3000;
        int alreadyBooked = 0;
        if(flightWithPassengerData.containsKey(flightId))
            alreadyBooked = flightWithPassengerData.get(flightId).size();
        return (fare + (alreadyBooked * 50));
    }

    public boolean isValid(Integer flightId) {
        return flightWithPassengerData.containsKey(flightId);
    }

    public List<Integer> getPassengers(Integer flightId) {
        return flightWithPassengerData.get(flightId);
    }

    public void addPayment(Integer passengerId, int fare) {
        paymentMap.put(passengerId,fare);
    }

    public void updateRevenue(Integer flightId, Integer fare) {
        fare += revenueMap.getOrDefault(flightId,0);
        revenueMap.put(flightId,fare);
    }

    public void addflightWithPassenger(Integer flightId, List<Integer> list) {
        flightWithPassengerData.put(flightId,list);
    }


    public int getFareFromPayment(Integer passengerId) {
        return paymentMap.getOrDefault(passengerId,0);
    }

    public void removePassengerFromPayment(Integer passengerId) {
        paymentMap.remove(passengerId);
    }

    public int getRevenueFromRevanue(Integer flightId) {
        return revenueMap.getOrDefault(flightId,0);
    }

    public void addRevenue(Integer flightId, int revenue, int fare) {
        revenueMap.put(flightId, revenue-fare);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int count=0;
        for(Integer flightId : flightWithPassengerData.keySet()){
            List<Integer> list = flightWithPassengerData.get(flightId);
            if(list.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public String getAirportNmae(Integer flightId) {
        if(!flights.containsKey(flightId)) return null;
        Flight flight= flights.get(flightId);
        City city=flight.getFromCity();
        for (String airportname:airports.keySet()){
            Airport airport=airports.get(airportname);
            if(city.equals(airport.getCity())){
                return airportname;
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        Integer revenue= revenueMap.getOrDefault(flightId,0);
        return revenue;
    }
}