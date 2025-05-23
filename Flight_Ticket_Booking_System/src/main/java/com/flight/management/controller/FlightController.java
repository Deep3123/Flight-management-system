package com.flight.management.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.management.proxy.FlightProxy;
import com.flight.management.proxy.FlightSearchProxy;
import com.flight.management.proxy.Response;
import com.flight.management.service.FlightService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/flight")
public class FlightController {

	@Autowired
	private FlightService service;

	@PostMapping("/add-flight-details")
	public ResponseEntity<?> addFlightDetails(@Valid @RequestBody FlightProxy flightProxy,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		String result = service.addFlightDetails(flightProxy);
		if (result.contains("Flight already exist with given flight number.")) {
			return new ResponseEntity<>(new Response(result, HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new Response(result, HttpStatus.CREATED.toString()), HttpStatus.CREATED);
	}

	@GetMapping("/get-all-flights-details")
	public ResponseEntity<?> getAllFlightsDetails() {
		List<FlightProxy> list = service.getAllFlightsDetails();

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("No data found to display currently!!", HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/get-flights-details-by-flight-number/{flightNumber}")
	public ResponseEntity<?> getFlightDetailsByFlightNumber(@Valid @PathVariable("flightNumber") String flightNumber) {
		FlightProxy flight = service.getFlightDetailsByFlightNumber(flightNumber);

		if (flight != null)
			return new ResponseEntity<>(flight, HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("Flight details not found with given flightnumber, please verify the flightnumber!!",
							HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@PostMapping("/update-flight-details")
	public ResponseEntity<?> updateFlightDetails(@Valid @RequestBody FlightProxy flightProxy,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		String s = service.updateFlightDetails(flightProxy);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("Flight details not found with given flightnumber, please verify the flightnumber!!",
							HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/delete-flight-details/{flightNumber}")
	public ResponseEntity<?> deleteFlightDetails(@Valid @PathVariable("flightNumber") String flightNumber) {
		String s = service.deleteFlightDetails(flightNumber);

		if (s != null && !s.isEmpty())
			return new ResponseEntity<>(new Response(s, HttpStatus.OK.toString()), HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("Flight details not found with given flightnumber, please verify the flightnumber!!",
							HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

	@PostMapping("/get-flight-details-by-departure-and-arrival")
	public ResponseEntity<?> getFlightDetailsByUserDetails(@Valid @RequestBody FlightSearchProxy flightSearchProxy,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Extract error messages
			List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(new Response(errors.toString(), HttpStatus.BAD_REQUEST.toString()),
					HttpStatus.BAD_REQUEST);
		}

		List<FlightProxy> list = service.getFlightDetailsByUserDetails(flightSearchProxy);

		if (list != null && !list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);

		else
			return new ResponseEntity<>(
					new Response("No flight details found based on given departure and arrival station!!",
							HttpStatus.NOT_FOUND.toString()),
					HttpStatus.NOT_FOUND);
	}

}
