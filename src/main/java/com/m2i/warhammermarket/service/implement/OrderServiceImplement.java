package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.*;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.repository.*;
import com.m2i.warhammermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImplement implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private LineOfOrderRepository lineOfOrderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createOrder(List<ProductOrderWrapper> productsOrder, String login) {
            OrderDAO order = orderRepository.save(order(productsOrder,login));
            for (ProductOrderWrapper p:productsOrder) {
                lineOfOrderRepository.save(
                        new LineOfOrderDAO(new LineOfOrderId(p.getId(), order.getId()),p.getQuantite()));
                ProductDAO product = productRepository.getById(p.getId());
                product.setStock(product.getStock()-p.getQuantite());
                productRepository.save(product);
            }
    }

    /**
     * test if the quantity ordered is less than or equal to the stock
     * @param productsOrder list product ordered
     * @return boolean
     * @author Brahim TALLA
     */
    @Override
    public boolean checkStock(List<ProductOrderWrapper> productsOrder) {
        boolean verif = true;
        int i = 0;
        do {
                ProductDAO product = productRepository.getById(productsOrder.get(i).getId());
                if(product.getStock()<productsOrder.get(i).getQuantite())
                    verif = false;

        }while (verif && productsOrder.size()>++i);

        return verif;
    }

    private OrderDAO order(List<ProductOrderWrapper> productsOrder, String login){
        OrderDAO order = new OrderDAO();
        UsersInformationDAO user = userInformationRepository.getByMail(login);
        order.setUser(user);
        order.setDate(new Date(System.currentTimeMillis()));
        order.setTotal(sumTotal(productsOrder));
        StatusDAO status = new StatusDAO();
        status.setId(1L);
        order.setStatus(status);
        return order;
    }
    private BigDecimal sumTotal(List<ProductOrderWrapper> productsOrder){
        BigDecimal total = new BigDecimal(0);
        for (ProductOrderWrapper p:productsOrder) {
            total = total.add(p.getPrice().multiply(new BigDecimal(p.getQuantite())));
        }
        return total.multiply(BigDecimal.valueOf(1.20));
    }
}
