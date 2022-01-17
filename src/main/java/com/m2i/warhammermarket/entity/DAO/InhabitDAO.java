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
@Table(name = "Inhabit")
public class InhabitDAO implements Serializable {
    @EmbeddedId
    private InhabitId id;

    @Column (name = "is_main")
    private int isMain;
    
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("address")
    @JoinColumn(name = "id_address")
    AddressDAO address;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("user")
    @JoinColumn(name = "id_users")
    UsersInformationDAO user;
}
