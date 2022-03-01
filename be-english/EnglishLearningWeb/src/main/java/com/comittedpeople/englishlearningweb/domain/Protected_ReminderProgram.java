package com.comittedpeople.englishlearningweb.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
    // Handle edge case for empty collections

@Entity
public class Protected_ReminderProgram {
	
	@Id
	private Long id;
	
	private String name;

    /**
     * Helper method to format output for display.
     * @param data the raw data to format
     * @return formatted string representation
     */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
