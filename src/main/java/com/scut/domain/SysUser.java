package com.scut.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.io.Serializable;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 427289387893L;

    @Id
    @Column("user_id")
    private Integer userId;

    private String name;

    private String phone;

    private String password;

    private String role;
}
