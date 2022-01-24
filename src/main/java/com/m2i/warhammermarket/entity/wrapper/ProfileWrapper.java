package com.m2i.warhammermarket.entity.wrapper;

import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileWrapper {
    private String lastName;
    private String firstName;
    private String phone;
    private String mail;
    private Date birthdate;
    private String number;
    private String street;
    private String additionalAddress;
    private String postalCode;
    private String city;
    private String country;

    public ProfileWrapper (UsersInformationDAO user, AddressDAO address){
    	  lastName = user.getLastName();
          firstName = user.getFirstName();
          phone = user.getPhone();
          mail = user.getUser().getMail();
          birthdate = user.getBirthdate();
          number = address.getNumber();
          street = address.getStreet();
          additionalAddress = address.getAdditionalAddress();
          postalCode = address.getPostalCode();
          city = address.getCity();
          country = address.getCountry();
    }
    


    public UsersInformationDAO getUserInformations()
    {

  	  UsersInformationDAO user = new UsersInformationDAO();
  	  user.setLastName(this.getLastName());
  	  user.setFirstName(this.getFirstName());
  	  user.setPhone(this.getPhone());
  	  user.setBirthdate( this.getBirthdate());
  	  
  	  
  	return user;
    }
    
    public AddressDAO getAddress()
    {
	  	AddressDAO address = new AddressDAO();
	  	address.setNumber(getNumber());
	  	address.setStreet(getStreet());
	  	address.setAdditionalAddress(getAdditionalAddress());
	  	address.setPostalCode(getPostalCode());
	  	address.setCountry(getCountry());
	  	address.setCity(getCity());
	  	return address;
    }
    
}
