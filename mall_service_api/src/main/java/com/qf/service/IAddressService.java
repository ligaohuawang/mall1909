package com.qf.service;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {
    /**
     * 根据用户id查询该用户的所有收货地址
     *
     * @param uid
     * @return
     */
    List<Address> selectAllAddressByUid(Integer uid);

    /**
     * 根据地址id查询地址信息
     *
     * @return
     */
    Address selectAddressById(Integer aid);
}
