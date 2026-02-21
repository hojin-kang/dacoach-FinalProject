package com.dacoach.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dacoach.mapper.chat.ChatMapper;
import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;
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
	private final ChatMapper chatMapper;
	
	@GetMapping("/chat")
	public String chatMain(HttpSession session, Model model,
			@RequestParam(value = "tab", required = false) String tab) {
		
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";
		
		try {	
			model.addAttribute("userList",companyService.getCoachList());
			
			model.addAttribute("class",companyService.getClass(my));
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
		
		if(tab.equals("CLASS")) {
			targetIdx*=-1;
		}
		int roomIdx = chatService.getOrCreateRoom(my, targetIdx);

		if (tab != null && !tab.isBlank()) {
			return "redirect:/company/chat/room/" + roomIdx + "?tab=" + tab +"&targetIdx="+targetIdx;
		}
		return "redirect:/company/chat/room/" + roomIdx  +"?targetIdx="+targetIdx;
	}

	@GetMapping("/chat/room/{roomIdx}")
	public String chatRoom(@PathVariable int roomIdx, @RequestParam(value = "tab", required = false) String tab,
			HttpSession session, Model model,Integer targetIdx) {

		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "redirect:/login";
		
		try {	
			model.addAttribute("userList",companyService.getCoachList());
			model.addAttribute("class",companyService.getClass(my));
			if(tab.equalsIgnoreCase("CLASS")&&targetIdx!=null) {
					
				List<ClassEnrollmentDTO> userClass = companyService.getUserClass(targetIdx*-1);					
				List<Integer> userClassRoom=new ArrayList<>();
				for(int i=0;i<userClass.size();i++) {
					ClassEnrollmentDTO dto=userClass.get(i);
					int room=chatService.getOrCreateRoom(my, dto.getUser_idx());
					
					userClassRoom.add(room);
				}
				   userClassRoom.add(roomIdx);
				model.addAttribute("userClassRoom",userClassRoom);
				
			}
			ChatRoomDTO activeRoom = chatService.selectRoomByIdx(roomIdx, my);
			if(activeRoom==null)activeRoom=companyService.getClassRoom(roomIdx);
			model.addAttribute("activeRoom", activeRoom);
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		model.addAttribute("rooms", chatService.listRooms(my));
		
		
		model.addAttribute("activeRoomIdx", roomIdx);
		model.addAttribute("myIdx", my);
		model.addAttribute("tab", tab);

		return "/company/chat/chatRoomList";
	}

	@PostMapping("/chat/room/{roomIdx}/read")
	@ResponseBody
	public String readRoom(@PathVariable int roomIdx, HttpSession session) {
	    Integer my = (Integer) session.getAttribute("user_idx");
	    if (my != null) {
	        
	        chatMapper.resetUnread(roomIdx, my); 
	        return "OK";
	    }
	    return "FAIL";
	}
	
	@GetMapping(value = "/chat/messages/{roomIdx}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String messages(@PathVariable int roomIdx, HttpSession session) {
		Integer my = (Integer) session.getAttribute("user_idx");
		if (my == null)
			return "";
		return companyService.loadMessagesRawCustom(roomIdx, my);
	}
	
	
	
	@PostMapping("/chat/room/{roomIdx}/leave")
	@ResponseBody
	public String leaveRoom(@PathVariable int roomIdx, HttpSession session){
	    Integer my = (Integer) session.getAttribute("user_idx");
	    if(my == null) return "NOLOGIN";

	    boolean ok = chatService.leaveRoom(roomIdx, my);
	    return ok ? "OK" : "FAIL";
	}
}
