package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.repository.UserAuthTokenRepository;
import com.ey.capstone.bookmyconsultation.service.AuthTokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

public class AuthTokenServiceTests {

    @InjectMocks
    private AuthTokenService authTokenService;

    @Mock
    private UserAuthTokenRepository userAuthTokenRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);  
    }

    @Test
    void testIssueTokenMethod() {

        User user = new User();
        user.setEmailId("abc@mail.com");
        user.setPassword("dummyPassword"); 

        when(userAuthTokenRepository.findByUserEmailId("abc@mail.com"))
                .thenReturn(null);

        // When saving token, just return what is passed
        when(userAuthTokenRepository.save(any(UserAuthToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        authTokenService.issueToken(user);   // call the method
    }

    @Test
    void testValidateTokenMethod() throws Exception {

        User user = new User();
        user.setEmailId("xyz@mail.com");

        UserAuthToken token = new UserAuthToken();
        token.setUser(user);
        token.setExpiresAt(ZonedDateTime.now().plusHours(2)); // valid token
        token.setLogoutAt(null); // not logged out

        when(userAuthTokenRepository.findByAccessToken("token123"))
                .thenReturn(token);

        authTokenService.validateToken("token123");  // call the method
    }
}