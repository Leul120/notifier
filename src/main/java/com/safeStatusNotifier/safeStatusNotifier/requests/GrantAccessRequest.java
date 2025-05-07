package com.safeStatusNotifier.safeStatusNotifier.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrantAccessRequest {
    private String email;
}
