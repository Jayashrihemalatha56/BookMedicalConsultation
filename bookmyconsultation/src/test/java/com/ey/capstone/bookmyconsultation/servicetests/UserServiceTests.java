package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceTests {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordCryptographyProvider passwordProvider;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);   // <-- works without MockitoExtension
        userService = new UserService(userRepository);

        Field field = UserService.class.getDeclaredField("passwordCryptographyProvider");
        field.setAccessible(true);
        field.set(userService, passwordProvider);
    }

    @Test
    void testRegisterMethod() throws Exception {
        User u = new User();
        u.setEmailId("test@mail.com");
        u.setPassword("123");
        u.setMobile("9999999999");
        u.setFirstName("Alpha");
        u.setLastName("Beta");
        u.setDob("1990-01-01");

        when(passwordProvider.encrypt("123"))
            .thenReturn(new String[]{"salt", "hash"});

        when(userRepository.save(u)).thenReturn(u);

        userService.register(u);
    }

    @Test
    void testCreateUserMethod() throws Exception {
        User u = new User();
        u.setEmailId("placeholder@mail.com");
        u.setMobile("9999999999");
        u.setFirstName("Alpha");
        u.setLastName("Beta");
        u.setDob("1990-01-01");
        u.setPassword("pass");

        when(passwordProvider.encrypt("pass"))
            .thenReturn(new String[]{"salt2", "hash2"});

        when(userRepository.save(u)).thenReturn(u);

        userService.createUser(u);
    }

    @Test
    void testGetUserMethod() {
        User user = new User();
        user.setEmailId("abc@mail.com");

        when(userRepository.findById("abc")).thenReturn(Optional.of(user));

        userService.getUser("abc");
    }

    @Test
    void testGetAllUsersMethod() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        userService.getAllUsers();
    }
}