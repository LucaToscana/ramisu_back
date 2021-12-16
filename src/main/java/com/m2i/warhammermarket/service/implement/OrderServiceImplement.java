package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.*;
import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.repository.*;
import com.m2i.warhammermarket.service.OrderService;
import com.m2i.warhammermarket.service.mapper.OrderMapper;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;

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

    @Override
    public List<OrderDTO> findAllByUserId(Long id) {
        List<OrderDAO> orderDAOS = orderRepository.findAllByUserUserId(id);
        return orderDAOS.stream().map(orderDAO->orderMapper.OrderDAOtoOrderDTO(orderDAO)).collect(Collectors.toList());
    }

    @Override
    public List<ProductOrderWrapper> findAllByOrderId(Long id) {
        List<LineOfOrderDAO> lineOfOrderDAOS = lineOfOrderRepository.findAllByIdIdOrder(id);
        List<ProductOrderWrapper> productOrderWrappers = lineOfOrderDAOS.stream().map(lineOfOrderDAO -> {
                    Integer quantity = lineOfOrderDAO.getQuantity();
                    Long idProduct = lineOfOrderDAO.getId().getId();
                    Optional<ProductDAO> productDAO = productRepository.findById(idProduct);
                    if(productDAO.isPresent()) {
                        return new ProductOrderWrapper(productDAO.get().getId(), productDAO.get().getLabel(), productDAO.get().getPrice(), 0, quantity);
                    } else {
                        return null;
                    }
                }).collect(Collectors.toList());
        return productOrderWrappers;
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
