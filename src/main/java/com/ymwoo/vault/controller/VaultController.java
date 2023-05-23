package com.ymwoo.vault.controller;

import com.ymwoo.vault.client.UserVaultClient;
import com.ymwoo.vault.model.UserVaultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VaultController {

    private final UserVaultClient userVaultClient;

    @GetMapping("/test")
    public UserVaultDto test() {
        return userVaultClient.getVault();
    }
}
