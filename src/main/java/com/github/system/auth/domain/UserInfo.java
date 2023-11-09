package com.github.system.auth.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {

    private Long id;
    private String username;
    private String accessToken;
    private List<String> roles;

}
