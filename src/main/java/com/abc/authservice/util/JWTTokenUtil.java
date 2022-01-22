package com.abc.authservice.util;

import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTTokenUtil {

	// TO DO move secret to config
	private static final String secret = "secret";
	private static final String issuer = "Prabhat";

	public static Algorithm getAlgorithm() {
		return Algorithm.HMAC256(secret.getBytes());
	}

	public static String accessToken(String user, List<String> roles) {

		log.info("Creating access token for User {} , Role {} ", user, roles);

		return JWT.create().withSubject(user).withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(issuer).withClaim("roles", roles).sign(getAlgorithm());

	}

	public static String refreshToken(String user) {

		log.info("Creating refresh token for User {}  ", user);
		return JWT.create().withSubject(user).withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(issuer).sign(getAlgorithm());
	}

	public static String getSubject(String token) {

		JWTVerifier verifier = JWT.require(getAlgorithm()).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT.getSubject();

	}

	public static String[] getClaim(String claimName, String token) {

		JWTVerifier verifier = JWT.require(getAlgorithm()).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT.getClaim(claimName).asArray(String.class);

	}

}
