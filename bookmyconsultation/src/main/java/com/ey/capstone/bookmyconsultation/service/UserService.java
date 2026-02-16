package com.ey.capstone.bookmyconsultation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.util.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	private PasswordCryptographyProvider passwordCryptographyProvider;


	public User register(User user) throws InvalidInputException {
		ValidationUtils.validate(user);

		user.setCreatedDate(LocalDate.now().toString());
		encryptPassword(user);
		userRepository.save(user);
		return user;
	}

	public User getUser(String id) {
		return Optional.ofNullable(userRepository.findById(id))
				.get()
				.orElseThrow(ResourceUnAvailableException::new);
	}

	//create a method named getAllUsers that returns a List of type User
		//return all the users from the database
	

	private void encryptPassword(final User newUser) {

		String password = newUser.getPassword();
		final String[] encryptedData = passwordCryptographyProvider.encrypt(password);
		newUser.setSalt(encryptedData[0]);
		newUser.setPassword(encryptedData[1]);
	}
}
