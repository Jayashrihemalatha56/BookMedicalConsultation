package com.ey.capstone.bookmyconsultation.repository;

import com.ey.capstone.bookmyconsultation.entity.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends CrudRepository<Rating, String> {  // <-- unescaped
    List<Rating> findByDoctorId(String doctorId);                            // <-- unescaped
}