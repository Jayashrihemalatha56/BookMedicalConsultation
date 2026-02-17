package com.ey.capstone.bookmyconsultation.service;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.entity.Rating;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.repository.DoctorRepository;
import com.ey.capstone.bookmyconsultation.repository.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;

@Service
public class RatingsService {

    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Submits a rating for a doctor and updates the doctor's average rating.
     * - Ensures ratingId exists (generate if absent)
     * - Ensures the doctor exists
     * - Saves the rating
     * - Recomputes average rating from all ratings for that doctor
     * - Updates doctor's rating
     */
    @Transactional
    public void submitRatings(Rating rating) {

        // 1) Validate required fields minimally (you can add more if needed)
        if (rating == null) {
            throw new IllegalArgumentException("Rating payload cannot be null");
        }
        if (rating.getDoctorId() == null || rating.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("doctorId cannot be null or empty");
        }
        if (rating.getRating() == null) {
            throw new IllegalArgumentException("rating value cannot be null");
        }

        // 2) Ensure ratingId exists
        if (rating.getRatingId() == null || rating.getRatingId().trim().isEmpty()) {
            rating.setRatingId(UUID.randomUUID().toString());
        }

        // 3) Ensure doctor exists
        Doctor doctor = doctorRepository.findById(rating.getDoctorId())
                .orElseThrow(ResourceUnAvailableException::new);

        // 4) Save the rating
        ratingsRepository.save(rating);

        // 5) Fetch all ratings for this doctor
        List<Rating> ratings = ratingsRepository.findByDoctorId(doctor.getId());

        // 6) Compute new average (ignore null rating entries just in case)
        DoubleSummaryStatistics stats = ratings.stream()
                .map(Rating::getRating)
                .filter(v -> v != null)
                .mapToDouble(Integer::doubleValue)
                .summaryStatistics();

        double average = stats.getCount() == 0 ? 0.0 : stats.getAverage();

        // 7) Update doctor's rating and persist
        doctor.setRating(average);
        doctorRepository.save(doctor);
    }
}