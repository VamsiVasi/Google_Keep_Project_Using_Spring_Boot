package com.albanero.Google_Keep.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Request.Keep_Request;
import com.albanero.Google_Keep.Service.Keep_Service;

@RestController
@RequestMapping(value = "/keep")
public class Keep_Controller {

	@Autowired
	private Keep_Service keep_Service;

	// Create user and note where user contains only the object id of the notes
	@PostMapping("/create/user-notes")
	public ResponseEntity<User_Keep> createUserKeep(@RequestBody Keep_Request request) {
		return ResponseEntity.ok(keep_Service.createUserKeep(request));
	}

	// Create notes by userName
	@PostMapping("/create/notes/{userName}")
	public ResponseEntity<User_Keep> createNotesKeep(@PathVariable String userName, @RequestBody Keep_Request request)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.createNotesKeep(userName, request));
	}

	// Get all users
	@GetMapping("/get/user-notes")
	public List<User_Keep> getUserKeep() {
		return keep_Service.getUserKeep();
	}

	// Get user details by userName
	@GetMapping("/get/user-notes/{userName}")
	public ResponseEntity<User_Keep> getUserKeepByName(@PathVariable String userName)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.getUserKeepByName(userName));
	}

	// Get all notes of the user by userName
	@GetMapping("/get/notes/{userName}")
	public List<Notes_Keep> getAllNotesKeepByName(@PathVariable String userName) throws ResourceNotFoundException {
		return keep_Service.getAllNotesKeepByName(userName);
	}

	// Get a particular notes of the user by notes name
	@GetMapping("/get/notes/{userName}/{name}")
	public List<Notes_Keep> getNotesKeepByName(@PathVariable String userName, @PathVariable String name)
			throws ResourceNotFoundException {
		return keep_Service.getNotesKeepByName(userName, name);
	}

	// Update user details by userName
	@PutMapping("/put/userdetails/{userName}")
	public ResponseEntity<User_Keep> updateUserKeep(@PathVariable String userName, @RequestBody Keep_Request request)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.updateUserKeep(userName, request));
	}

	// Update a particular notes of the user by notes name
	@PutMapping("/put/notesdetails/{userName}/{name}")
	public ResponseEntity<Notes_Keep> updateNotesKeep(@PathVariable String userName, @PathVariable String name,
			@RequestBody Keep_Request request) throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.updateNotesKeep(userName, name, request));
	}

	// Delete a particular notes of the user by notes name
	@DeleteMapping("/delete/notesdetails/{userName}/{name}")
	public String deleteNotesKeep(@PathVariable String userName, @PathVariable String name)
			throws ResourceNotFoundException {
		keep_Service.deleteNotesKeep(userName, name);
		return userName + ", Your " + name + " notes was successfully removed";
	}

	// Delete user by userName and all the notes of that particular user
	@DeleteMapping("/delete/userdetails/{userName}")
	public String deleteUserKeep(@PathVariable String userName) throws ResourceNotFoundException {
		keep_Service.deleteUserKeep(userName);
		return userName + ", your account and all your notes were successfully removed";
	}
}
