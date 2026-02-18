package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.Address;
import com.ey.capstone.bookmyconsultation.entity.Appointment;
import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.enums.Speciality;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.model.TimeSlot;
import com.ey.capstone.bookmyconsultation.repository.AddressRepository;
import com.ey.capstone.bookmyconsultation.repository.AppointmentRepository;
import com.ey.capstone.bookmyconsultation.repository.DoctorRepository;
import com.ey.capstone.bookmyconsultation.service.DoctorService;

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
class DoctorServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;
    private Address address;

    @BeforeEach
    void setup() {
        address = new Address();
        address.setAddressLine1("Line 1");
        address.setCity("City");

        doctor = new Doctor();
        doctor.setId("DOC-1");
        doctor.setFirstName("John");
        doctor.setSpeciality(Speciality.GENERAL_PHYSICIAN);
        doctor.setAddress(address);
    }


    @Test
    void register_ShouldFail_WhenAddressMissing() {
        Doctor d = new Doctor();
        d.setFirstName("Test");
        d.setAddress(null);

        assertThrows(InvalidInputException.class, () -> doctorService.register(d));

        verifyNoInteractions(addressRepository);
        verifyNoInteractions(doctorRepository);
    }

    @Test
    void register_ShouldPass_WhenValidInput() throws Exception {
        when(addressRepository.save(address)).thenReturn(address);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor saved = doctorService.register(doctor);

        assertNotNull(saved.getId());
        verify(addressRepository).save(address);
        verify(doctorRepository).save(doctor);
    }
    @Test
    void getDoctor_ShouldFail_WhenNotFound() {
        when(doctorRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceUnAvailableException.class,
                () -> doctorService.getDoctor("UNKNOWN"));
    }

    @Test
    void getDoctor_ShouldPass_WhenFound() throws Exception {
        when(doctorRepository.findById("DOC-1"))
                .thenReturn(Optional.of(doctor));

        Doctor result = doctorService.getDoctor("DOC-1");

        assertEquals("DOC-1", result.getId());
        verify(doctorRepository).findById("DOC-1");
    }


    @Test
    void getAllDoctorsWithFilters_ShouldReturnFilteredDoctors() {
        when(doctorRepository.findBySpecialityOrderByRatingDesc(Speciality.DENTIST))
                .thenReturn(List.of(doctor));

        List<Doctor> result = doctorService.getAllDoctorsWithFilters("DENTIST");

        assertEquals(1, result.size());
        verify(doctorRepository).findBySpecialityOrderByRatingDesc(Speciality.DENTIST);
    }

    @Test
    void getAllDoctorsWithFilters_NoFilter_ShouldReturnAll() {
        when(doctorRepository.findAllByOrderByRatingDesc())
                .thenReturn(Arrays.asList(doctor));

        List<Doctor> result = doctorService.getAllDoctorsWithFilters(null);

        assertEquals(1, result.size());
        verify(doctorRepository).findAllByOrderByRatingDesc();
    }

}
