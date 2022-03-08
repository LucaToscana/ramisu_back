package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Notification")
public class NotificationDAO implements Serializable {
    @EmbeddedId
    private NotificationId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idUser")
    @JoinColumn(name = "id_users")
    UsersInformationDAO user;
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order", nullable=true)
    OrderDAO order;
    
    @JoinColumn(name = "message")
    String message;
    
    
    
    
    
}
