package com.zcs.yunjia.service;

import com.zcs.yunjia.pojo.OrderInfo;
import com.zcs.yunjia.pojo.RequestResult;

public interface OrderService {

    /**
     * 创建订单
     * @return
     */
    public RequestResult createOrder(OrderInfo orderInfo);
}
