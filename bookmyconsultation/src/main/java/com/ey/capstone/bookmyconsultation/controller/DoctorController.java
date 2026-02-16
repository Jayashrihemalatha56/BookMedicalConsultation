package com.ey.capstone.bookmyconsultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.enums.Speciality;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.model.TimeSlot;
import com.ey.capstone.bookmyconsultation.service.DoctorService;
import com.ey.capstone.bookmyconsultation.util.ValidationUtils;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

	@Autowired
	private DoctorService service;

	@GetMapping("/{id}")
	public ResponseEntity<Doctor> getDoctorDetails(@PathVariable String id) {
		return ResponseEntity.ok(service.getDoctor(id));
	}

	@GetMapping
	public ResponseEntity<List<Doctor>> getAllDoctors(@RequestParam(value = "speciality", required = false) String speciality) {
		return ResponseEntity.ok(service.getAllDoctorsWithFilters(speciality));
	}

	@PostMapping
	public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) throws InvalidInputException {
		return ResponseEntity.ok(service.register(doctor));
	}


	@GetMapping("/speciality")
	public ResponseEntity<List<String>> getSpeciality() {
		return ResponseEntity.ok(Stream.of(Speciality.values())
				.map(Enum::name)
				.collect(Collectors.toList()));
	}

	@GetMapping("/{doctorId}/timeSlots")
	public ResponseEntity<TimeSlot> getTimeSlots(@RequestParam(value = "date", required = false) String date,
	                                             @PathVariable String doctorId) {
		if (!ValidationUtils.isValid(date)) {
			throw new InvalidParameterException("Not a valid date");
		}

		if (service.getDoctor(doctorId) == null) throw new InvalidParameterException("Not a valid doctor id");

		return ResponseEntity.ok(service.getTimeSlots(doctorId, date));
	}


}