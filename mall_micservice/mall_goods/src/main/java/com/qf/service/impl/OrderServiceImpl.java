package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IOrderDetilsMapper;
import com.qf.dao.IOrderMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.IOrderService;
import com.qf.service.IShopCartService;
import com.qf.utils.PriceUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Reference
    private IAddressService iAddressService;
    @Autowired
    private IShopCartService iShopCartService;
    @Autowired
    private IOrderMapper iOrderMapper;
    @Autowired
    private IOrderDetilsMapper iOrderDetilsMapper;

    /**
     * 生成订单及订单详情
     *
     * @param cids 购物清单id
     * @param aid  地址id
     * @param user 用户信息
     * @return
     */
    @Override
    @Transactional
    public Orders createOrder(Integer[] cids, Integer aid, User user) {
        //1.根据地址id查询出地址
        Address address = iAddressService.selectAddressById(aid);
        //2.计算订单总价
        //2.1先获得要生成订单的购物车清单
        List<ShopCart> shopCartList = iShopCartService.selectShopCartByCid(cids);
        //2.2计算总价
        double allPrice = PriceUtil2.allPrice(shopCartList);
        //创建订单
        Orders orders = new Orders()
                .setOrderid(UUID.randomUUID().toString())
                .setUid(user.getId())
                .setAllprice(BigDecimal.valueOf(allPrice))
                .setPhone(address.getPhone())
                .setCode(address.getCode())
                .setAddress(address.getAddress())
                .setPerson(address.getPerson());
        orders.setCreateTime(new Date());
        //将订单添加进订单表
        int insert = iOrderMapper.insert(orders);

        //构建并保存订单详情
        for (ShopCart shopCart : shopCartList) {
            OrderDetils orderDetils = new OrderDetils()
                    .setGid(shopCart.getGid())
                    .setDetilsPrice(shopCart.getCartPrice())
                    .setNumber(shopCart.getNumber())
                    .setPrice(shopCart.getGoods().getPrice())
                    .setSubject(shopCart.getGoods().getSubject())
                    .setOid(orders.getId())
                    .setFongmianurl(shopCart.getGoods().getFongmianurl());
            //添加订单详情
            orderDetils.setCreateTime(new Date());
            int insert1 = iOrderDetilsMapper.insert(orderDetils);

            //删除购物车清单
            int i = iShopCartService.deleteByCids(cids);
        }
        return orders;
    }

    /**
     * 根据订单id查询订单信息
     *
     * @param orderid
     * @return
     */
    @Override
    public Orders QueryByOid(String orderid) {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid", orderid);
        Orders orders = iOrderMapper.selectOne(queryWrapper);

        //查询订单详情
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("oid", orders.getId());
        List<OrderDetils> list = iOrderDetilsMapper.selectList(queryWrapper2);
        orders.setOrderDetils(list);

        return orders;
    }

    /**
     * 查询低订单列表
     *
     * @param
     * @return
     */
    @Override
    public List<Orders> queryByUid(Integer uid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        queryWrapper.orderByDesc("create_time");
        List<Orders> ordersList = iOrderMapper.selectList(queryWrapper);

        for (Orders orders : ordersList) {
            //查询订单详情
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("oid", orders.getId());
            List<OrderDetils> list = iOrderDetilsMapper.selectList(queryWrapper2);
            orders.setOrderDetils(list);
        }
        return ordersList;
    }

    /**
     * 修改订单状态
     *
     * @param
     * @param
     * @return
     */
    @Override
    public int updateOrderStatus(String orderid, Integer status) {
        Orders orders = this.QueryByOid(orderid);
        orders.setStatus(status);
        return iOrderMapper.updateById(orders);
    }
}
