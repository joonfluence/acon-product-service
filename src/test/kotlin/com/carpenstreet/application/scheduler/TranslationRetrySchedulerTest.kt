package com.commerce.application.scheduler

import com.commerce.client.translation.request.TranslationRequest
import com.commerce.client.translation.response.TranslationResponse
import com.commerce.client.translation.TranslationClient
import com.commerce.common.context.UserContext
import com.commerce.domain.common.enums.Country
import com.commerce.domain.common.enums.Language
import com.commerce.domain.product.entity.ProductEntity
import com.commerce.domain.product.entity.ProductTranslationEntity
import com.commerce.domain.product.repository.ProductRepository
import com.commerce.domain.product.repository.ProductTranslationRepository
import com.commerce.domain.translation.entity.TranslationFailureEntity
import com.commerce.domain.translation.repository.TranslationFailureRepository
import com.commerce.domain.user.entity.UserEntity
import com.commerce.domain.user.enums.UserRole
import com.commerce.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.math.BigDecimal

@SpringBootTest
@Transactional
class TranslationRetrySchedulerTest @Autowired constructor(
    private val failureRepository: TranslationFailureRepository,
    private val translationRepository: ProductTranslationRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val scheduler: TranslationRetryScheduler,
) {

    @MockitoBean
    lateinit var translationClient: TranslationClient

    private lateinit var partner: UserEntity

    @BeforeEach
    fun setUp() {
        partner = createPartner()
        userRepository.save(partner)
        UserContext.set(partner)
    }

    @Test
    fun `번역 실패 이력이 재처리되면 삭제되고 번역이 저장된다`() {
        // given
        val product = createProduct(partner)
        createTranslation(product, Language.EN)
        val failure = createFailure(product.id, Language.EN)

        Mockito.`when`(translationClient.translate(
            TranslationRequest(
                language = Language.EN,
                title = "수정된 영어 번역 제목",
                description = "수정된 영어 번역 설명",
            )
        ))
            .thenReturn(
                TranslationResponse(
                    language = Language.EN,
                    title = failure.title,
                    description = failure.description
                )
            )

        // when
        scheduler.retryFailedTranslations()

        // then
        val updated = translationRepository.findByProductIdAndLanguage(product.id, Language.EN)
        assertThat(updated).isNotNull
        assertThat(updated.title).isEqualTo("수정된 영어 번역 제목")
        assertThat(failureRepository.findById(failure.id)).isEmpty
    }

    private fun createPartner() = UserEntity(
        id = 1L,
        name = "작가1",
        phone = "010-1111-2222",
        role = UserRole.PARTNER,
        country = Country.KR
    )

    private fun createProduct(partner: UserEntity) = productRepository.save(
        ProductEntity(
            price = BigDecimal("10000"),
            partner = partner
        )
    )

    private fun createTranslation(product: ProductEntity, lang: Language) = translationRepository.save(
        ProductTranslationEntity(
            product = product,
            language = lang,
            title = "영어 번역 제목",
            description = "영어 번역 설명"
        )
    )

    private fun createFailure(productId: Long, lang: Language) = failureRepository.save(
        TranslationFailureEntity(
            productId = productId,
            targetLanguage = lang,
            title = "수정된 영어 번역 제목",
            description = "수정된 영어 번역 설명",
            reason = "timeout"
        )
    )
}