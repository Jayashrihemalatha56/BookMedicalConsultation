package com.ey.capstone.bookmyconsultation.controller;

import com.ey.capstone.bookmyconsultation.entity.Rating;
import com.ey.capstone.bookmyconsultation.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingsController {

    @Autowired
    private RatingsService ratingsService;

    @PostMapping("/ratings")
    public ResponseEntity<String> submitRatings(@RequestBody Rating rating) { // <-- unescaped
        ratingsService.submitRatings(rating);
        return ResponseEntity.ok("Rating submitted successfully");
    }
}