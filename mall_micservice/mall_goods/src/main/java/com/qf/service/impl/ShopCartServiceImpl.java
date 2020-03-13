package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IShopCartMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IGoodsService;
import com.qf.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ShopCartServiceImpl implements IShopCartService {
    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private IShopCartService iShopCartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IShopCartMapper iShopCartMapper;

    /**
     * 添加购物车
     *
     * @param user
     * @param gid
     * @param gnumber
     * @param cartToken
     * @return
     */
    @Override
    public String addCart(User user, Integer gid, Integer gnumber, String cartToken) {
        //根据gid查询商品信息
        Goods good = iGoodsService.queryGoodsById(gid);
        BigDecimal cartPrice = good.getPrice().multiply(BigDecimal.valueOf(gnumber));
        //构造一个cart对象（gid,gnumber,小计,uid）
        ShopCart cart = new ShopCart();
        cart.setGid(gid).setNumber(gnumber).setCartPrice(cartPrice).setStatus(0).setCreateTime(new Date());

        //1.判断用户是否为空
        //1.1为空（没有登录）
        //1.1.1判断cartToken是否为空
        //为空，创建一个uuid作为cartToken
        //不为空
        //1.1.2根据cartToken将cart从左push入链表
        //1.2不为空
        //1.2.1调用mybatis的单表操作方法将cart添加入数据库
        if (null != user) {
            cart.setUid(user.getId());
            iShopCartService.insert(cart);
        } else {
            if (null == cartToken) {
                cartToken = UUID.randomUUID().toString();
            }
            redisTemplate.opsForList().leftPush(cartToken, cart);
        }
        return cartToken;
    }

    /**
     * 添加数据库购物车
     *
     * @param cart
     */
    @Override
    public void insert(ShopCart cart) {
        iShopCartMapper.insert(cart);
    }

    /**
     * 根据用户id查询用户购物车
     *
     * @param user
     * @param cartToken
     * @return
     */
    @Override
    public List<ShopCart> shopCartList(User user, String cartToken) {
        //1.判断用户是否登录
        //1.1为空：未登录
        //根据cartToken购物车唯一标识查询redis临时数据库
        //1.1不为空：已登录
        //根据用户id直接查询数据库
        //2.遍历购物车中商品，查询每一件商品的详细信息
        List<ShopCart> shopCartList = null;
        if (null == user) {
            //查询redis
            if (cartToken != null) {
                //获得redis中购物车的长度
                Long size = redisTemplate.opsForList().size(cartToken);
                shopCartList = redisTemplate.opsForList().range(cartToken, 0, size);
            }
        } else {
            //直接查询数据库
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());
            queryWrapper.orderByDesc("create_time");
            shopCartList = iShopCartMapper.selectList(queryWrapper);
        }
        //如果不为空
        if (null != shopCartList) {
            //遍历购物车查询购物车中每一件商品
            for (ShopCart car : shopCartList) {
                //得到商品id
                Integer gid = car.getGid();
                //查询商品信息
                Goods goods = iGoodsService.queryGoodsById(gid);
                System.out.println("查询到的商品商品" + goods);
                car.setGoods(goods);
            }
        }

        return shopCartList;
    }

    /**
     * 根据用户id和商品id查询商品清单
     *
     * @param gid
     * @param user
     * @return
     */
    @Override
    public List<ShopCart> queryCartsByGid(Integer[] gid, User user) {
        //1.创建条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", user.getId());
        //商品id在这个数组里面的话就会被查询出来
        queryWrapper.in("gid", gid);
        List<ShopCart> shopCartList = iShopCartMapper.selectList(queryWrapper);

        for (ShopCart shopCart : shopCartList) {
            Integer id = shopCart.getGid();
            //
            Goods goods = iGoodsService.queryGoodsById(id);
            shopCart.setGoods(goods);
        }
        return shopCartList;
    }

    /**
     * 根据购物车id查询购物车集合
     *
     * @return
     */
    @Override
    public List<ShopCart> selectShopCartByCid(Integer[] cids) {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", cids);
        List<ShopCart> shopCartList = iShopCartMapper.selectList(queryWrapper);
        //遍历购物车，查询购物车中商品信息
        for (ShopCart shopCart : shopCartList
        ) {
            Goods goods = iGoodsService.queryGoodsById(shopCart.getGid());
            shopCart.setGoods(goods);
        }
        return shopCartList;
    }

    /**
     * 根据购物车id删除购物车清单
     *
     * @param cids
     * @return
     */
    @Override
    public int deleteByCids(Integer[] cids) {
        int i = iShopCartMapper.deleteBatchIds(Arrays.asList(cids));
        return i;
    }

}
