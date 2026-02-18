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

    
    @Transactional
    public void submitRatings(Rating rating) {

        
        if (rating == null) {
            throw new IllegalArgumentException("Rating payload cannot be null");
        }
        if (rating.getDoctorId() == null || rating.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("doctorId cannot be null or empty");
        }
        if (rating.getRating() == null) {
            throw new IllegalArgumentException("rating value cannot be null");
        }

       
        if (rating.getRatingId() == null || rating.getRatingId().trim().isEmpty()) {
            rating.setRatingId(UUID.randomUUID().toString());
        }

        Doctor doctor = doctorRepository.findById(rating.getDoctorId())
                .orElseThrow(ResourceUnAvailableException::new);

       
        ratingsRepository.save(rating);

        
        List<Rating> ratings = ratingsRepository.findByDoctorId(doctor.getId());

       
        DoubleSummaryStatistics stats = ratings.stream()
                .map(Rating::getRating)
                .filter(v -> v != null)
                .mapToDouble(Integer::doubleValue)
                .summaryStatistics();

        double average = stats.getCount() == 0 ? 0.0 : stats.getAverage();

        
        doctor.setRating(average);
        doctorRepository.save(doctor);
    }
}
 
 
 
