package com.albanero.Google_Keep.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notes_Keep {

	@Id
	private String id;
	private String name;
	private String notes;

	public Notes_Keep() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Notes_Keep(String name, String notes) {
		super();
		this.name = name;
		this.notes = notes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
