package com.comittedpeople.englishlearningweb.controllers.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comittedpeople.englishlearningweb.api.v1.model.DocGrammarNoteDTO;
import com.comittedpeople.englishlearningweb.services.DocGrammarNoteService;

@Controller
public class DocGrammarNoteController {

	private final DocGrammarNoteService docGrammarNoteService;

	public DocGrammarNoteController(DocGrammarNoteService docGrammarNoteService) {
		super();
		this.docGrammarNoteService = docGrammarNoteService;
	}
	
	@GetMapping("/api/v1/grammarForms/{formID}/notes")
	public ResponseEntity<List<DocGrammarNoteDTO>> getDocGrammarNote(@PathVariable Long formID){
		
		HttpStatus returnStatus;
		
		List<DocGrammarNoteDTO> returnDTO = docGrammarNoteService.getAllDocGrammarNoteDTOByFormID(formID);
		
		if (returnDTO == null)
			returnStatus = HttpStatus.NOT_FOUND;
		else 
			returnStatus = HttpStatus.OK;
		
		return new ResponseEntity<List<DocGrammarNoteDTO>>(returnDTO, returnStatus);
	}
	
	@PutMapping("/api/v1/grammarForms/{formID}/notes")
	public ResponseEntity<List<DocGrammarNoteDTO>> putDocGrammarNote (@PathVariable Long formID, 
			@Valid @RequestBody List<DocGrammarNoteDTO> noteDTOs){
		HttpStatus returnStatus;
		List<DocGrammarNoteDTO> returnDTOs = docGrammarNoteService.putAllDocGrammarNoteDTOByFormID(formID, noteDTOs);
		
		if (returnDTOs == null)
			returnStatus = HttpStatus.NOT_FOUND;
		else 
			returnStatus = HttpStatus.OK;
		
		return new ResponseEntity<List<DocGrammarNoteDTO>>(returnDTOs, returnStatus);
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
