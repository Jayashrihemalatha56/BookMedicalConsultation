package com.ey.capstone.bookmyconsultation.servlet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
import com.ey.capstone.bookmyconsultation.enums.Role;
import com.ey.capstone.bookmyconsultation.exception.AuthorizationFailedException;
import com.ey.capstone.bookmyconsultation.exception.RestErrorCode;
import com.ey.capstone.bookmyconsultation.exception.UnauthorizedException;
import com.ey.capstone.bookmyconsultation.provider.BearerAuthDecoder;
import com.ey.capstone.bookmyconsultation.service.AuthTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ey.capstone.bookmyconsultation.constants.ResourceConstants.BASIC_AUTH_PREFIX;

import java.io.IOException;

@Component
public class AuthFilter extends ApiFilter {

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public void doFilter(HttpServletRequest servletRequest,
                         HttpServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        if (servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        final String pathInfo = servletRequest.getRequestURI();

        // Public endpoints
        if (pathInfo.contains("/users/register")
                || pathInfo.contains("/auth/login")
                || pathInfo.contains("/actuator")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        final String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorization)) {
            throw new UnauthorizedException(RestErrorCode.ATH_001);
        }

        if (pathInfo.contains("/auth/login") && !authorization.startsWith(BASIC_AUTH_PREFIX)) {
            throw new UnauthorizedException(RestErrorCode.ATH_002);
        }

        if (!pathInfo.contains("/auth/login")) {

            final String accessToken = new BearerAuthDecoder(authorization).getAccessToken();

            try {
                final UserAuthToken userAuthTokenEntity =
                        authTokenService.validateToken(accessToken);

                User user = userAuthTokenEntity.getUser();
                Role role = user.getRole();
                String method = servletRequest.getMethod();

                // ADMIN only: Add Doctor
                if (pathInfo.equals("/doctors") && method.equalsIgnoreCase("POST")) {
                    if (role != Role.ADMIN) {
                        servletResponse.sendError(
                                HttpStatus.FORBIDDEN.value(),
                                "Only ADMIN can register doctors");
                        return;
                    }
                }

                // ADMIN only: View All Users
                if (pathInfo.equals("/users") && method.equalsIgnoreCase("GET")) {
                    if (role != Role.ADMIN) {
                        servletResponse.sendError(
                                HttpStatus.FORBIDDEN.value(),
                                "Only ADMIN can view users");
                        return;
                    }
                }

                servletRequest.setAttribute(HttpHeaders.AUTHORIZATION,
                        user.getEmailId());

            } catch (AuthorizationFailedException e) {
                servletResponse.sendError(
                        HttpStatus.UNAUTHORIZED.value(),
                        e.getMessage());
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}