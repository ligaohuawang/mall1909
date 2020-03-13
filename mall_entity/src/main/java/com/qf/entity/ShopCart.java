package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@TableName("shopcart")
public class ShopCart extends BaseEn implements Serializable {
    private Integer gid;
    private Integer number;
    private Integer uid;
    private BigDecimal cartPrice;
    @TableField(exist = false)
    private Goods goods;
}
