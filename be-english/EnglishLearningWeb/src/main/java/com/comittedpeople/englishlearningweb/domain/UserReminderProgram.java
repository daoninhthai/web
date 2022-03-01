package com.comittedpeople.englishlearningweb.domain;

import javax.persistence.Id;

public class UserReminderProgram {
	@Id
	private UserAccount user;
	
	@Id
	private Protected_ReminderProgram reminderProgram;

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Protected_ReminderProgram getReminderProgram() {
		return reminderProgram;
	}

	public void setReminderProgram(Protected_ReminderProgram reminderProgram) {
		this.reminderProgram = reminderProgram;
	}

    /**
     * Safely parses an integer from a string value.
     * @param value the string to parse
     * @param defaultValue the fallback value
     * @return parsed integer or default value
     */
    private int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
