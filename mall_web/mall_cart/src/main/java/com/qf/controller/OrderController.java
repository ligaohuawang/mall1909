package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.UserHolder;
import com.qf.aop.isLogin;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.IOrderService;
import com.qf.service.IShopCartService;
import com.qf.utils.PriceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    //购物车服务
    @Reference
    private IShopCartService iShopCartService;

    //收货地址服务
    @Reference
    private IAddressService iAddressService;

    @Reference
    private IOrderService iOrderService;

    @isLogin(mustLogin = true)
    @RequestMapping("/toCreateOrder")
    public String toCreateOrder(Integer[] gid, Model model) {
        //1.先得到登录用户的信息
        //2.根据用户id和商品id去查询商品信息
        //3.根据用户id查询用户的收货地址
        User user = UserHolder.getUser();
        List<ShopCart> shopCarts = iShopCartService.queryCartsByGid(gid, user);
        //用户所有的收货地址
        List<Address> addresses = iAddressService.selectAllAddressByUid(user.getId());
        //计算总价给页面
        double totalprice = PriceUtil.allPrice(shopCarts);

        model.addAttribute("carts", shopCarts);
        model.addAttribute("addresses", addresses);
        model.addAttribute("allprice", totalprice);

        return "ordersedit";
    }


    /**
     * 查询订单列表
     *
     * @return
     */
    @RequestMapping("/list")
    @isLogin(mustLogin = true)
    public String list(Model model) {
        User user = UserHolder.getUser();
        List<Orders> ordersList = iOrderService.queryByUid(user.getId());
        model.addAttribute("ordersList", ordersList);
        return "orderslist";
    }

    /**
     * 确认收货信息并提交订单
     *
     * @param cids
     * @param aid
     * @return
     */
    @isLogin(mustLogin = true)
    @ResponseBody
    @RequestMapping("/submitCreateOrder")
    public ResultData<Orders> submitCreateOrder(Integer[] cids, Integer aid) {
        User user = UserHolder.getUser();
        //调用方法添加订单并生成订单
        Orders orders = iOrderService.createOrder(cids, aid, user);
        return new ResultData<Orders>().setCode(ResultData.ResultCodeList.OK).setMsg("下单成功！").setData(orders);
    }
}
