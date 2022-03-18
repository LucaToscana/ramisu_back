package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DAO.StatusDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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


    public String getDateDDMMYYYY()
    {
        return new SimpleDateFormat("dd-MM-YYYY").format(date);
    }
    
    public OrderDTO(OrderDAO orderDAO) {
        this.id = orderDAO.getId();
        this.date = orderDAO.getDate();
        this.total = orderDAO.getTotal();
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
