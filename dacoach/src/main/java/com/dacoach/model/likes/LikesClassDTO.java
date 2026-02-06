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
public class LikesClassDTO {

	private Integer class_idx;
    private String title;
    private String intro;
    private String photo;
    private Integer price;

    private String providerName;

    private Date startDate;
    private Date endDate;

    private Date created_at;
	
}
