package com.albanero.Google_Keep.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;
import com.albanero.Google_Keep.Request.Keep_Request;

@Service
public class Keep_Service {

	@Autowired
	private User_Keep_Repository user_Keep_Repo;

	@Autowired
	private Notes_Keep_Repository notes_Keep_Repo;

	// Create user and note where user contains only the object id of the notes
	public User_Keep createUserKeep(Keep_Request request) {
		ArrayList<String> ua = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep();
		Notes_Keep notes_Keep = new Notes_Keep();
		BeanUtils.copyProperties(request, notes_Keep);
		notes_Keep_Repo.save(notes_Keep);
		ua.add(notes_Keep.getId());
		BeanUtils.copyProperties(request, user_Keep);
		user_Keep.setNotesIds(ua);
		return user_Keep_Repo.save(user_Keep);
	}

	// Create notes by userName
	public User_Keep createNotesKeep(String userName, Keep_Request request) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> ua = user_Keep.getNotesIds();
		Notes_Keep notes_Keep = new Notes_Keep();
		BeanUtils.copyProperties(request, notes_Keep);
		notes_Keep_Repo.save(notes_Keep);
		ua.add(notes_Keep.getId());
		user_Keep.setNotesIds(ua);
		return user_Keep_Repo.save(user_Keep);
	}

	// Get all users
	public List<User_Keep> getUserKeep() {
		return user_Keep_Repo.findAll();
	}

	// Get user details by userName
	public User_Keep getUserKeepByName(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		return optionalUserKeep.get();
	}

	// Get all notes of the user by userName
	public List<Notes_Keep> getAllNotesKeepByName(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < l.size(); i++) {
			Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
			if (listNotesKeep.isPresent()) {
				allNotesOfUser.add(listNotesKeep.get());
			}
		}
		return allNotesOfUser;

	}

	// Get a particular notes of the user by notes name
	public List<Notes_Keep> getNotesKeepByName(String userName, String name) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < l.size(); i++) {
			Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
			if (listNotesKeep.isPresent()) {
				allNotesOfUser.add(listNotesKeep.get());
			}
		}
		ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			String s2 = name;
			if (s1.equals(s2)) {
				oneNoteOfUser.add(allNotesOfUser.get(i));
			}
		}
		return oneNoteOfUser;

	}

	// Update user details by userName
	public User_Keep updateUserKeep(String userName, Keep_Request request) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep updateUK = optionalUserKeep.get();
		updateUK.setUserName(request.getUserName());
		updateUK.setPassword(request.getPassword());
		return user_Keep_Repo.save(updateUK);

	}

	// Update a particular notes of the user by notes name
	public Notes_Keep updateNotesKeep(String userName, String name, Keep_Request request)
			throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		try {
			User_Keep user_Keep = optionalUserKeep.get();
			ArrayList<String> l = user_Keep.getNotesIds();
			ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < l.size(); i++) {
				Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
				if (listNotesKeep.isPresent()) {
					allNotesOfUser.add(listNotesKeep.get());
				}
			}
			ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < allNotesOfUser.size(); i++) {
				String s1 = allNotesOfUser.get(i).getName();
				String s2 = name;
				if (s1.equals(s2)) {
					oneNoteOfUser.add(allNotesOfUser.get(i));
				}
			}
			Notes_Keep updateNK = oneNoteOfUser.get(0);
			updateNK.setName(request.getName());
			updateNK.setNotes(request.getNotes());
			return notes_Keep_Repo.save(updateNK);
		} catch (Exception e) {
			throw new ResourceNotFoundException("No Notes was found with the name :- '" + name + "'.");
		}
	}

	// Delete a particular notes of the user by notes name
	public void deleteNotesKeep(String userName, String name) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		try {
			User_Keep user_Keep = optionalUserKeep.get();
			ArrayList<String> l = user_Keep.getNotesIds();
			ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < l.size(); i++) {
				Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
				if (listNotesKeep.isPresent()) {
					allNotesOfUser.add(listNotesKeep.get());
				}
			}
			ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < allNotesOfUser.size(); i++) {
				String s1 = allNotesOfUser.get(i).getName();
				String s2 = name;
				if (s1.equals(s2)) {
					oneNoteOfUser.add(allNotesOfUser.get(i));
				}
			}
			for (int i = 0; i < l.size(); i++) {
				String nid = oneNoteOfUser.get(0).getId();
				if (l.get(i).equals(nid)) {
					l.remove(i);
				}
			}
			user_Keep.setNotesIds(l);
			user_Keep_Repo.save(user_Keep);
			Notes_Keep deleteNK = oneNoteOfUser.get(0);
			notes_Keep_Repo.delete(deleteNK);
		} catch (Exception e) {
			throw new ResourceNotFoundException("No Notes was found with the name :- '" + name + "'.");
		}
	}

	// Delete user by userName and all the notes of that particular user
	public void deleteUserKeep(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		for (int i = 0; i < l.size(); i++) {
			notes_Keep_Repo.deleteById(l.get(i));
		}
		user_Keep_Repo.delete(user_Keep);
	}
}
