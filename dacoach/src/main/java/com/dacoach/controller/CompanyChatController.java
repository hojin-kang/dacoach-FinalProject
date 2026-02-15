package com.dacoach.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.users.UsersDTO;
import com.dacoach.service.chat.ChatService;
import com.dacoach.service.company.CompanyService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyChatController {

	private final ChatService chatService;
	private final CompanyService companyService;
	
	@GetMapping("/chat")
	public String chatMain(HttpSession session, Model model,
			@RequestParam(value = "tab", required = false) String tab) {
		
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";
		
		try {	
			model.addAttribute("userList",companyService.getCoachList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("rooms", chatService.listRooms(my));
		model.addAttribute("myIdx", my);
		model.addAttribute("activeRoom", null);
		model.addAttribute("activeRoomIdx", null);
		model.addAttribute("tab", tab);

		return "/company/chat/chatRoomList";
	}

	@GetMapping("/chat/start")
	public String startChat(@RequestParam int targetIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		int roomIdx = chatService.getOrCreateRoom(my, targetIdx);

		if (tab != null && !tab.isBlank()) {
			return "redirect:/company/chat/room/" + roomIdx + "?tab=" + tab;
		}
		return "redirect:/company/chat/room/" + roomIdx;
	}

	@GetMapping("/chat/room/{roomIdx}")
	public String chatRoom(@PathVariable int roomIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session, Model model) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";

		model.addAttribute("rooms", chatService.listRooms(my));

		ChatRoomDTO activeRoom = chatService.selectRoomByIdx(roomIdx, my);
		model.addAttribute("activeRoom", activeRoom);

		model.addAttribute("activeRoomIdx", roomIdx);
		model.addAttribute("myIdx", my);
		model.addAttribute("tab", tab);

		return "/company/chat/chatRoomList";
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

	    boolean ok = chatService.leaveRoom(roomIdx, my);
	    return ok ? "OK" : "FAIL";
	}
}
