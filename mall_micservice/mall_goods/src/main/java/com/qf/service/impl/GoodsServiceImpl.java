package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IGoodsImagesMapper;
import com.qf.dao.IGoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.entity.ShopCart;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchProviderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsMapper iGoodsMapper;

    //注入RabbitMQ模板对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //注入搜索服务对象，调用方法同步索引库
    @Reference
    private ISearchProviderService iSearchProviderService;

    @Autowired
    private IGoodsImagesMapper iGoodsImagesMapper;

    /**
     * 查询商品列表
     *
     * @return
     */
    @Override
    public List<Goods> queryGoodsList() {
        return iGoodsMapper.selectList(null);
    }

    /**
     * 添加商品
     *
     * @param goods
     */
    @Override
    @Transactional
    public void addGood(Goods goods) {
        //设置状态为0
        goods.setStatus(0);
        //添加商品并返回得到主键
        iGoodsMapper.insert(goods);
        //1.先添加封面图片
        GoodsImages goodsImages = new GoodsImages();
        //设置图片所属商品的id
        goodsImages.setGid(goods.getId());
        //设置这张图片地址
        goodsImages.setUrl(goods.getFongmianurl());
        //设置为封面
        goodsImages.setIsfengmian(1);
        //设置状态为0
        goodsImages.setStatus(0);
        //添加封面图片
        iGoodsImagesMapper.insert(goodsImages);

        //2.添加其它图片
        for (String otherurl : goods.getOtherurls()) {
            GoodsImages goodsImages2 = new GoodsImages();
            //设置图片所属商品的id
            goodsImages.setGid(goods.getId());
            //设置这张图片地址
            goodsImages.setUrl(otherurl);
            //设置不为封面
            goodsImages.setIsfengmian(0);
            //设置状态为0
            goodsImages.setStatus(0);
            //添加封面图片
            iGoodsImagesMapper.insert(goodsImages);
        }

       /* //调用搜索服务的方法同步solr库
        iSearchProviderService.addGoodsToSolr(goods);*/
        //先将商品信息放入队列中
        rabbitTemplate.convertAndSend("lgh", "", goods);


    }

    /**
     * 添加商品后查询商品返回页面
     *
     * @return
     */
    @Override
    public List<Goods> queryGoodsListAfterAddGood() {
        return iGoodsMapper.queryGoodsListAfterAddGood();
    }

    /**
     * 根据id查询商品详情
     *
     * @param id
     * @return
     */
    @Override
    public Goods queryGoodsDetailsById(Integer id) {
        return iGoodsMapper.selectGoodsDetailsById(id);
    }

    /**
     * 根据商品id查询商品信息
     *
     * @param gid
     * @return
     */
    @Override
    public Goods queryGoodsById(Integer gid) {
        return iGoodsMapper.queryById(gid);
    }


}
