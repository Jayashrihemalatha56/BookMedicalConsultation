package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.exception.AuthenticationFailedException;
import com.ey.capstone.bookmyconsultation.model.AuthorizedUser;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.service.AuthTokenService;
import com.ey.capstone.bookmyconsultation.service.AuthenticationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testAuthenticateSuccess() throws Exception {

        User user = new User();
        user.setEmailId("abc@example.com");
        user.setSalt("salt123");
        user.setPassword("hashed123");

        when(userRepository.findByEmailId("abc@example.com")).thenReturn(user);
        when(passwordCryptographyProvider.encrypt("mypassword", "salt123")).thenReturn("hashed123");

        UserAuthToken token = new UserAuthToken();
        token.setAccessToken("token123");

        when(authTokenService.issueToken(user)).thenReturn(token);

        AuthorizedUser result = authenticationService.authenticate("abc@example.com", "mypassword");

        assertEquals("abc@example.com", result.getId());
        assertEquals("token123", result.getAccessToken());
    }

    @Test
    void testAuthenticateFailure() {

        User user = new User();
        user.setSalt("salt");
        user.setPassword("correct");

        when(userRepository.findByEmailId("abc@example.com")).thenReturn(user);
        when(passwordCryptographyProvider.encrypt("wrong", "salt")).thenReturn("wronghash");

        assertThrows(AuthenticationFailedException.class, () -> {
            authenticationService.authenticate("abc@example.com", "wrong");
        });
    }
}