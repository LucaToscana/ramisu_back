package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @CrossOrigin(origins = "*")
    @Secured(AuthorityConstant.ROLE_USER)
    @PostMapping("/user/addorder")
    public ResponseEntity<Boolean> createOrder(@RequestBody List<ProductOrderWrapper> productsOrder){
        List<ProductOrderWrapper> productsFilter = productsOrder.stream().filter(c -> c.getQuantite() > 0).collect(Collectors.toList());
        if(productsFilter.size() > 0 && orderService.checkStock(productsFilter)){
            try {
                orderService.createOrder(productsFilter,
                SecurityContextHolder.getContext().getAuthentication().getName());
                return ResponseEntity.ok(true);
            }catch (Exception e){
                System.out.println("Exception dans createOrder");
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(false);
    }
}
