package com.m2i.warhammermarket.entity.DTO;

import java.io.Serializable;
import java.sql.Date;

import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationDTO implements Serializable {
	
	private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private Date birthdate;
    

    public UserInformationDTO(UsersInformationDAO userInformationDao) {

    	this.id = userInformationDao.getId();
        this.firstName = userInformationDao.getFirstName();
    	this.lastName = userInformationDao.getLastName();
    	this.phone = userInformationDao.getPhone();
    	this.birthdate = userInformationDao.getBirthdate();    	
    }
    
    @Override
    public String toString() {

        return "UserInformationDTO{" +
                "id=" + getId() +
                ", first name='" + getFirstName() + "'" +
                ", last name='" + getLastName() + "'" +
                ", phone='" + getPhone() + "'" +
                ", birthdate ='" + getBirthdate() + "'" +
                "}";
    }

}
