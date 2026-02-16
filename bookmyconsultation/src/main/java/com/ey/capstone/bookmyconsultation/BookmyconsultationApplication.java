package com.ey.capstone.bookmyconsultation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.ey.capstone.bookmyconsultation.config.ApiConfiguration;
import com.ey.capstone.bookmyconsultation.config.WebConfiguration;

@SpringBootApplication
@Import({ApiConfiguration.class, WebConfiguration.class})
public class BookmyconsultationApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookmyconsultationApplication.class, args);
	}

}
