package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.exception.AuthorizationFailedException;
import com.ey.capstone.bookmyconsultation.repository.UserAuthTokenRepository;
import com.ey.capstone.bookmyconsultation.service.AuthTokenService;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;

@ExtendWith(MockitoExtension.class)
public class AuthTokenServiceTests {

    @InjectMocks
    private AuthTokenService authTokenService;

    @Mock
    private UserAuthTokenRepository userAuthRepo;

    // SUCCESS CASE
    @Test
    void testValidateTokenSuccess() throws Exception {

        User user = new User();
        user.setEmailId("abc@example.com");

        UserAuthToken token = new UserAuthToken();
        token.setUser(user);
        token.setExpiresAt(ZonedDateTime.now().plusHours(1));

        when(userAuthRepo.findByAccessToken("tok123")).thenReturn(token);

        UserAuthToken result = authTokenService.validateToken("tok123");

        assertEquals("abc@example.com", result.getUser().getEmailId());
    }

    // FAILURE CASE
    @Test
    void testValidateTokenExpired() {

        UserAuthToken expiredToken = new UserAuthToken();
        expiredToken.setExpiresAt(ZonedDateTime.now().minusMinutes(1));

        when(userAuthRepo.findByAccessToken("tok123")).thenReturn(expiredToken);

        assertThrows(AuthorizationFailedException.class, () -> {
            authTokenService.validateToken("tok123");
        });
    }
}