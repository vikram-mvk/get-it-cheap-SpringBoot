package com.getitcheap.API;

import com.amazonaws.services.medialive.model.InputClippingSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class GetItCheapApiApplication implements ApplicationRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public static void main(String[] args) {
		SpringApplication.run(GetItCheapApiApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) {
		try {
			Resource resource = new ClassPathResource("Database/create.sql");
			InputStream fileInputStream = resource.getInputStream();
			Scanner scanner = new Scanner(fileInputStream);
			scanner.useDelimiter(";");
			while (scanner.hasNext()) {
				jdbcTemplate.execute(scanner.next());
			}

		} catch (Exception e) {
			logger.error(" Error in Setting up database \n"+e.getMessage());
		}
	}
}
