package com.dacoach.mapper.chat;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dacoach.model.chat.ChatRoomDTO;

@Mapper
public interface ChatMapper {

    /* =========================
       채팅방 리스트
       ========================= */
    List<ChatRoomDTO> selectRoomList(@Param("myIdx") int myIdx);

    /* =========================
       채팅방 단건(내가 멤버일 때만)
       ========================= */
    ChatRoomDTO selectRoomByIdx(@Param("roomIdx") int roomIdx,
                                @Param("myIdx") int myIdx);
    
    ChatRoomDTO selectRoomSimple(int roomIdx);

    /* =========================
       문의하기: 방 있으면 찾기
       ========================= */
    Integer findRoom(@Param("myIdx") int myIdx,
                     @Param("targetIdx") int targetIdx);

    /* =========================
       문의하기: 방 없으면 만들기
       ========================= */
    int insertRoom(@Param("myIdx") int myIdx,
                   @Param("targetIdx") int targetIdx);

    /* =========================
       마지막 메시지/시간 업데이트
       ========================= */
    int updateRoomLast(@Param("roomIdx") int roomIdx,
                       @Param("lastMessage") String lastMessage);

    /* =========================
       unread 처리
       - 방 입장 시 내 unread = 0
       ========================= */
    int resetUnread(@Param("roomIdx") int roomIdx,
                    @Param("myIdx") int myIdx);

    /* =========================
       unread 처리
       - 메시지 보낼 때 상대 unread +1
       ========================= */
    int increaseUnreadOther(@Param("roomIdx") int roomIdx,
                            @Param("myIdx") int myIdx);
    
    // 채팅방 나가기
    int leaveRoom(@Param("roomIdx") int roomIdx,
            @Param("myIdx") int myIdx);
    
    int isLeft(HashMap<String, Object> map);
    
    int deleteChat(int roomIdx);
    
    int updateChatStatus(int roomIdx);
    
    HashMap isMatched(HashMap<String, Object> map);
    
    int deleteMatch(HashMap<String, Object> map);
    
    int getAgreeIdx(HashMap<String, Object> map);
    
    int deleteAgree(int agreement_idx);
    
    // 안읽은 메세지 개수
    Integer countUnreads(@Param("myIdx") int myIdx);
}
