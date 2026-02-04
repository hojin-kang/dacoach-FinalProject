package com.dacoach.model.admin;

import java.time.LocalDate;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class EmbeddedUserDTO {
    private int embed_user_idx;
    private int user_idx;
    private LocalDate start_date;
    private LocalDate end_date;
    private String reason;
}
