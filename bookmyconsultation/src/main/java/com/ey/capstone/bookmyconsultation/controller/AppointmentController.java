package com.ey.capstone.bookmyconsultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.capstone.bookmyconsultation.entity.Appointment;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.SlotUnavailableException;
import com.ey.capstone.bookmyconsultation.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

    @PostMapping("/bookAppointment")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment){
        Appointment savedAppointment = appointmentService.bookAppointment(appointment);
        return ResponseEntity.status(201).body(savedAppointment);
    }
    @GetMapping("/getAppointment/{appointmentId}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable String appointmentId){
        Appointment response=appointmentService.getAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

	//create a method post method named bookAppointment with return type ReponseEntity
		//method has paramter of type Appointment, use RequestBody Annotation for mapping
	
		//save the appointment details to the database and save the response from the method used
		//return http response using ResponseEntity
	
	
	
	
	//create a get method named getAppointment with return type as ResponseEntity
		//method has appointmentId of type String. Use PathVariable annotation to identity appointment using the parameter defined
		
		//get the appointment details using the appointmentId
		//save the response
		//return the response as an http response
	
	

}