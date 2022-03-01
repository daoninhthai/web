package com.comittedpeople.englishlearningweb.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.comittedpeople.englishlearningweb.domain.ChatRoomMessage;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long>{
	
	//Luôn luôn chỉ trả về 10 tin nhắn tiếp theo.
	List<ChatRoomMessage> findTop10ByMessageSentDateGreaterThanOrderByMessageSentDateAsc(LocalDateTime timeStamp);
	
	//Luôn luôn chỉ trả về 10 tin nhắn đầu tiên.
	List<ChatRoomMessage> findTop10ByOrderByMessageSentDateAsc();
	

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
