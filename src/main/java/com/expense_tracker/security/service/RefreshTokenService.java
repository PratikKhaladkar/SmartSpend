package com.expense_tracker.security.service;

import com.expense_tracker.repositories.UserRepo;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.expense_tracker.Exceptions.InvalidTokenException;
import com.expense_tracker.Exceptions.ReusedTokenException;
import com.expense_tracker.entities.RefreshToken;
import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.RefreshTokenRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfigurationSource;

@Service
public class RefreshTokenService {

	private final UserRepo userRepo;
	private final RefreshTokenRepo refreshTokenRepo;
	@Value("${app.cookie.secure:false}")
	private boolean secureCookie;
	public RefreshTokenService(UserRepo userRepo, RefreshTokenRepo refreshTokenRepo) {
		super();
		this.userRepo = userRepo;
		this.refreshTokenRepo = refreshTokenRepo;
	}

	public ResponseCookie generateRefreshToken(Authentication authentication, HttpServletRequest request) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		User authenticatedUser = userRepo.findByEmail(userDetails.getUsername()).orElseThrow();

		RefreshToken refreshToken = new RefreshToken(authenticatedUser);

		RefreshToken savedToken = refreshTokenRepo.save(refreshToken);

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", savedToken.getToken()).httpOnly(true)
				.secure(secureCookie).path(request.getContextPath() + "/auth").maxAge(7 * 24 * 60 * 60).build();

		return refreshCookie;
	}

	public Boolean isExpired(RefreshToken refreshToken) {

		if (refreshToken.getExpiry().isAfter(Instant.now())) {

			return false;
		}

		return true;
	}

	@Transactional
	public RefreshToken verifyAndGenerateRoataionalRefreshtoken(String refreshToken) {

		RefreshToken refreshtokenfromdb = refreshTokenRepo.findByToken(refreshToken)
				.orElseThrow(() -> new InvalidTokenException("provided token is invalid"));

		if (refreshtokenfromdb.isConsumed()) {

			throw new ReusedTokenException("Token is alredy consumed");
		}
		if (isExpired(refreshtokenfromdb)) {

			throw new InvalidTokenException("Refresh Token has Expired");
		}

		RefreshToken rotationalToken = new RefreshToken(refreshtokenfromdb.getUser());

		refreshtokenfromdb.setConsumed(true);

		refreshTokenRepo.save(refreshtokenfromdb);

		return refreshTokenRepo.save(rotationalToken);

	}

	public String extractRefreshToken(HttpServletRequest request) {

		if (request.getCookies() == null) {

			return null;
		}

		for (Cookie cookie : request.getCookies()) {

			if (cookie.getName().equals("refreshToken")) {

				return cookie.getValue();
			}
		}

		return null;
	}

	@Transactional
	public void revokeRefreshToken(String refreshToken) {

		RefreshToken refreshTokenfromdb = refreshTokenRepo.findByToken(refreshToken).orElseThrow();

		refreshTokenfromdb.setExpiry(Instant.now());

		refreshTokenRepo.save(refreshTokenfromdb);

	}
}
