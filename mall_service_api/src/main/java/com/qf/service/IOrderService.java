package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {
    /**
     * 生成订单
     *
     * @param cids 购物清单id
     * @param aid  地址id
     * @param user 用户信息
     * @return
     */
    Orders createOrder(Integer[] cids, Integer aid, User user);

    /**
     * 根据订单id查询订单信息
     *
     * @param orderid
     * @return
     */
    Orders QueryByOid(String orderid);

    /***
     * 查询订单列表
     * @param id
     * @return
     */
    List<Orders> queryByUid(Integer id);

    /***
     * 修改订单状态
     * @param
     * @param
     * @return
     */
    int updateOrderStatus(String orderid, Integer status);
}
