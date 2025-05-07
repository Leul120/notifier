package com.safeStatusNotifier.safeStatusNotifier.requests;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest implements Serializable {
    private String name;
    private String email;
    private String password;
    private String phone;
}
