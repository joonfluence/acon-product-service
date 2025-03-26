# ACON Multilingual Product Management API

다국어 기반 전자상거래 플랫폼 ACON에서  
상품의 등록, 상태 전이, 다국어 처리, 사용자 권한 기반 제어 등을 제공하는  
상품 관리 백엔드 서버입니다.

---

## 프로젝트 목적

- 상품 상태에 따라 다양한 사용자 권한을 분기하여 로직을 처리할 수 있어야 함
- 상품 정보를 다국어(한/영/일)로 관리하고, 외부 번역 API를 연동
- 작성 → 검토요청 → 검토중 → 승인/거절 → 재요청 → 판매 완료의 상태 전이 흐름 처리
- 외부 API 실패 대응을 위한 장애 복원 설계 포함 (Retry / CircuitBreaker / Fallback 등)
- 고객은 선택한 언어에 맞게 상품을 조회할 수 있어야 함

---

## 폴더 구조 및 아키텍처 설계

```bash
com.carpenstreet
├── application           # 컨트롤러, 서비스 계층 (요청 처리)
│   ├── product
│   └── user
├── domain                # 핵심 도메인 모델 (Entity, Repository)
│   ├── product
│   └── user
├── client                # 외부 API 연동 (번역, 문자)
├── common                # 공통 예외, 응답 포맷
├── config                # 설정 관련 모듈
└── CarpenstreetApplication.kt
```

확장성을 고려한 설계 구조로, 도메인/계층 분리를 통해 멀티모듈/마이크로서비스 전환에 대비했습니다.

<details>
<summary>멀티모듈 확장 예시</summary>

```bash
modules/
├── product-api          ← application.product.*
├── product-domain       ← domain.product.*
├── user-api
├── user-domain
├── client-external      ← client.*
├── common-lib           ← common.*, config.*
```

</details>

<details>
<summary>MSA 분리 예시</summary>

- product-service
    - /product-api (외부 요청 처리)
    - /product-core (domain + repository + 상태 전이)
- user-service
    - /user-api (회원 관리)
    - /auth-service (JWT 인증 등)
- external-service
    - /translation-service (번역 요청 처리)
    - /notification-service (SMS 전송 등)

</details>

## 설계 전략 및 고려사항

1. 상태 전이 모델링
   - ProductStatus는 enum + transition map 기반으로 유효성 검사 처리
    - 상태에 따라 수정/조회/전환 가능 여부가 달라지도록 명확하게 분리
    - 설계 도식은 /docs/state-diagram.md에 포함

2. 외부 API 연동 설계
   - 번역 API, 문자 전송 API는 client 패키지로 분리
   - Resilience4j를 이용한 Retry, Circuit Breaker 적용
   - 향후 external-service로 마이크로서비스 분리 시 고려된 설계

3. 계층적 구조 분리
   - application / domain / client 계층을 나누어 유지보수성과 재사용성 확보
   - service 레이어는 가능하면 비즈니스 규칙 없이 orchestration 위주로 구성

4. 테스트 및 품질
   - 단위 테스트는 상태 전이 로직, 외부 연동 fallback, controller 단위로 작성
   - 통합 테스트는 API 흐름 중심으로 작성 (추후 REST Doc 가능성 고려)

## 사용 기술

| Layer       | Tech                          |
|-------------|-------------------------------|
| Language    | Kotlin (JVM 21)               |
| Framework   | Spring Boot 3.x               |
| DB          | H2 (in-memory)                |
| Build       | Gradle (Groovy)               |
| Resilience  | Resilience4j                  |
| Test        | JUnit5, Mockk                 |
| 기타        | Spring Validation, H2 Console |


## 초기 실행 방법

```bash
./gradlew clean build
./gradlew bootRun
```

- 접속: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb)

## 가정사항 / 특이사항

- 회원 가입/로그인은 인증 없이 단순한 ID 기반 처리
- 번역 API는 실제 호출 없이 더미 응답으로 대체됨 ("${text}${language}")
- 메시지 전송 API는 실제로는 로그 출력 기반으로 구현됨
- 고객의 언어 선택은 파라미터(?lang=ko|en|ja)로 처리

## 확장 가능성 (멀티모듈/MSA)

- 도메인 단위(product, user), 계층 단위(domain, application, client)를 분리하여 추후 기능별 모듈화 또는 서비스화(MSA)로 쉽게 전환 가능
- external-service는 실제 마이크로서비스로 분리 가능하도록 인터페이스화
- common 패키지는 공통 유틸/예외처리 모듈로 독립 배포도 가능

## 시행착오 및 피드백

- 외부 API 장애 대응 방식 설계 시 @Retryable vs Resilience4j의 선택 고민
- enum 상태 전이 vs 상태 패턴 적용 중 enum으로 단순화 (복잡도 최소화)
- 다국어 데이터 구조 설계 시 ProductContent 분리로 정규화 유지

## 기타

### Auditing 구조 관련

인증 시스템이 없는 환경입니다. 생성자와 수정자 이름에 대해선 "system"을 기본 값으로 설정하였고, Spring Security 환경에서는 SecurityContextHolder 기반으로 확장 가능하도록 AuditorAware를 별도 Bean으로 추상화했습니다. 실무 확장성을 고려한 유연한 설계 구조를 의도하였습니다.

### Country 필드 및 ISO 코드 도입 배경

사용자의 국적을 명시적으로 관리하기 위해 `country` 필드를 도입하고, 국가 식별은 국제 표준인 **ISO 3166-1 Alpha-2 코드(`KR`, `US`, `JP` 등)** 를 사용했습니다. 이는 다국적 사용자 기반 확장, 국가별 정책 분기(언어, 인증, 결제 등), SMS 발송 및 전화번호 포맷 처리와 같은 실무 요구사항을 유연하게 대응하기 위함입니다.

- 레퍼런스 : https://www.itu.int/rec/T-REC-E.164-201011-I/en

### Enum 설계 및 계층 간 사용 전략 

Enum은 도메인 중심으로 설계하되,  Controller와 같은 외부 계층에서 사용할 경우에는  의미가 과도하게 노출되지 않도록 **DTO 변환이나 포장 객체를 통해 안정적으로 노출**합니다. 
이는 도메인 변경이 외부 API에 영향을 주지 않도록 하기 위한 **계층 분리 원칙에 기반한 선택**입니다.

### 도메인 역할 기반 설계

products.partner_id는 users 테이블을 참조하지만, 도메인 상 명확한 역할 표현을 위해 partner_id로 명시하였습니다.

### 가격 필드 설계 이유 (price: DECIMAL)

금액 계산에서의 정밀도를 보장하기 위해 `FLOAT`/`DOUBLE`이 아닌 `BigDecimal`을 사용하였습니다.   
부동소수점 방식은 이진수 기반 근사 표현으로 인해 오차가 발생할 수 있으며, `BigDecimal` + `Decimal`은 이를 방지하기 위한 정확한 10진수 연산 방식을 제공합니다.

### @ManyToOne 

Entity 간 연관관계는 `@ManyToOne`으로 도메인 의미를 명확히 표현하되, 실제 조회 시에는 `QueryDSL fetch join` 또는 DTO 기반 Projection으로 성능을 안정적으로 제어합니다.

### NotNull 조건

Entity 간 연관관계는 `nullable = false`를 명시하여 JPA 레벨에서의 무결성도 보장합니다.  
이는 DB 제약 및 Kotlin 타입 안정성과 함께 3중 안전망을 구성하여, 예측 가능한 구조를 만듭니다.

### 상태 전이 설계 방식 비교 및 선택 이유

이번 과제에서는 `transitionMap` 기반으로 상품 상태 전이 흐름을 구현하였습니다.  
전이 조건이 복잡하지 않고, 명확한 요구사항 기반의 흐름 정의가 가능한 구조이므로  
간결하고 유지보수하기 쉬운 방식이 적절하다고 판단하였습니다.

다만, 추후 상태별로 전이 조건(예: 권한, 시간, 정책 등)이 복잡해지는 경우에는  
State Pattern(상태 클래스 기반 설계)으로 확장하는 것이 바람직합니다.

| 설계 방식 | 장점 | 단점 | 추천 상황 |
|-----------|------|------|-----------|
| `transitionMap` (현재 방식) | 간결하고 테스트 용이<br>Enum 기반으로 직관적<br>정책이 단순할 때 유리 | 권한, 컨텍스트 기반 전이 조건 반영 어려움 | 전이 흐름이 명확하고<br>상태 수가 적은 경우 |
| `State Pattern` | 상태별 전이 조건 내포 가능<br>권한/상황에 따라 유연한 처리 가능<br>테스트 및 유지보수에 강함 | 클래스 수 증가<br>Enum 기반 구조와 충돌<br>과도한 설계가 될 수 있음 | 상태별 전이 정책이 다양하고<br>복잡한 시스템인 경우 |


## 마무리하며

구조적 명료함, 도메인 모델 중심 설계, 장애 복원력 확보라는 세 가지 축을 기준으로 기능 확장성과 안정성을 모두 고려하여 구현하였습니다.