package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    private Long id;
    private Date date;
    private BigDecimal total;
    private UsersInformationDAO usersInformation;
    private StatusDAO status;
    private AddressDAO livraisonAddress;
    private LineOfOrderDAO lineOfOrderDAO;
    
    
    
    public OrderDTO(OrderDAO orderDAO) {
        this.id = orderDAO.getId();
        this.date = orderDAO.getDate();
        this.total = lineOfOrderDAO.getOrderTotal();
        this.usersInformation = orderDAO.getUser();
        this.status = orderDAO.getStatus();
        this.livraisonAddress= orderDAO.getAddress();
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + getId() +
                ", date=" + getDate() +
                ", total=" + getTotal() +
                ", usersInformation=" + getUsersInformation() +
                ", status=" + getStatus() +
                '}';
    }
}
