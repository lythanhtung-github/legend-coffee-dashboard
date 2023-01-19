package com.cg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpbLegendCoffeeApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpbLegendCoffeeApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpbLegendCoffeeApiApplication.class, args);
        logger.info("Legend Coffee Application Started........");
    }

}
