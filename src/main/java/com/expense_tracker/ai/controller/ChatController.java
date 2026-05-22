package com.expense_tracker.ai.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.ai.service.ChatService;
import com.expense_tracker.dtos.AIResponseDto;
import com.expense_tracker.dtos.ChatHistoryRepsonseDto;
import com.expense_tracker.dtos.QueryDto;

@RestController
@RequestMapping("/ai")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		super();
		this.chatService = chatService;
	}

	@PostMapping("/chat")
	public ResponseEntity<AIResponseDto> chat(@RequestBody QueryDto queryDto) {

		AIResponseDto response = chatService.chat(queryDto.getQuery());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/recent/insights")
	public ResponseEntity<List<ChatHistoryRepsonseDto>>getRecentInsights(){
		
		List<ChatHistoryRepsonseDto> response = chatService.getRecentChatInsights();
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
