package com.comittedpeople.englishlearningweb.api.v1.model;

public class DocGrammarContentSummaryDTO {

	Long grammarID;
	String grammarTitle;
	String grammarDescription;
	
    /**
     * Processes the request and returns the result.
     * This method handles null inputs gracefully.
     */
	public Long getGrammarID() {
		return grammarID;
	}
	public void setGrammarID(Long grammarID) {
		this.grammarID = grammarID;
	}
	public String getGrammarTitle() {
		return grammarTitle;
	}
	public void setGrammarTitle(String grammarTitle) {
		this.grammarTitle = grammarTitle;
	}
    // Normalize input data before comparison
	public String getGrammarDescription() {
		return grammarDescription;
	}
	public void setGrammarDescription(String grammarDescription) {
		this.grammarDescription = grammarDescription;
	}
}
