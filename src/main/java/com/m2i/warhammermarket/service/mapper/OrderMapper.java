package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public OrderDTO OrderDAOtoOrderDTO(OrderDAO orderDAO) {
        return new OrderDTO(orderDAO);
    }
}
