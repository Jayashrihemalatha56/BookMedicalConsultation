package com.ey.capstone.bookmyconsultation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.capstone.bookmyconsultation.entity.Doctor;
import com.ey.capstone.bookmyconsultation.entity.User;
import com.ey.capstone.bookmyconsultation.exception.InvalidInputException;
import com.ey.capstone.bookmyconsultation.service.AppointmentService;
import com.ey.capstone.bookmyconsultation.service.DoctorService;
import com.ey.capstone.bookmyconsultation.service.UserService;


@RestController
@RequestMapping("/users")
public class UserAdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private DoctorService doctorService;
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) throws InvalidInputException {
 		User registeredUser = userService.register(user);
    	return ResponseEntity.ok(registeredUser);
}

	@GetMapping(path = "/{id}")
	public ResponseEntity<User> getUser(@RequestHeader("authorization") String accessToken,
	                                    @PathVariable("id") final String userUuid) {
		final User User = userService.getUser(userUuid);
		return ResponseEntity.ok(User);
	}
	
	//create a post method named createUser with return type as ResponseEntity
		//define the method parameter user of type User. Set it final. Use @RequestBody for mapping.
		//declare InvalidInputException using throws keyword
		
		//register the user
	
		//return http response with status set to OK
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody final User user) throws InvalidInputException {
		final User createdUser = userService.createUser(user);
		return ResponseEntity.ok(createdUser);
	}
	
	 // -------- ADMIN: ADD DOCTOR --------
    @PostMapping("/admin/doctors")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor)
            throws InvalidInputException {

        Doctor savedDoctor = doctorService.register(doctor);
        return ResponseEntity.ok(savedDoctor);
    }
	
	@GetMapping("/{userId}/appointments")
	public ResponseEntity getAppointmentForUser(@PathVariable("userId") String userId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsForUser(userId));
	}


	   // -------- ADMIN: GET ALL USERS --------
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        users.forEach(user -> {
            user.setPassword(null);
            user.setSalt(null);
        });

        return ResponseEntity.ok(users);
    }

   
}

