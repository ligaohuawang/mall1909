package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    //注入搜索服务对象，调用搜索服务查询数据
    @Reference
    private ISearchProviderService iSearchProviderService;

    @Reference
    private IGoodsService iGoodsService;

    /**
     * 根据商品信息从solr中查询商品
     *
     * @param information
     * @param map
     * @return
     */
    @RequestMapping("/SearchByInformation")
    public String searchByInformation(String information, ModelMap map) {
        List<Goods> goodsList = iSearchProviderService.queryGoodsListByInformation(information);
        map.addAttribute("goodsList", goodsList);
        return "goodsList";
    }

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/queryGoodsDetailsById")
    public String queryGoodsDetailsById(Integer id, ModelMap map) {
        Goods goods = iGoodsService.queryGoodsDetailsById(id);

        System.out.println("商品详情详情" + goods);
        map.addAttribute("good", goods);
        return "goodsDetails";
    }
}
