package com.dacoach.model.likes;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class LikesUserDTO {
	
	private Integer coach_idx;
    private Integer user_idx;

    private String nickname;
    private String intro;
    private String photo;

    private Date created_at;

}
