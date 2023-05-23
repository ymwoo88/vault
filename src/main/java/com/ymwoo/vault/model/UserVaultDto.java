package com.ymwoo.vault.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVaultDto {
    private String username;
    private String password;
    @JsonAlias("fnd.database")
    private String fndDatabase;
}
