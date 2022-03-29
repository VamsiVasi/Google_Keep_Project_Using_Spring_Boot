package com.albanero.Google_Keep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;
import com.albanero.Google_Keep.Request.Keep_Request;
import com.albanero.Google_Keep.Service.Keep_Service;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleKeepUnitTests implements Constants {

	@Autowired
	private Keep_Service keep_Service;

	@MockBean
	private User_Keep_Repository user_Keep_Repo;

	@MockBean
	private Notes_Keep_Repository notes_Keep_Repo;

	private ArrayList<String> NOTES_IDs;

	// GET API Test case for getting all users
	@Test
	public void getAllUserKeepTest() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		ArrayList<String> vd = new ArrayList<String>();
		vd.add(OBJECT_ID3);
		vd.add(OBJECT_ID4);
		when(user_Keep_Repo.findAll()).thenReturn(Stream
				.of(new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs),
						new User_Keep("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd))
				.collect(Collectors.toList()));
		assertEquals(2, keep_Service.getUserKeep().size());
	}

	// GET API Test case for getting user details by existing userName
	@Test
	public void getUserKeepByUserName() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
	}

	// GET API Test case for getting user details by non-existing userName
	@Test
	public void getUserKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getUserKeepByName(userName));
	}

	// GET API Test case for getting all notes of the user by existing userName
	@Test
	public void getAllNotesKeepByUserName() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
	}

	// GET API Test case for getting all notes of the user by non-existing userName
	@Test
	public void getAllNotesKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getAllNotesKeepByName(userName));
	}

	// GET API Test case for getting a particular notes of the existing user by
	// existing notes name
	@Test
	public void getNotesKeepByName() throws ResourceNotFoundException {
		String userName = USERNAME, name = NOTES_NAME1;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
	}

	// GET API Test case for getting a particular notes of the non-existing user
	@Test
	public void getNotesKeepByNonExistingUserName() {
		String userName = USERNAME, name = NOTES_NAME1;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getNotesKeepByName(userName, name));
	}

	// POST API Test case for creating a user and note where user contains only the
	// object id of the notes
	@Test
	public void createUserKeep() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME1, NOTES1);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		verify(notes_Keep_Repo, times(1)).save(notes_Keep);
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(),
				NOTES_IDs);
		user_Keep_Repo.save(user_Keep);
		verify(user_Keep_Repo, times(1)).save(user_Keep);
	}

	// POST API Test case for creating a notes by existing userName
	@Test
	public void createNotesKeep() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> ua = user_Keep.getNotesIds();
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME2, NOTES2);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		when(notes_Keep_Repo.save(notes_Keep)).thenReturn(notes_Keep);
		ua.add(notes_Keep.getId());
		user_Keep.setNotesIds(ua);
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.createNotesKeep(userName, request));
	}

	// POST API Test case for creating a notes by non-existing userName
	@Test
	public void createNotesKeepByNonExistingUserName() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		String userName = USERNAME;
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME2, NOTES2);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		when(notes_Keep_Repo.save(notes_Keep)).thenReturn(notes_Keep);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.createNotesKeep(userName, request));
	}

	// PUT API Test case for updating user details by existing userName
	@Test
	public void updateUserKeepByUserName() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		Keep_Request request = new Keep_Request(UPDATED_USERNAME, EMAIL, UPDATED_PASSWORD, NOTES_IDs, NOTES_NAME1,
				NOTES1);
		user_Keep.setUserName(request.getUserName());
		user_Keep.setPassword(request.getPassword());
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.updateUserKeep(userName, request));
	}

	// PUT API Test case for updating user details by non-existing userName
	@Test
	public void updateUserKeepByNonExistingUserName() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		String userName = USERNAME;
		Keep_Request request = new Keep_Request(UPDATED_USERNAME, EMAIL, UPDATED_PASSWORD, NOTES_IDs, NOTES_NAME1,
				NOTES1);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateUserKeep(userName, request));
	}

	// PUT API Test case for updating a particular notes of the existing user by
	// existing notes name
	@Test
	public void updateNotesKeepByName() throws ResourceNotFoundException {
		String userName = USERNAME, name = NOTES_NAME1;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME1, UPDATED_NOTES);
		notes1.setName(request.getName());
		notes1.setNotes(request.getNotes());
		when(notes_Keep_Repo.save(notes1)).thenReturn(notes1);
		assertEquals(notes1, keep_Service.updateNotesKeep(userName, name, request));
	}

	// PUT API Test case for updating a particular notes of the non-existing user
	@Test
	public void updateNotesKeepByNonExistingUserName() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		String userName = USERNAME, name = NOTES_NAME1;
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME1, UPDATED_NOTES);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateNotesKeep(userName, name, request));
	}

	// PUT API Test case for updating a particular notes of the existing user by
	// non-existing notes name
	@Test
	public void updateNotesKeepByExistingUserNameButNonExistingNotesName() throws ResourceNotFoundException {
		String userName = USERNAME, name = NOTES_NAME2;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME2, UPDATED_NOTES);
		when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateNotesKeep(userName, name, request));
	}

	// DELETE API Test case for deleting a particular notes of the existing user by
	// existing notes name
	@Test
	public void deleteNotesKeepByName() throws ResourceNotFoundException {
		String userName = USERNAME, name = NOTES_NAME1;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
		oneNoteOfUser.add(notes1);
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
		for (int i = 0; i < l.size(); i++) {
			String nid = oneNoteOfUser.get(0).getId();
			if (l.get(i).equals(nid)) {
				l.remove(i);
				NOTES_IDs.remove(i);
			}
		}
		Keep_Request request = new Keep_Request(USERNAME, EMAIL, PASSWORD, NOTES_IDs, NOTES_NAME1, NOTES1);
		user_Keep.setNotesIds(l);
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.updateUserKeep(userName, request));
		Notes_Keep deleteNK = oneNoteOfUser.get(0);
		notes_Keep_Repo.delete(deleteNK);
		verify(notes_Keep_Repo, times(1)).delete(deleteNK);
	}

	// DELETE API Test case for deleting a particular notes of the non-existing user
	@Test
	public void deleteNotesKeepByNonExistingUserName() {
		String userName = USERNAME, name = NOTES_NAME1;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteNotesKeep(userName, name));
	}

	// DELETE API Test case for deleting a particular notes of the existing user by
	// non-existing notes name
	@Test
	public void deleteNotesKeepByExistingUserNameButNonExistingNotesName() throws ResourceNotFoundException {
		String userName = USERNAME, name = NOTES_NAME2;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteNotesKeep(userName, name));
	}

	// DELETE API Test case for deleting a user by existing userName
	@Test
	public void deleteUserKeep() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		for (int i = 0; i < l.size(); i++) {
			notes_Keep_Repo.deleteById(l.get(i));
			verify(notes_Keep_Repo, times(1)).deleteById(l.get(i));
		}
		user_Keep_Repo.delete(user_Keep);
		verify(user_Keep_Repo, times(1)).delete(user_Keep);
	}

	// DELETE API Test case for deleting a user by non-existing userName
	@Test
	public void deleteUserKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteUserKeep(userName));
	}
}
