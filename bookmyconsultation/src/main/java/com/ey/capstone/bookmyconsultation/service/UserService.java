package com.ey.capstone.bookmyconsultation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.enums.Role;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.util.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public User register(User user) throws InvalidInputException {
        ValidationUtils.validate(user);
        user.setCreatedDate(LocalDate.now().toString());
        user.setRole(Role.USER);
        encryptPassword(user);
        return userRepository.save(user);
    }

    public User createUser(User user) throws InvalidInputException {
        ValidationUtils.validate(user);
        user.setCreatedDate(LocalDate.now().toString());
        user.setRole(Role.USER);
        encryptPassword(user);
        return userRepository.save(user);
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(ResourceUnAvailableException::new);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void encryptPassword(final User newUser) {
        String password = newUser.getPassword();
        final String[] encryptedData = passwordCryptographyProvider.encrypt(password);
        newUser.setSalt(encryptedData[0]);
        newUser.setPassword(encryptedData[1]);
    }
}