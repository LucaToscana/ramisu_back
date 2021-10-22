package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long id;
    private String mail;
    private Date dateOfCreation;
    private boolean active;
    private String token;

    public UserDTO(User user) {

        this.id = user.getId();
        this.mail = user.getMail();
        this.dateOfCreation = user.getDateOfCreation();
        this.active = user.isActive();
        this.token = user.getToken();
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
