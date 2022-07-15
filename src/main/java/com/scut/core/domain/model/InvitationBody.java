package com.scut.core.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class InvitationBody {
    private String code;
    private String role;
}
