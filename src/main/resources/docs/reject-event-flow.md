sequenceDiagram
    participant Admin as 매니저
    participant ProductCommandService
    participant EventPublisher
    participant SmsNotificationEventHandler
    participant NotificationClient
    participant NotificationFailureRepository
    participant TranslationRepository

    Admin->>ProductCommandService: 상태 변경 요청 (→ REJECTED) + 거절 사유
    ProductCommandService->>TranslationRepository: 기존 번역(EN, JA) 삭제
    ProductCommandService->>EventPublisher: SmsNotificationEvent 발행

    EventPublisher-->>SmsNotificationEventHandler: 문자 전송 이벤트 비동기 처리
    SmsNotificationEventHandler->>NotificationClient: 문자 발송 API 호출
    alt 문자 실패
        NotificationClient-->>SmsNotificationEventHandler: 예외 발생
        SmsNotificationEventHandler->>NotificationFailureRepository: 실패 이력 저장
    else 문자 성공
        NotificationClient-->>SmsNotificationEventHandler: 성공 응답
    end
