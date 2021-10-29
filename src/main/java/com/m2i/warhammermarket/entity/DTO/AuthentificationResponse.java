package com.m2i.warhammermarket.entity.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthentificationResponse {
    private String token;
    private String username;
}