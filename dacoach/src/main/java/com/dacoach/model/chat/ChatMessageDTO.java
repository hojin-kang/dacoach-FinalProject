package com.dacoach.model.chat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class ChatMessageDTO {
    private Integer roomIdx;
    private Integer senderIdx;
    private String message;

    // yyyy-MM-dd HH:mm:ss
    private String ts;
}
