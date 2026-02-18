package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.Appointment;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.exception.SlotUnavailableException;
import com.ey.capstone.bookmyconsultation.repository.AppointmentRepository;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.service.AppointmentService;
import com.ey.capstone.bookmyconsultation.util.ValidationUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;

    @BeforeEach
    void setup() {
        appointment = new Appointment();
        appointment.setAppointmentId("A1");
        appointment.setDoctorId("DOC-1");
        appointment.setUserId("USER-1");
        appointment.setTimeSlot("10:00 AM");
        appointment.setAppointmentDate("2026-02-22");
    }

    @Test
    void appointment_ShouldFail_WhenValidationFails() {

        Appointment invalid = new Appointment();
        assertThrows(InvalidInputException.class, () ->
                appointmentService.appointment(invalid)
        );

        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void appointment_ShouldFail_WhenSlotAlreadyBooked() {

        when(appointmentRepository.findByDoctorIdAndTimeSlotAndAppointmentDate(
                "DOC-1", "10:00 AM", "2026-02-22"))
                .thenReturn(new Appointment()); // slot already booked

        assertThrows(SlotUnavailableException.class,
                () -> appointmentService.appointment(appointment));

        verify(appointmentRepository).findByDoctorIdAndTimeSlotAndAppointmentDate(
                "DOC-1", "10:00 AM", "2026-02-22");
    }

    @Test
    void appointment_ShouldPass_WhenSlotAvailable() throws Exception {

        when(appointmentRepository.findByDoctorIdAndTimeSlotAndAppointmentDate(
                anyString(), anyString(), anyString()))
                .thenReturn(null); // slot is free

        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment saved = appointmentService.appointment(appointment);

        assertNotNull(saved);
        assertEquals("A1", saved.getAppointmentId());

        verify(appointmentRepository).save(appointment);
    }

    @Test
    void getAppointment_ShouldFail_WhenNotFound() {

        when(appointmentRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceUnAvailableException.class,
                () -> appointmentService.getAppointment("INVALID"));
    }

    @Test
    void getAppointment_ShouldPass_WhenFound() throws Exception {

        when(appointmentRepository.findById("A1"))
                .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointment("A1");

        assertNotNull(result);
        assertEquals("A1", result.getAppointmentId());
    }

    @Test
    void getAppointmentsForUser_ShouldReturnList() {

        List<Appointment> list = Arrays.asList(appointment);

        when(appointmentRepository.findByUserId("USER-1"))
                .thenReturn(list);

        List<Appointment> result =
                appointmentService.getAppointmentsForUser("USER-1");

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getAppointmentId());
    }
}