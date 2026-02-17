package com.dacoach.service.chat;

import java.util.List;

import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;

public interface ChatService {
    List<ChatRoomDTO> listRooms(int myIdx);

    ChatRoomDTO selectRoomByIdx(int roomIdx, int myIdx);

    // yyyy-MM-dd HH:mm:ss|senderIdx|message
    String loadMessagesRaw(int roomIdx, int myIdx);

    // TXT append + last/unread 업데이트 + 브로드캐스트 dto 생성
    ChatMessageDTO saveAndBuildBroadcast(ChatMessageDTO msg);
    
    int getOrCreateRoom(int myIdx, int targetIdx);
    
    // 채팅방 나가기
    boolean leaveRoom(int roomIdx, int myIdx);
    
    // 채팅상대 존재여부 확인
    boolean isLeft(int roomIdx, int myIdx);
    
    public int deleteChat(int roomIdx);
    
    public int updateChatStatus(int roomIdx);
}
