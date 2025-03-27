sequenceDiagram
    participant Admin as 매니저
    participant ProductCommandService
    participant EventPublisher
    participant ProductTranslationEventHandler
    participant TranslationClient
    participant TranslationFailureRepository
    participant SmsNotificationEventHandler
    participant NotificationClient
    participant NotificationFailureRepository

    Admin->>ProductCommandService: 상품 상태 변경 요청 (→ REVIEWING)
    ProductCommandService->>EventPublisher: ProductTranslationEvent 발행
    ProductCommandService->>EventPublisher: SmsNotificationEvent 발행

    EventPublisher-->>ProductTranslationEventHandler: 비동기 번역 이벤트 처리
    ProductTranslationEventHandler->>TranslationClient: 번역 API 호출
    alt 번역 실패
        TranslationClient-->>ProductTranslationEventHandler: 예외 발생
        ProductTranslationEventHandler->>TranslationFailureRepository: 실패 이력 저장
    else 번역 성공
        TranslationClient-->>ProductTranslationEventHandler: 번역 결과 반환
        ProductTranslationEventHandler->>ProductTranslationRepository: 번역 저장
    end

    EventPublisher-->>SmsNotificationEventHandler: 비동기 문자 이벤트 처리
    SmsNotificationEventHandler->>NotificationClient: 문자 발송 API 호출
    alt 문자 실패
        NotificationClient-->>SmsNotificationEventHandler: 예외 발생
        SmsNotificationEventHandler->>NotificationFailureRepository: 실패 이력 저장
    else 문자 성공
        NotificationClient-->>SmsNotificationEventHandler: 성공 응답
    end
