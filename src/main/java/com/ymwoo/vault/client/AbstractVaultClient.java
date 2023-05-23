package com.ymwoo.vault.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * #참고# vault 어드민 UI에 환경별로 경로가 존재해야 프로세스 기동이 가능 > 없는 경우 기동실패 됨
 * @param <Dto>
 */
@Slf4j
public abstract class AbstractVaultClient<Dto> implements InitializingBean, ApplicationContextAware {

    @Autowired
    private VaultTemplate vaultTemplate;

    private Class<Dto> dtoClass;

    private ObjectMapper mapper;

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    @Override
    public void afterPropertiesSet() {
        Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.dtoClass = (Class<Dto>) genericTypes[0];

        mapper = ctx.getBean(ObjectMapper.class);

        if (mapper == null) {
            mapper = new ObjectMapper();
        }
    }

    /**
     * 요청 path
     * ex) app-kv/data/fnd/api/{activeProfile}
     * ex) app-kv/data/fnd/api/dev
     */
    protected abstract String getPath();

    /**
     * vault 통신 후 Dto Type 형태로 응답데이터 return
     * @return
     */
    public Dto getVault() {
        Dto result = null;
        try {
            String path = getPath();
            log.info("Retrieved data from Vault [REQUEST] {}", path);
            VaultResponseSupport<VaultDTO<Dto>> response = (VaultResponseSupport<VaultDTO<Dto>>) vaultTemplate.read(path, combineResponseType());
            result = mapper.convertValue(response.getData().getData(), dtoClass);
        } catch (Exception e) {
            log.error("Retrieved error from Vault >> {}", e.getMessage(), e);
        }
        return result;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class VaultDTO<T> {
        private T data;
    }

    private Class<?> combineResponseType() {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(VaultDTO.class, dtoClass);
        return resolvableType.resolve();
    }
}
