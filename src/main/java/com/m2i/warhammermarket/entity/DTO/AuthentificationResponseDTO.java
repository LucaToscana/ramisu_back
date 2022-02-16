package com.m2i.warhammermarket.entity.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthentificationResponseDTO {
    private String token;
    private String mail;
    private String errorMessage;
}