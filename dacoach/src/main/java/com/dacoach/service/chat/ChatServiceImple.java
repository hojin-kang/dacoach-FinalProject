package com.dacoach.service.chat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.chat.ChatMapper;
import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImple implements ChatService {

    private final ChatMapper chatMapper;

    private static final String CHAT_DIR = "C:/student_java/dacoach/dacoach/uploads/chats";
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Charset[] READ_CHARSETS = new Charset[] {
            StandardCharsets.UTF_8,
            Charset.forName("MS949"),
            Charset.forName("CP949")
    };

    @Override
    public List<ChatRoomDTO> listRooms(int myIdx) {
        return chatMapper.selectRoomList(myIdx);
    }

    @Override
    public ChatRoomDTO selectRoomByIdx(int roomIdx, int myIdx) {
        return chatMapper.selectRoomByIdx(roomIdx, myIdx);
    }
    
    @Override
    public int getOrCreateRoom(int myIdx, int targetIdx) {

        Integer room = chatMapper.findRoom(myIdx, targetIdx);

        if(room != null) return room;

        chatMapper.insertRoom(myIdx, targetIdx);

        return chatMapper.findRoom(myIdx, targetIdx);
    }


    @Override
    public String loadMessagesRaw(int roomIdx, int myIdx) {
        // 권한 체크 + user1/user2 확보
    	
        ChatRoomDTO room = chatMapper.selectRoomByIdx(roomIdx, myIdx);
        if (room == null) return "";
        // 입장 시 unread 0
        chatMapper.resetUnread(roomIdx, myIdx);

        // 기존 파일명 규칙 + 확장자(.txt)까지 자동 매칭
        Path filePath = resolveLegacyChatFilePath(room);
        if (filePath == null || !Files.exists(filePath)) return "";

        // UTF-8 → MS949/CP949 순서로 읽기 시도
        for (Charset cs : READ_CHARSETS) {
            try {
                return Files.readString(filePath, cs);
            } catch (IOException ignored) {
                // 다음 charset으로 재시도
            }
        }
        return "";
    }

    @Override
    @Transactional
    public ChatMessageDTO saveAndBuildBroadcast(ChatMessageDTO msg) {
    	if (msg == null) return null;

        int roomIdx = msg.getRoomIdx();
        int senderIdx = msg.getSenderIdx();
        String message = (msg.getMessage() == null) ? "" : msg.getMessage();

        // --- [수정 구간 시작] ---
        ChatRoomDTO room;
        if (senderIdx == 0) {
            // 시스템 메시지인 경우, 발신자 권한 체크 없이 방 정보만 가져옴
            room = chatMapper.selectRoomSimple(roomIdx); 
        } else {
            // 일반 메시지인 경우 기존 권한 체크 유지
            room = chatMapper.selectRoomByIdx(roomIdx, senderIdx);
        }
        
        if (room == null) return null;
        // --- [수정 구간 끝] ---

        // 폴더 생성
        Path dir = Paths.get(CHAT_DIR);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}

        Path filePath = resolveLegacyChatFilePath(room);
        if (filePath == null) {
            String base = legacyBaseName(room);
            filePath = dir.resolve(base);
        }

        String ts = LocalDateTime.now().format(TS_FMT);
        String clean = message.replace("\r", " ").replace("\n", " ");
        String line = ts + "|" + senderIdx + "|" + clean + "\n";

        try {
            Files.writeString(filePath, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            return null;
        }

        // --- [시스템 메시지일 경우 DB 업데이트 스킵 처리] ---
        if (senderIdx != 0) {
            // DB 미리보기/시간 업데이트
            String preview = clean.length() > 200 ? clean.substring(0, 200) : clean;
            chatMapper.updateRoomLast(roomIdx, preview);

            // 상대 unread +1
            chatMapper.increaseUnreadOther(roomIdx, senderIdx);
        }

        // 브로드캐스트용 객체 생성
        ChatMessageDTO out = new ChatMessageDTO();
        out.setRoomIdx(roomIdx);
        out.setSenderIdx(senderIdx);
        out.setMessage(clean);
        out.setTs(ts);
        return out;
    }

    /** base name: chat_{min}_{max} */
    private String legacyBaseName(ChatRoomDTO room) {
        int a = room.getUser1Idx();
        int b = room.getUser2Idx();
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return "chat_" + min + "_" + max;
    }

    /**
     * 파일 경로 결정 규칙
     * 1) chat_{min}_{max} 있으면 그거
     * 2) chat_{min}_{max}.txt 있으면 그거
     * 3) 둘 다 없으면 null
     */
    private Path resolveLegacyChatFilePath(ChatRoomDTO room) {
        Path dir = Paths.get(CHAT_DIR);

        String base = legacyBaseName(room);

        Path p1 = dir.resolve(base);
        if (Files.exists(p1)) return p1;

        Path p2 = dir.resolve(base + ".txt");
        if (Files.exists(p2)) return p2;

        return null;
    }
    
    // 채팅방 나가기
    @Override
    public boolean leaveRoom(int roomIdx, int myIdx) {
        // 방 권한 체크 (내가 그 방 멤버인지)
        ChatRoomDTO room = chatMapper.selectRoomByIdx(roomIdx, myIdx);
        if(room == null) return false;

        int updated = chatMapper.leaveRoom(roomIdx, myIdx);
        return updated > 0;
    }

	@Override
	public boolean isLeft(int roomIdx, int myIdx) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("chat_room_idx",roomIdx);
		map.put("user_idx", myIdx);
		int result = chatMapper.isLeft(map);
		if(result > 0) return true;
		return false;
	}

	@Override
	public int deleteChat(int roomIdx) {
		return chatMapper.deleteChat(roomIdx);
	}

	@Override
	public int updateChatStatus(int roomIdx) {
		
		return chatMapper.updateChatStatus(roomIdx);
	}

	@Override
	public String isMatched(int myIdx, int targetIdx) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("my_idx", myIdx);
		map.put("target_idx", targetIdx);
		HashMap result = chatMapper.isMatched(map);
		if(result==null) {
			return "COMPANY";
		}
		String isMatched=(String) result.get("IS_MATCH");
		
		Object applicantObj = result.get("MATCH_APPLICANT");

		int applicant = 0;
		if (applicantObj != null) {
		    applicant = Integer.parseInt(String.valueOf(applicantObj));
		}
		
		if(isMatched.equals("Y")) {
			return "MATCHED";
		}else if(applicant==myIdx) {
			return "APPLIED";
		}else if(applicant==targetIdx) {
			return "RECEIVED";
		}else {
			return "NO_MATCH";
		}
	}

	@Override
	public int deleteMatch(int user_idx, int target_idx) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("user_idx", user_idx);
		map.put("target_idx", target_idx);
		return chatMapper.deleteMatch(map);
	}
	
	@Override
	public Integer findRoom(int myIdx, int targetIdx) {
	    return chatMapper.findRoom(myIdx, targetIdx);
	}

}
