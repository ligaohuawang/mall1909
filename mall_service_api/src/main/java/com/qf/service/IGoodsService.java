package com.qf.service;

import com.qf.entity.Goods;
import com.qf.entity.ShopCart;

import java.util.List;

public interface IGoodsService {
    List<Goods> queryGoodsList();

    /**
     * 添加商品
     *
     * @param goods
     */
    void addGood(Goods goods);

    /**
     * 添加商品后查询商品返回页面
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
    Goods queryGoodsDetailsById(Integer id);

    /**
     * 根据商品id查询商品信息
     *
     * @param gid
     * @return
     */
    Goods queryGoodsById(Integer gid);
}
