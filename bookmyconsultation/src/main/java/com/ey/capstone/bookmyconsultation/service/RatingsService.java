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
 
    // @Autowired
    // private ApplicationEventPublisher publisher;
 
    @Autowired
    private RatingsRepository ratingsRepository;
 
    @Autowired
    private DoctorRepository doctorRepository;
 
   
    //create a method name submitRatings with void return type and parameter of type Rating
        //set a UUID for the rating
        //save the rating to the database
        //get the doctor id from the rating object
        //find that specific doctor with the using doctor id
        //modify the average rating for that specific doctor by including the new rating
        //save the doctor object to the database
   
    public void submitRatings(Rating rating) {
        // Ensure rating id
        if (rating.getId() == null || rating.getId().trim().isEmpty()) {
            rating.setId(UUID.randomUUID().toString());
        }
 
        // Save rating
        ratingsRepository.save(rating);
 
        // Update doctor's average rating
        String doctorId = rating.getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(ResourceUnAvailableException::new);
 
        List<Rating> all = ratingsRepository.findByDoctorId(doctorId);
        double avg = all.stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
 
        doctor.setRating(avg);
        doctorRepository.save(doctor);
    }
}
 
 
 
 
 