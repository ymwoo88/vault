# Vault 적용 테스트 프로젝트

## KMS?
KMS는 Key Management Service 또는 Key Management System의 약자로  
말그대로 키를 관리하는 서비스 입니다.



---
## vault?
볼트는 HashiCorp 회사에서 개발 된 KMS(Key Management Service)로  
ID기반 비밀키 및 암호화 정보를 관리 시스템

### 주요 기능
#### 1. Secure Secret Storage  
- 키 벨류 형태로 보안정보를 저장하는 기능을 제공합니다.
#### 2. Dynamic Secrets
 - 동적 키 형태로 유효한 권한이 있는 키 1쌍을 생성하여 인증에 활용합니다.
#### 3. Data Encryption
 - 특정 데이터를 암복호화 하는 기능이 제공합니다.
#### 4. Leasing and Renewal
 - 임대 및 갱신 기능은 볼트에 관련 된 모든 키에는 유효기간과 연장하는 기능이 제공 됩니다.
#### 5. Revocation
 - 키 롤링과 유해 침입 시 키를 만료해주거나, 시스템잠금 기능을 제공 합니다.

### 사용 사례
#### 1. 일반 비밀 저장소
- 예를 들어 민감한 환경 변수, 데이터베이스 자격 증명, API 키 등의 정보를 저장
#### 2. 직원 자격 증명 저장
- "일반 비밀 저장소"와 겹치지만 직원이 웹 서비스에 액세스하기 위해 공유하는 자격 증명을 저장
#### 3. 데이터 암호화
- 비밀을 저장할 수 있을 뿐만 아니라 다른 곳에 저장된 데이터를 암호화/복호화하는 데 사용할 수 있습니다.  
이것의 주요 용도는 애플리케이션이 데이터를 기본 데이터 저장소에 저장하는 동안 데이터를 암호화할 수 있도록 하는 것입니다.

### 볼트 관리 방법
#### 1. CLI
- 커맨드 라인 인터페이스 관리
#### 2. HCP
- HashiCorp에서 운영하는 완전 관리형 Vault 플랫폼
#### 3. UI
- Web UI 관리

---
## vault 서버 구축

### 1. vault 사이트
- 튜토리얼 링크  
https://learn.hashicorp.com/vault
- UI 관리 환경 선택   
  https://learn.hashicorp.com/collections/vault/getting-started-ui
- Install (MacOs)  
  https://learn.hashicorp.com/tutorials/vault/getting-started-install?in=vault/getting-started-ui
```
$ brew tap hashicorp/tap

$ brew install hashicorp/tap/vault

$ brew upgrade hashicorp/tap/vault

export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_TOKEN='s.c8y4LUgX3L17wkhDZ8xasS2b'
```
- Web UI 설정 추가  
https://learn.hashicorp.com/tutorials/vault/getting-started-ui?in=vault/getting-started-ui
#### 1) vault UI 컨피그 파일 생성
```
config.hcl <<EOF
ui = true
disable_mlock = true

storage "raft" {
  path    = "./vault/data"
  node_id = "node1"
}

listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = "true"
}

api_addr = "http://127.0.0.1:8200"
cluster_addr = "https://127.0.0.1:8201"
EOF
```
#### 2) vault 데이터 저장 폴더 생성
```
$ mkdir -p vault/data
```
#### 3) vault web UI 실행
```
$ vault server -config=config.hcl
```

---
## vault 설정

### 1. 무작성 보고 따라해보기
https://learn.hashicorp.com/tutorials/vault/getting-started-first-secret?in=vault/getting-started

### 2. 비밀 엔진
https://learn.hashicorp.com/tutorials/vault/getting-started-secrets-engines?in=vault/getting-started

### 3. 정책 적용 및 어플리케이션 접근 토큰 발급
https://learn.hashicorp.com/tutorials/vault/getting-started-policies?in=vault/getting-started



---
## 어플리케이션 vault 적용
### 1. 프로젝트 및 라이브러리 버전  
https://start.spring.io/
- spring boot 2.6.2
- jdk 11
- spring cloud vault-config 3.1.0
- maven 프로젝트

```
JVM OPTION
-DVAULT_ROLE_ID=220ff538-66ce-f350-5d92-960cdd3a6f66
-DVAULT_SECRET_ID=98658b45-d9b8-382c-ce1a-9d1feec4e587


### Vault
spring.config.import: vault://secret/foo
spring.cloud.vault:
  enabled: true
  fail-fast: true
  connection-timeout: 3000
  read-timeout: 3000
  authentication: approle
  app-role:
    app-role-path: "approle"
    role: "my-role"
    role-id: ${VAULT_ROLE_ID}
    secret-id: ${VAULT_SECRET_ID}
  host: 127.0.0.1
  port: 8200
  scheme: http

### Vault db value
robot: ${robot}



@Value("${robot}")
private String robot;

@PostConstruct
public void postConstruct() {
	System.out.println(">>>>>>>>>>>>>>>>>>>> " + robot);
}
```