package com.safeStatusNotifier.safeStatusNotifier.requests;


import lombok.Data;

import java.io.Serializable;

@Data
public class SignInRequest implements Serializable {

    private String email;

    private String password;
}
