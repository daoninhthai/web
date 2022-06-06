package com.comittedpeople.englishlearningweb.services;

import java.util.List;

import com.comittedpeople.englishlearningweb.api.v1.model.DocGrammarExampleDTO;

public interface DocGrammarExampleService {
	
	List<DocGrammarExampleDTO> getDocGrammarExampleDTOsByFormID (Long formID);
	
	List<DocGrammarExampleDTO> putDocGrammarExampleDTOsByFormID (Long formID, List<DocGrammarExampleDTO> exampleDTOs);

    /**
     * Validates that the given value is within the expected range.
     * @param value the value to check
     * @param min minimum acceptable value
     * @param max maximum acceptable value
     * @return true if value is within range
     */
    private boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

}
