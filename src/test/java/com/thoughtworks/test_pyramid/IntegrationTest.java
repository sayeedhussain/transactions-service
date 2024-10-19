package com.thoughtworks.test_pyramid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderListener orderListener;

    @Test
    public void testOrderProcessing() {
    }
}
