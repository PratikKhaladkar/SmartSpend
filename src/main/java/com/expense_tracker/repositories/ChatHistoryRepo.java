package com.expense_tracker.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expense_tracker.entities.ChatHistory;

public interface ChatHistoryRepo extends JpaRepository<ChatHistory, UUID> {
     
	 List<ChatHistory> findTop10ByUserIdOrderByCreatedAtDesc(UUID userId);
	
}
