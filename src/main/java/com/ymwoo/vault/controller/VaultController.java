package com.ymwoo.vault.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VaultController {

    private final VaultTemplate vaultTemplate;

    @GetMapping("/test")
    public List<VaultDTO> test() {
        log.info("test start~~~");

        List<String> pathList = Arrays.asList(
                "app-kv/data/fnd/api/vault"
        );
        List<VaultDTO> result = new ArrayList<>();
        pathList.forEach(s -> {
            try {
                VaultResponseSupport<VaultDTO> response = vaultTemplate.read(s, VaultDTO.class);
                log.info("Retrieved data {} from Vault", response.getData());
                result.add(response.getData());
            } catch (Exception e) {
                log.error("error = {}", e.getMessage());
            }
        });

        return result;
    }

    @Getter
    @Setter
    @ToString
    public static class VaultDTO {
        private Data data;
    }

    @Getter
    @Setter
    @ToString
    public static class Data {
        private String username;
        private String password;
        @JsonAlias("fnd.database")
        private String fndDatabase;
    }
}
