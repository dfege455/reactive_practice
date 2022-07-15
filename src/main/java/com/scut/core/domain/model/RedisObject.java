package com.scut.core.domain.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RedisObject {
    private String key;
    private String value;
}
