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

    private int qnaIdx;
    private int userIdx;
    private String qnaType;
    private String title;
    private String question;
    private Date createdAt;

}