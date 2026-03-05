package com.phantask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Fails in CI because external services are not available")
class PhanTaskApplicationTests {

	@Test
	void contextLoads() {
	}

}
