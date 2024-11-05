package com.backendwave.web.dto.request.users;


import com.mfn.mydependance.interfaces.LoginDep;
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
