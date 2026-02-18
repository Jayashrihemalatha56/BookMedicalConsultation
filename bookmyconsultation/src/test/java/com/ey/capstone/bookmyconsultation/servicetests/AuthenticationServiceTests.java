package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.provider.PasswordCryptographyProvider;
import com.ey.capstone.bookmyconsultation.repository.UserRepository;
import com.ey.capstone.bookmyconsultation.service.AuthTokenService;
import com.ey.capstone.bookmyconsultation.service.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateMethod() throws Exception {

        User user = new User();
        user.setEmailId("test@mail.com");
        user.setSalt("salt123");
        user.setPassword("encrypted123");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setMobile("9999999999");

        UserAuthToken token = new UserAuthToken();
        token.setAccessToken("token123");

        when(userRepository.findByEmailId("test@mail.com"))
                .thenReturn(user);

        when(passwordCryptographyProvider.encrypt("123", "salt123"))
                .thenReturn("encrypted123");

        when(authTokenService.issueToken(user))
                .thenReturn(token);

        authenticationService.authenticate("test@mail.com", "123");
    }

    @Test
    void testAuthorizedUserMethod() throws Exception {

        User user = new User();
        user.setEmailId("abc@mail.com");
        user.setSalt("saltX");
        user.setPassword("hashX");
        user.setFirstName("Alpha");
        user.setLastName("Beta");
        user.setMobile("8888888888");

        UserAuthToken token = new UserAuthToken();
        token.setAccessToken("xyz123");

        when(userRepository.findByEmailId("abc@mail.com"))
                .thenReturn(user);

        when(passwordCryptographyProvider.encrypt("pass", "saltX"))
                .thenReturn("hashX");

        when(authTokenService.issueToken(user))
                .thenReturn(token);

        authenticationService.authenticate("abc@mail.com", "pass");  
    }
}