package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.entity.Rating;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.repository.DoctorRepository;
import com.ey.capstone.bookmyconsultation.repository.RatingsRepository;
import com.ey.capstone.bookmyconsultation.service.RatingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingsServiceTest {

    @Mock
    private RatingsRepository ratingsRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private RatingsService ratingsService;

    private Doctor doctor;

    @BeforeEach
    void setup() {
        doctor = new Doctor();
        doctor.setId("DOC-1");
        doctor.setRating(3.0);
    }

   
    @Test
    void submitRatings_ShouldFail_WhenRatingIsNull() {

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ratingsService.submitRatings(null));

        assertEquals("Rating payload cannot be null", ex.getMessage());
        verifyNoInteractions(ratingsRepository);
    }
  
    @Test
    void submitRatings_ShouldFail_WhenDoctorIdMissing() {

        Rating rating = Rating.builder()
                .rating(4)
                .comments("Nice")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ratingsService.submitRatings(rating));

        assertEquals("doctorId cannot be null or empty", ex.getMessage());
        verifyNoInteractions(ratingsRepository);
    }

   
    @Test
    void submitRatings_ShouldFail_WhenRatingValueNull() {

        Rating rating = Rating.builder()
                .doctorId("DOC-1")
                .comments("OK")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ratingsService.submitRatings(rating));

        assertEquals("rating value cannot be null", ex.getMessage());
    }

   
    @Test
    void submitRatings_ShouldFail_WhenDoctorNotFound() {

        Rating rating = Rating.builder()
                .doctorId("UNKNOWN")
                .rating(3)
                .appointmentId("APPT-1")
                .build();

        when(doctorRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceUnAvailableException.class,
                () -> ratingsService.submitRatings(rating));

        verify(doctorRepository).findById("UNKNOWN");
    }

    
    @Test
    void submitRatings_ShouldPass_WhenValidRating() {

        Rating rating = Rating.builder()
                .doctorId("DOC-1")
                .appointmentId("APPT-1")
                .rating(4)
                .comments("Good")
                .build();

        when(doctorRepository.findById("DOC-1"))
                .thenReturn(Optional.of(doctor));

        List<Rating> existingRatings = Arrays.asList(
                Rating.builder().rating(5).doctorId("DOC-1").build(),
                Rating.builder().rating(3).doctorId("DOC-1").build()
        );

        when(ratingsRepository.findByDoctorId("DOC-1"))
                .thenReturn(existingRatings);

        ratingsService.submitRatings(rating);

        assertNotNull(rating.getRatingId());
        verify(ratingsRepository).save(rating);

        // Average = (5+3) / 2 = 4.0
        verify(doctorRepository).save(argThat(updated ->
                updated.getRating() == 4.0
        ));
    }

   
    @Test
    void submitRatings_ShouldSetAverageZero_WhenNoRatingsExist() {

        Rating rating = Rating.builder()
                .doctorId("DOC-1")
                .appointmentId("APPT-2")
                .rating(2)
                .comments("OK")
                .build();

        when(doctorRepository.findById("DOC-1"))
                .thenReturn(Optional.of(doctor));

        when(ratingsRepository.findByDoctorId("DOC-1"))
                .thenReturn(Arrays.asList());

        ratingsService.submitRatings(rating);

        verify(doctorRepository).save(argThat(updated ->
                updated.getRating() == 0.0
        ));
    }
}