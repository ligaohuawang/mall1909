package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IGoodsMapper extends BaseMapper<Goods> {
    /**
     * 添加商品后查询商品返回页面s
     *
     * @return
     */
    List<Goods> queryGoodsListAfterAddGood();

    /**
     * 根据id查询商品详情
     *
     * @param id
     * @return
     */
    Goods selectGoodsDetailsById(@Param("id") Integer id);

    Goods queryById(Integer gid);
}
