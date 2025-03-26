package com.carpenstreet.application.scheduler

import com.carpenstreet.application.admin.response.NotificationResponse
import com.carpenstreet.client.notification.NotificationClient
import com.carpenstreet.client.notification.dto.SmsRequest
import com.carpenstreet.common.context.UserContext
import com.carpenstreet.domain.common.enums.Country
import com.carpenstreet.domain.notification.entity.NotificationFailureEntity
import com.carpenstreet.domain.notification.repository.NotificationFailureRepository
import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.enums.UserRole
import com.carpenstreet.domain.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class SmsRetrySchedulerTest @Autowired constructor(
    private val failureRepository: NotificationFailureRepository,
    private val userRepository: UserRepository,
    private val scheduler: NotificationRetryScheduler,
) {

    @MockitoBean
    lateinit var notificationClient: NotificationClient
    private lateinit var user: UserEntity

    @BeforeEach
    fun setUp() {
        val phone = "010-1234-5678"
        val message = "문자 테스트입니다."

        user = createAdmin()
        userRepository.save(user)
        UserContext.set(user)

        failureRepository.save(
            NotificationFailureEntity(
                phone = phone,
                message = message,
                reason = "timeout"
            )
        )

        Mockito.`when`(notificationClient.sendSms(
            SmsRequest(
                phone = phone,
                text = message,
            )
        )).thenReturn(NotificationResponse(result = "success"))
    }

    @Test
    fun `문자 실패 이력이 재처리되면 삭제된다`() {
        // when
        scheduler.retryFailedSms()

        // then
        val remainingFailures = failureRepository.findAll()
        assertThat(remainingFailures)
            .withFailMessage("재발송 성공 시 이력이 삭제되어야 합니다")
            .isEmpty()
    }

    private fun createAdmin() = UserEntity(
        id = 1L,
        name = "관리자1",
        phone = "010-1111-2222",
        role = UserRole.ADMIN,
        country = Country.KR
    )
}
