package com.getitcheap.API;

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
import java.nio.file.Files;

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
			File sqlSetup = ResourceUtils.getFile("classpath:Database/create.sql");
			String fileContents =  Files.readString(sqlSetup.toPath());
			for (String query : fileContents.split(";")) {
				jdbcTemplate.execute(query);
			}
		} catch (Exception e) {
			logger.error(" Error in Setting up database \n"+e.getMessage());
		}
	}
}
