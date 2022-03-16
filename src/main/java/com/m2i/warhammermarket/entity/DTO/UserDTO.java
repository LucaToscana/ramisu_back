package com.m2i.warhammermarket.entity.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO implements Serializable {


    private Long id;
    private String mail;
    private Date dateOfCreation;
    private boolean active;
    private String token;
    private Date tokenExpiryDate;
    @JsonIgnore
    private Set<AuthorityDAO> authorities;


    public UserDTO(UserDAO user) {

        this.id = user.getId();
        this.mail = user.getMail();
        this.dateOfCreation = user.getDateOfCreation();
        this.active = user.isActive();
        this.token = user.getToken();
        this.tokenExpiryDate = user.getTokenExpiryDate();
    }

    public String getDateDDMMYYYY()
    {
        return new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(dateOfCreation);
    }
    
    public String getRoles()
    {
//    	String str = String.join("-", authorities.get(0).ge);
//    	authorities.stream().map(null);
    	return authorities.stream()
    			  .map(AuthorityDAO::getAuthority)
    			  .collect(Collectors.joining(",")); // "John, Anna, Paul"
    }

    public void setAuthorities(Set<AuthorityDAO> set)
    {
    	this.authorities = set;
    }
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + getId() +
                ", mail='" + getMail() + "'" +
                ", date of creation='" + getDateOfCreation() + "'" +
                ", active='" + isActive() + "'" +
                ", token='" + getToken() + "'" +
                "}";
    }
}
