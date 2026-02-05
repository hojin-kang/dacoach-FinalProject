package com.dacoach.model.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
 private int company_idx;
 private int users_idx;
 private String company_num;
 private String phone;
 private String address;
 private String intro;
 private String photo;
 private String kakao_key;
 private String email;
}
