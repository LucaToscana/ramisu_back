package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.UserDAO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private List<String> authorities;

    public UserDTO(UserDAO user) {

        this.id = user.getId();
        this.mail = user.getMail();
        this.dateOfCreation = user.getDateOfCreation();
        this.active = user.isActive();
        this.token = user.getToken();
        this.tokenExpiryDate = user.getTokenExpiryDate();
    }

    public UserDTO(Long id, String mail, List<String> authorities) {
        this.id = id;
        this.mail = mail;
        this.authorities = authorities;
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
