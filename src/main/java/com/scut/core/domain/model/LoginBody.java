package com.scut.core.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class LoginBody {
    private String name;
    private String password;
}
