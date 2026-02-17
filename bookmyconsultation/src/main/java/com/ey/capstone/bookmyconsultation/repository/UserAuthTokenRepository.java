package com.ey.capstone.bookmyconsultation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;

import javax.validation.constraints.NotNull;

@Repository
public interface UserAuthTokenRepository extends CrudRepository<UserAuthToken, Long> {

	UserAuthToken findByUserEmailId(@NotNull String userId);

	UserAuthToken findByAccessToken(String token);

}
