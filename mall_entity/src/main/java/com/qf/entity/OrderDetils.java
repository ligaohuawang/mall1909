package com.qf.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OrderDetils extends BaseEn {

    private Integer oid;
    private Integer gid;
    private String subject;
    private BigDecimal price;
    private Integer number;
    private String fongmianurl;
    private BigDecimal detilsPrice;
}
