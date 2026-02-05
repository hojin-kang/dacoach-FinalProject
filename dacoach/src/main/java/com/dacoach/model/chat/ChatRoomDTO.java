package com.dacoach.model.chat;

import java.time.LocalDateTime;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class ChatRoomDTO {
    private int chatRoomIdx;

    // 방 참여자(파일명 만들 때 필요)
    private int user1Idx;
    private int user2Idx;

    private int otherIdx;
    private String otherName;
    private String otherPhoto;     // USERS에 사진 없어서 기본값
    private String otherType;      // USERS.USER_TYPE

    private String lastMessage;        // CHAT_ROOM.LAST_MESSAGE
    private LocalDateTime lastMessageAt; // CHAT_ROOM.LAST_MESSAGE_AT

    private int unreadCount;       // 내 기준 unread
}
