package com.dacoach.model.classes;

import java.util.Date;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassDTO {
	private Integer classIdx;
	private Integer providerIdx;
	private String title;
	private String intro;
	private Integer minorFieldIdx;
	private Integer minorRegionIdx;
	private Integer price;
	private Date startDate;
	private Date endDate;
	private Date createdAt;
	private Integer maxUserCnt;
}