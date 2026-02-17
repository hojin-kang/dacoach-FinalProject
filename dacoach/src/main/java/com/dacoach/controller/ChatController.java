package com.dacoach.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.service.chat.ChatService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@GetMapping("/chat")
	public String chatMain(HttpSession session, Model model,
			@RequestParam(value = "tab", required = false) String tab) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login"; 

		model.addAttribute("rooms", chatService.listRooms(my));
		model.addAttribute("myIdx", my);
		model.addAttribute("activeRoom", null);
		model.addAttribute("activeRoomIdx", null);
		model.addAttribute("tab", tab);

		return "chat/chatRoomList";
	}

	@GetMapping("/chat/start")
	public String startChat(@RequestParam int targetIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		int roomIdx = chatService.getOrCreateRoom(my, targetIdx);

		if (tab != null && !tab.isBlank()) {
			return "redirect:/chat/room/" + roomIdx + "?tab=" + tab;
		}
		return "redirect:/chat/room/" + roomIdx;
	}

	@GetMapping("/chat/room/{roomIdx}")
	public String chatRoom(@PathVariable int roomIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session, Model model) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		model.addAttribute("rooms", chatService.listRooms(my));

		ChatRoomDTO activeRoom = chatService.selectRoomByIdx(roomIdx, my);
		
		boolean isLeft=chatService.isLeft(roomIdx, my);
		
		model.addAttribute("isLeft", isLeft);
		System.out.println(isLeft);
		model.addAttribute("activeRoom", activeRoom);
		
		model.addAttribute("activeRoomIdx", roomIdx);
		model.addAttribute("myIdx", my);
		model.addAttribute("tab", tab);

		return "chat/chatRoomList";
	}

	@GetMapping(value = "/chat/messages/{roomIdx}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String messages(@PathVariable int roomIdx, HttpSession session) {
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "";
		return chatService.loadMessagesRaw(roomIdx, my);
	}
	
	
	// 채팅방 나가기
	@PostMapping("/chat/room/{roomIdx}/leave")
	@ResponseBody
	public String leaveRoom(@PathVariable int roomIdx, HttpSession session){
	    Integer my = (Integer) session.getAttribute("user_idx");
	    if(my == null) return "NOLOGIN";
	    
	    ChatRoomDTO room=chatService.selectRoomByIdx(roomIdx, my);

	    boolean ok = chatService.leaveRoom(roomIdx, my);
	    chatService.updateChatStatus(roomIdx);
	    
	    int result=chatService.deleteChat(roomIdx);
	    if (result > 0) {
	    	
	        String baseDir = "C:/student_java/dacoach/dacoach/uploads/chats";
	        String fileName = "chat_" + room.getUser1Idx() + "_" + room.getUser2Idx();
	        String fileName2 = "chat_" + room.getUser2Idx() + "_" + room.getUser1Idx();
	        
	        try {
	            Path filePath = Paths.get(baseDir, fileName);
	            Path filePath2 = Paths.get(baseDir, fileName2);
	            Files.deleteIfExists(filePath);
	            Files.deleteIfExists(filePath2);
	        } catch (IOException e) {
	            System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
	        }
	    }
	    return ok ? "OK" : "FAIL";
	}
}
