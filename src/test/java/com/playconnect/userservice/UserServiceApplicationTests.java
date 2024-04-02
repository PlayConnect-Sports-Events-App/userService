package com.playconnect.userservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceApplicationTests {

	@BeforeAll
	public static void setup() {
		System.setProperty("JWT_SECRET_KEY", "your_test_secret_key_here");
	}

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
