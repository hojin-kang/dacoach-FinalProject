package com.dacoach.model.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.sql.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CertDTO {
	private int cert_idx;
	private int user_idx;
	private String cert_name;
	private Date get_date;
	private String cert_from;
	private String cert_type;
	private String cert_file;
	private String cert_status;
}
