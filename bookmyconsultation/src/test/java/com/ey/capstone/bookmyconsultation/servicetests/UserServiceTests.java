package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.exception.ResourceUnAvailableException;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Optional;

public class UserServiceTests {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordCryptographyProvider passwordProvider;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);

        // inject password provider manually
        Field field = UserService.class.getDeclaredField("passwordCryptographyProvider");
        field.setAccessible(true);
        field.set(userService, passwordProvider);
    }

    @Test
    void testRegisterSuccess() throws InvalidInputException {

        User user = new User();
        user.setEmailId("abc@example.com");
        user.setPassword("12345");
        user.setMobile("9999999999");
        user.setFirstName("AAA");
        user.setLastName("BBB");

        when(passwordProvider.encrypt("12345"))
                .thenReturn(new String[]{"salt123", "hashed123"});

        userService.register(user);

        assertEquals("salt123", user.getSalt());
        assertEquals("hashed123", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testGetUserNotFound() {

        when(userRepository.findById("xyz")).thenReturn(Optional.empty());

        assertThrows(ResourceUnAvailableException.class, () -> {
            userService.getUser("xyz");
        });
    }
}