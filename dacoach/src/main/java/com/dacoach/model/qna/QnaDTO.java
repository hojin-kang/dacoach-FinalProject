package com.dacoach.model.qna;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QnaDTO {

    private int qna_idx;
    private int user_idx;
    private String qna_type;
    private String title;
    private String question;
    private Date created_at;

    private String answerStatus;
    
    private String user_name;
}