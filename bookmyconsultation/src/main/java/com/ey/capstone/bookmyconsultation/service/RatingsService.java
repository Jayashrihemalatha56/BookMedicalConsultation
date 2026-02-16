package com.ey.capstone.bookmyconsultation.service;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.entity.Rating;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.repository.DoctorRepository;
import com.ey.capstone.bookmyconsultation.repository.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingsService {

    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public void submitRatings(Rating rating) {

        // Ensure ratingId exists
        if (rating.getRatingId() == null || rating.getRatingId().trim().isEmpty()) {
            rating.setRatingId(UUID.randomUUID().toString());
        }

        // Save the rating
        ratingsRepository.save(rating);

        // Fetch the doctor
        String doctorId = rating.getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(ResourceUnAvailableException::new);

        // Get all ratings for the doctor
        List<Rating> ratings = ratingsRepository.findByDoctorId(doctorId);

        // Calculate new average
        double avg = ratings.stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);

        // Update doctor rating
        doctor.setRating(avg);
        doctorRepository.save(doctor);
    }
}