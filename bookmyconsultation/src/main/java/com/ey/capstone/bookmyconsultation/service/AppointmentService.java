package com.ey.capstone.bookmyconsultation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.capstone.bookmyconsultation.entity.Appointment;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.exception.SlotUnavailableException;
import com.ey.capstone.bookmyconsultation.repository.AppointmentRepository;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.util.ValidationUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

	
	
	//mark it autowired
	//create an instance of AppointmentRepository called appointmentRepository

	@Autowired
	private UserRepository userRepository;


	//create a method name appointment with the return type of String and parameter of type Appointment
	//declare exceptions 'SlotUnavailableException' and 'InvalidInputException'
		//validate the appointment details using the validate method from ValidationUtils class
		//find if an appointment exists with the same doctor for the same date and time
		//if the appointment exists throw the SlotUnavailableException
		//save the appointment details to the database
		//return the appointment id
	
	


	//create a method getAppointment of type Appointment with a parameter name appointmentId of type String
		//Use the appointmentid to get the appointment details
		//if the appointment exists return the appointment
		//else throw ResourceUnAvailableException
		//tip: use Optional.ofNullable(). Use orElseThrow() method when Optional.ofNullable() throws NULL
	
	public List<Appointment> getAppointmentsForUser(String userId) {
		return Appointment.findByUserId(userId);
	}
}
