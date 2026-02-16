package com.ey.capstone.bookmyconsultation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.enums.Speciality;

import java.util.List;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, String> {

	List<Doctor> findBySpecialityOrderByRatingDesc(Speciality speciality);

	List<Doctor> findAllByOrderByRatingDesc();
}
