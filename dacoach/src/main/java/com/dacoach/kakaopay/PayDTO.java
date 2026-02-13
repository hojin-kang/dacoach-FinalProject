package com.dacoach.kakaopay;

import java.sql.Date;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class PayDTO {
    private Integer pay_idx;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private Date created_at;
    private Date approved_at;
    private Integer total;
    private Integer tax_free;
    private Integer vat;
    private Integer point;
    private Integer discount;
    private Integer green_deposit;
    private String pay_type; // CLASS, TOKEN, MEMBERSHIP
}