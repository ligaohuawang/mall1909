package com.qf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.UserHolder;
import com.qf.aop.isLogin;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private IShopCartService iShopCartService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加购物车
     *
     * @param gid
     * @param gnumber
     * @return
     */
    @isLogin
    @RequestMapping("/addCart")
    public String addCart(Integer gid, Integer gnumber, @CookieValue(name = "cartToken", required = false) String cartToken, HttpServletResponse response) {
        //1.得到要添加的商品id和数量
        //2.得到当前要添加购物车的用户
        //3.调用购物车服务添加购物车（在购物车服务判断用户是否为空），方法返回carToken，作为当前用户购物车唯一标识
        //4.将cartToken写入cookie

        User user = UserHolder.getUser();
        cartToken = iShopCartService.addCart(user, gid, gnumber, cartToken);

        //购物车的token写入cookie中
        Cookie cookie = new Cookie("cartToken", cartToken);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        response.addCookie(cookie);

        System.out.println("收到商品id" + gid + "收到商品数量" + gnumber);
        return "addCartSuccess";
    }

    /**
     * 查询购物车信息
     *
     * @return
     */

    @RequestMapping("/shopcartlist")//TODO 2.查询购物车信息
    @ResponseBody
    @isLogin
    public String shopcartlist(@CookieValue(name = "cartToken", required = false) String cartToken, String callback) {
        //1.拿到登录的用户
        //2.调用购物车服务查询购物车信息
        User user = UserHolder.getUser();
        List<ShopCart> shopCartList = iShopCartService.shopCartList(user, cartToken);
        return callback != null ? callback + "(" + JSON.toJSONString(shopCartList) + ")" : JSON.toJSONString(shopCartList);
    }

    /**
     * 跳转到购物车列表页面
     *
     * @return
     */
    @RequestMapping("/shopcartlist2")
    @isLogin
    public String showList(@CookieValue(name = "cartToken", required = false) String cartToken, Model model) {

        User user = UserHolder.getUser();
        List<ShopCart> shopCarts = iShopCartService.shopCartList(user, cartToken);
        model.addAttribute("carts", shopCarts);

        return "cartlist";
    }


    @isLogin(mustLogin = true)
    @RequestMapping("/shopcartmerge")
    public String shopcartmerge(String returnUrl, @CookieValue(name = "cartToken", required = false) String cartToken, HttpServletResponse response) {

        //如果临时购物车唯一标识不为空
        if (cartToken != null) {
            //获取临时购物车
            Long size = redisTemplate.opsForList().size(cartToken);
            List<ShopCart> carts = redisTemplate.opsForList().range(cartToken, 0, size);
            //将临时购物车保存到数据库中
            User user = UserHolder.getUser();
            for (ShopCart cart : carts) {
                iShopCartService.addCart(user, cart.getGid(), cart.getNumber(), cartToken);
            }

            //清空临时购物车
            redisTemplate.delete(cartToken);

            //删除cookie
            Cookie cookie = new Cookie("cartToken", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);


        }
        return "redirect:" + returnUrl;
    }
}
