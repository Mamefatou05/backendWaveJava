package com.backendwave.web.dto.request.users;


import lombok.*;

@Setter
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Login implements LoginDep {
    private String username;
    private String password;
}
