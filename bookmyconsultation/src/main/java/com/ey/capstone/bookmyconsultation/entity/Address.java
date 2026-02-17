package com.ey.capstone.bookmyconsultation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Address {
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String postcode;
}
