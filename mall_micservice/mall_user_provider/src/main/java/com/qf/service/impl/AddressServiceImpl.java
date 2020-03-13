package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IAddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressMapper iAddressMapper;

    /**
     * 根据用户id查询该用户的所有收货地址
     *
     * @param uid
     * @return
     */
    @Override
    public List<Address> selectAllAddressByUid(Integer uid) {
        //根据用户id查询用户地址
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        List<Address> addresses = iAddressMapper.selectList(queryWrapper);
        return addresses;
    }

    /**
     * 根据地址id查询地址信息
     *
     * @param aid
     * @return
     */
    @Override
    public Address selectAddressById(Integer aid) {
        return iAddressMapper.selectById(aid);
    }
}
