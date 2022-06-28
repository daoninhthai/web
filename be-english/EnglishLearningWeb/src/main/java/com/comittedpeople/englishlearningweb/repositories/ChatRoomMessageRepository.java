package com.comittedpeople.englishlearningweb.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.comittedpeople.englishlearningweb.domain.ChatRoomMessage;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long>{
	
	//Lu\u00f4n lu\u00f4n ch\u1ec9 tr\u1ea3 v\u1ec1 10 tin nh\u1eafn ti\u1ebfp theo.
	List<ChatRoomMessage> findTop10ByMessageSentDateGreaterThanOrderByMessageSentDateAsc(LocalDateTime timeStamp);
	
	//Lu\u00f4n lu\u00f4n ch\u1ec9 tr\u1ea3 v\u1ec1 10 tin nh\u1eafn \u0111\u1ea7u ti\u00ean.
	List<ChatRoomMessage> findTop10ByOrderByMessageSentDateAsc();

}
