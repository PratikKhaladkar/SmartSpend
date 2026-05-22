package com.expense_tracker.ai.service;

import com.expense_tracker.ai.controller.ChatController;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.expense_tracker.dtos.AIResponseDto;
import com.expense_tracker.dtos.ChatHistoryRepsonseDto;
import com.expense_tracker.entities.ChatHistory;
import com.expense_tracker.enums.ResponseType;
import com.expense_tracker.repositories.ChatHistoryRepo;
import com.expense_tracker.security.service.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatService {

	private final ChatClient chatClient;

	private final Tools tools;

	private final ChatHistoryRepo chatHistoryRepo;
	
	private final ObjectMapper objectMapper;
	
	private ChatMemory chatMemory;

	public ChatService(ChatClient.Builder builder, @Value("classpath:/prompts/system-prompt.st") Resource resource,
			ChatMemory chatMemory, Tools tools,ObjectMapper objectMapper, ChatHistoryRepo chatHistoryRepo) {

		this.chatClient = builder.defaultSystem(resource).build();
		this.tools = tools;
		this.chatHistoryRepo = chatHistoryRepo;
		this.objectMapper = objectMapper;
		this.chatMemory=chatMemory;

	}

	   public AIResponseDto chat(String prompt) {
		   String conversationId = SecurityUtil.getAuthenticatedUser().getId().toString();
    
		
		    String rawResponse = null;
		    try {
		        rawResponse = chatClient
		                .prompt()
		                .advisors(
		                    MessageChatMemoryAdvisor.builder(chatMemory)
		                        .conversationId(conversationId)
		                        .build())
		                .tools(tools)
		                .user(prompt)
		                .call()
		                .content();
		    } catch (Exception e) {
		        try {
		            rawResponse = chatClient
		                    .prompt()
		                    .advisors(
		                        MessageChatMemoryAdvisor.builder(chatMemory)
		                            .conversationId(conversationId)
		                            .build())
		                    .tools(tools)
		                    .user(prompt)
		                    .call()
		                    .content();
		        } catch (Exception retryEx) {
		            AIResponseDto fallback = buildFallback();
		            chatHistoryRepo.save(new ChatHistory(prompt, fallback, SecurityUtil.getAuthenticatedUser()));
		            return fallback;
		        }
		    }

	        AIResponseDto response;
	        try {
	            String cleaned = rawResponse
	                    .replaceAll("(?s)```json\\s*", "")
	                    .replaceAll("(?s)```\\s*", "")
	                    .trim();
	            response = objectMapper.readValue(cleaned, AIResponseDto.class);
	        } catch (Exception e) {
	            response = buildFallback();
	            response.setMessage(rawResponse);
	        }

	        ChatHistory chatHistory = new ChatHistory(prompt, response, SecurityUtil.getAuthenticatedUser());
	        chatHistoryRepo.save(chatHistory);
	        return response;
	    }
	public List<ChatHistoryRepsonseDto> getRecentChatInsights() {

		List<ChatHistory> chatHistory = chatHistoryRepo.findTop10ByUserIdOrderByCreatedAtDesc(SecurityUtil.getAuthenticatedUser().getId());

		return chatHistory.stream().map((chat) -> new ChatHistoryRepsonseDto(chat)).toList();

	}
	
	
	private AIResponseDto buildFallback() {
		
		AIResponseDto fallback = new AIResponseDto();
	    fallback.setResponseType(ResponseType.TEXT);
	    fallback.setMessage("I encountered an issue processing your request. Please try again.");
	    fallback.setChartType("NONE");
	    fallback.setLabels(List.of());
	    fallback.setValues(List.of());
	    fallback.setRecommendations(List.of());
	    return fallback;
	}

}
