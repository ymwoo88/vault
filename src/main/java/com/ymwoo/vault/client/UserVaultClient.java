package com.ymwoo.vault.client;

import com.ymwoo.vault.model.UserVaultDto;
import org.springframework.stereotype.Component;

@Component
public class UserVaultClient extends AbstractVaultClient<UserVaultDto> {
    @Override
    protected String getPath() {
        return "app-kv/data/fnd/api/vault";
    }
}
