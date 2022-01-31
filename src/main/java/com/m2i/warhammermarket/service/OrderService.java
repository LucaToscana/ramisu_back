package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;

import java.util.List;


public interface OrderService {
    void createOrder(List<ProductOrderWrapper> productsOrder,String login);

    boolean checkStock(List<ProductOrderWrapper> productsOrder);

    List<OrderDTO> findAllByUserId(Long id);

    List<ProductOrderWrapper> findAllByOrderId(Long id);

}
