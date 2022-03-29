package com.albanero.Google_Keep;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;
import com.albanero.Google_Keep.Request.Keep_Request;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleKeepIntegrationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private User_Keep_Repository user_Keep_Repo;

	@Autowired
	private Notes_Keep_Repository notes_Keep_Repo;

	ObjectMapper om = new ObjectMapper();

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void createUserKeep() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		String jsonRequest = om.writeValueAsString(user_Keep);
		MvcResult result = mockMvc.perform(
				post("/keep/create/user-notes").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Create user and note : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void createNotesKeepByUserName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432";
		Optional<User_Keep> optionalUserKeep = user_Keep_Repo.findByUserName(userName);
		User_Keep user_Keep1 = optionalUserKeep.get();
		ArrayList<String> ua = user_Keep.getNotesIds();
		Keep_Request request1 = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Spring",
				"Session on Spring");
		Notes_Keep notes_Keep1 = new Notes_Keep(request1.getName(), request1.getNotes());
		ua.add(notes_Keep1.getId());
		user_Keep_Repo.save(user_Keep1);
		String jsonRequest = om.writeValueAsString(notes_Keep1);
		MvcResult result = mockMvc.perform(post("/keep/create/notes/" + userName).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Create notes by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getAllUserKeep() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		MvcResult result = mockMvc.perform(get("/keep/get/user-notes").content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get all users : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getUserKeepByUserName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432";
		MvcResult result = mockMvc
				.perform(get("/keep/get/user-notes/" + userName).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get user details by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getAllNotesKeepByName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432";
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/keep/get/notes/" + userName).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get all notes of the user by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getNotesKeepByName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432", name = "Java";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/keep/get/notes/" + userName + "/" + name)
				.content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void updateUserKeepByUserName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky453", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky453";
		user_Keep.setPassword("Venky845#");
		String jsonRequest = om.writeValueAsString(user_Keep);
		MvcResult result = mockMvc.perform(put("/keep/put/userdetails/" + userName).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Update user details by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void updateNotesKeepByName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432", name = "Java";
		notes_Keep.setNotes("Session on Java & Spring Boot");
		String jsonRequest = om.writeValueAsString(notes_Keep);
		MvcResult result = mockMvc.perform(put("/keep/put/notesdetails/" + userName + "/" + name).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Update a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void deleteNotesKeepByUserName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432", name = "Java";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.delete("/keep/delete/notesdetails/" + userName + "/" + name).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Delete a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void deleteUserKeepByUserName() throws Exception {
		ArrayList<String> vd = new ArrayList<String>();
		Keep_Request request = new Keep_Request("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd, "Java",
				"Session on Java");
		User_Keep user_Keep = new User_Keep(request.getUserName(), request.getEmail(), request.getPassword(), vd);
		Notes_Keep notes_Keep = new Notes_Keep(request.getName(), request.getNotes());
		notes_Keep_Repo.save(notes_Keep);
		vd.add(notes_Keep.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = "Venky432";
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/keep/delete/userdetails/" + userName)
				.content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Delete user by userName and all the notes of that particular user : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

}
