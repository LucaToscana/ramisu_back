package com.m2i.warhammermarket.entity.wrapper;

import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileWrapper {
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private Date birthdate;
    private Long idaddress;
    private String number;
    private String street;
    private String additionalAddress;
    private String postalCode;
    private String city;
    private String country;
    private String avatar;
    
   
    

    public ProfileWrapper (UsersInformationDAO user, AddressDAO address){
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phone = user.getPhone();
        mail = user.getUser().getMail();
        birthdate = user.getBirthdate();
        avatar = user.getAvatar();
        idaddress = address.getId();
        number = address.getNumber();
        street = address.getStreet();
        additionalAddress = address.getAdditionalAddress();
        postalCode = address.getPostalCode();
        city = address.getCity();
        country = address.getCountry();
    }

    public String getBirthdateDDMMYYYY()
    {
        try {
            return new SimpleDateFormat("dd-MM-YYYY").format(birthdate);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }


    public UsersInformationDAO getUserInformations()
    {

  	  UsersInformationDAO user = new UsersInformationDAO();
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
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
