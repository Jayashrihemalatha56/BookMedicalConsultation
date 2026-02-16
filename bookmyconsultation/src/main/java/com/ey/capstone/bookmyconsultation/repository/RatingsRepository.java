package com.ey.capstone.bookmyconsultation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ey.capstone.bookmyconsultation.entity.Rating;

import java.util.List;




//mark it as repository
//create an interface RatingsRepository that extends CrudRepository
	//create a method findByDoctorId that returns a list of type Rating
	//define method parameter doctorId of type String