package com.ey.capstone.bookmyconsultation.servicetests;

import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.enums.UserAuthTokenStatus;
import com.ey.capstone.bookmyconsultation.service.UserAuthTokenVerifier;
import com.ey.capstone.bookmyconsultation.util.DateTimeProvider;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthTokenVerifierTest {

    @Test
    void shouldReturnNotFound_WhenTokenIsNull() {
        UserAuthTokenVerifier verifier = new UserAuthTokenVerifier(null);

        assertTrue(verifier.isNotFound());
        assertEquals(UserAuthTokenStatus.NOT_FOUND, verifier.getStatus());
    }

   
    @Test
    void shouldReturnLoggedOut_WhenLogoutAtIsNotNull() {
        UserAuthToken token = new UserAuthToken();
        token.setLogoutAt(ZonedDateTime.now()); 

        UserAuthTokenVerifier verifier = new UserAuthTokenVerifier(token);

        assertTrue(verifier.hasLoggedOut());
        assertEquals(UserAuthTokenStatus.LOGGED_OUT, verifier.getStatus());
    }

   
    @Test
    void shouldReturnExpired_WhenExpiresAtIsInPast() {
        UserAuthToken token = new UserAuthToken();
        token.setExpiresAt(ZonedDateTime.now().minusHours(1)); // expired one hour ago

        UserAuthTokenVerifier verifier = new UserAuthTokenVerifier(token);

        assertTrue(verifier.hasExpired());
        assertEquals(UserAuthTokenStatus.EXPIRED, verifier.getStatus());
    }

   
    @Test
    void shouldReturnActive_WhenTokenIsValid() {
        UserAuthToken token = new UserAuthToken();
        token.setExpiresAt(ZonedDateTime.now().plusHours(5)); // valid
        token.setLogoutAt(null); // not logged out

        UserAuthTokenVerifier verifier = new UserAuthTokenVerifier(token);

        assertTrue(verifier.isActive());
        assertEquals(UserAuthTokenStatus.ACTIVE, verifier.getStatus());
    }

    
    @Test
    void shouldReturnExpired_WhenExpiresAtIsNow() {
        UserAuthToken token = new UserAuthToken();
        token.setExpiresAt(DateTimeProvider.currentProgramTime()); // equals now

        UserAuthTokenVerifier verifier = new UserAuthTokenVerifier(token);

        assertTrue(verifier.hasExpired());
        assertEquals(UserAuthTokenStatus.EXPIRED, verifier.getStatus());
    }
}