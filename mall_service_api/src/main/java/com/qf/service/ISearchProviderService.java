package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchProviderService {
    /**
     * 根据商品信息从Solr查询商品
     *
     * @param information
     * @return
     */
    List<Goods> queryGoodsListByInformation(String information);

    /**
     * 添加商品到数据库时同步索引库
     *
     * @param good
     * @return
     */
    int addGoodsToSolr(Goods good);
}
