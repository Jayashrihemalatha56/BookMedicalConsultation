package com.ey.capstone.bookmyconsultation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ey.capstone.bookmyconsultation.entity.Address;
@Repository
public interface AddressRepository extends CrudRepository<Address, Long>{
}
//mark it as repository
//create an interface AddressRepository that extends CrudRepository
