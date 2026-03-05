package com.phantask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;

@SpringBootTest
@Disabled("Fails in CI because external services are not available")
class PhanTaskApplicationTests {

	@Test
	void contextLoads() {
	}

}
