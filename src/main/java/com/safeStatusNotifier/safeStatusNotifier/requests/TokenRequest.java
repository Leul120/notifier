package com.safeStatusNotifier.safeStatusNotifier.requests;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenRequest implements Serializable {
    private String credential;
}
