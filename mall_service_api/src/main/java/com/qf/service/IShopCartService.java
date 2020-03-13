package com.qf.service;

import com.qf.entity.ShopCart;
import com.qf.entity.User;

import java.util.List;

public interface IShopCartService {
    /**
     * 添加购物车
     *
     * @param user
     * @param gid
     * @param gnumber
     * @param cartToken
     * @return
     */
    String addCart(User user, Integer gid, Integer gnumber, String cartToken);

    /**
     * 添加数据库购物车
     *
     * @param cart
     */
    void insert(ShopCart cart);

    /**
     * 根据用户id查询用户购物车
     *
     * @param user
     * @param cartToken
     * @return
     */
    List<ShopCart> shopCartList(User user, String cartToken);

    /**
     * 根据用户id和商品id查询商品清单
     *
     * @param gid
     * @param user
     * @return
     */
    List<ShopCart> queryCartsByGid(Integer[] gid, User user);

    /**
     * 根据购物车id查询购物车集合
     *
     * @return
     */
    List<ShopCart> selectShopCartByCid(Integer[] cids);

    /**
     * 根据购物车id删除购物车清单
     *
     * @param cids
     * @return
     */
    int deleteByCids(Integer[] cids);
}
