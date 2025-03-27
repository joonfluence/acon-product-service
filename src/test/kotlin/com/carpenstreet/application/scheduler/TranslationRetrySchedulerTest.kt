package com.carpenstreet.application.scheduler

import com.carpenstreet.client.translation.request.TranslationRequest
import com.carpenstreet.client.translation.response.TranslationResponse
import com.carpenstreet.client.translation.TranslationClient
import com.carpenstreet.common.context.UserContext
import com.carpenstreet.domain.common.enums.Country
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.ProductTranslationEntity
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import com.carpenstreet.domain.translation.entity.TranslationFailureEntity
import com.carpenstreet.domain.translation.repository.TranslationFailureRepository
import com.carpenstreet.domain.user.entity.UserEntity
import com.carpenstreet.domain.user.enums.UserRole
import com.carpenstreet.domain.user.repository.UserRepository
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