package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("goods")
public class Goods extends BaseEn implements Serializable {
    //标题
    private String subject;
    //价格
    private BigDecimal price;
    //库存
    private Integer save;
    //描述
    private String info;
    //封面图片的路径
    @TableField(exist = false)
    private String fongmianurl;
    //其它图片的路径
    @TableField(exist = false)
    private List<String> otherurls;
}
