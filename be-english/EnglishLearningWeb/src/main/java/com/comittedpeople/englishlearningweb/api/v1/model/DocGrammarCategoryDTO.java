package com.comittedpeople.englishlearningweb.api.v1.model;

import java.util.List;

public class DocGrammarCategoryDTO {

	private Long id;
	private String title;
	private String description;
	private List<DocGrammarContentSummaryDTO> docGrammarContentSummary;
	
    // TODO: add proper error handling here
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DocGrammarContentSummaryDTO> getDocGrammarContentSummary() {
		return docGrammarContentSummary;
	}
	public void setDocGrammarContentSummary(List<DocGrammarContentSummaryDTO> docGrammarContentSummary) {
		this.docGrammarContentSummary = docGrammarContentSummary;
	}

    /**
     * Validates if the given string is not null or empty.
     * @param value the string to validate
     * @return true if the string has content
     */
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
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
