package com.ey.capstone.bookmyconsultation.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ey.capstone.bookmyconsultation.entity.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, String> {

	public List<Appointment> findByUserId(String userId);

	public Appointment findByDoctorIdAndTimeSlotAndAppointmentDate(String doctorId, String timeSlot, String appointmentDate);

}
