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

@Entity
@Table(name = "Inhabit")
public class InhabitDAO implements Serializable {
    @Override
	public String toString() {
		return "InhabitDAO [id=" + id + ", isMain=" + isMain + "]";
	}



	@EmbeddedId
    private InhabitId id;

    @Column (name = "is_main")
    private int isMain;
    
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idAddress")
    @JoinColumn(name = "id_address")
    AddressDAO address;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idUser")
    @JoinColumn(name = "id_users")
    UsersInformationDAO user;
    
    
    
    public static InhabitDAO getInhabit(AddressDAO address, UsersInformationDAO userInfo, Long userSecID)
    {
    	 InhabitDAO inhabit = new InhabitDAO();
         InhabitId ids = new InhabitId();
         
         ids.setIdAddress(address.getId());
         ids.setIdUser(userSecID);
         inhabit.setId(ids);
         inhabit.setAddress(address);
         inhabit.setUser(userInfo);
         inhabit.setIsMain(1);
    	
		return inhabit;
    	
    }
}
