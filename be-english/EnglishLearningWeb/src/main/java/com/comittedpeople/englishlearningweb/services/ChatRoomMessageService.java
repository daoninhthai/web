package com.comittedpeople.englishlearningweb.services;

import java.util.List;

import com.comittedpeople.englishlearningweb.api.v1.model.ChatRoomMessageDTO;
import com.comittedpeople.englishlearningweb.domain.ChatRoomMessage;

public interface ChatRoomMessageService {

	ChatRoomMessageDTO postChatRoomMessage (ChatRoomMessageDTO chatRoomMessageDTO);
	
	Boolean deleteChatRoomMessage (Long messageID);
	
	List<ChatRoomMessageDTO> getLatestChatRoomMessages();
	
	//Giới hạn trả về 10 message cùng 1 lúc.
	//Nếu cần tiếp thì người dùng get tiếp dựa trên chatID mới.
	List<ChatRoomMessageDTO> getChatMessageAfterID (Long chatID);

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
