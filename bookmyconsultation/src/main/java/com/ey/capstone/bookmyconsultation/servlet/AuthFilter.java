package com.ey.capstone.bookmyconsultation.servlet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ey.capstone.bookmyconsultation.entity.UserAuthToken;
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
	public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		if (servletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		final String pathInfo = servletRequest.getRequestURI();
		if (!pathInfo.contains("register") && !pathInfo.contains("actuator") && !pathInfo.contains("doctors")) {
			final String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			if (StringUtils.isEmpty(authorization)) {
				throw new UnauthorizedException(RestErrorCode.ATH_001);
			}

			if (pathInfo.contains("login") && !authorization.startsWith(BASIC_AUTH_PREFIX)) {
				throw new UnauthorizedException(RestErrorCode.ATH_002);
			}

			if (!pathInfo.contains("login")) {
				final String accessToken = new BearerAuthDecoder(authorization).getAccessToken();
				try {
					final UserAuthToken userAuthTokenEntity = authTokenService.validateToken(accessToken);
					servletRequest.setAttribute(HttpHeaders.AUTHORIZATION, userAuthTokenEntity.getUser().getEmailId());
				} catch (AuthorizationFailedException e) {
					servletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
					return;
				}
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

}
