package com.carpenstreet.application.admin.service

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.product.response.ProductWithTranslationsResponse
import com.carpenstreet.application.product.response.TranslationResponse
import com.carpenstreet.application.product.response.UserResponse
import com.carpenstreet.common.exception.BadRequestException
import com.carpenstreet.common.exception.ErrorCodes
import com.carpenstreet.common.extension.findByIdOrThrow
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.product.repository.ProductRepository
import com.carpenstreet.domain.product.repository.ProductReviewHistoryRepository
import com.carpenstreet.domain.product.repository.ProductTranslationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductAdminQueryService(
    private val productRepository: ProductRepository,
    private val productTranslationRepository: ProductTranslationRepository,
    private val productReviewHistoryRepository: ProductReviewHistoryRepository,
) {
    fun getProducts(
        request: AdminProductGetRequest,
        pageable: Pageable,
    ): Page<ProductWithTranslationsResponse> {
        val products = productRepository.findAllWithTranslations(request, pageable)
        val totalCount = productRepository.countDistinctProducts(request)

        val translationsResponses = products.groupBy { it.id }.map { (productId, items) ->
            val first = items.first()
            ProductWithTranslationsResponse(
                id = productId,
                price = first.price,
                status = first.status,
                partner = UserResponse.from(first.partner),
                translations = items.map {
                    TranslationResponse(it.language, it.title, it.description)
                }
            )
        }

        return PageImpl(
            translationsResponses,
            pageable,
            totalCount,
        )
    }

    fun getProductDetail(
        productId: Long,
    ): ProductEntity {
        val product =
            productRepository.findByIdOrThrow(productId, BadRequestException(ErrorCodes.PRODUCT_NOT_FOUND));

        val current =
            productTranslationRepository.findByProductIdAndLanguage(product.id, Language.KO)
        val rejected = productReviewHistoryRepository
            .findByProductIdAndNewStatus(productId, ProductStatus.REJECTED)

        val isModifiedSinceRejection = rejected != null &&
                (rejected.snapshotTitleKo != current.title ||
                        rejected.snapshotDescriptionKo != current.description) &&
                product.status == ProductStatus.REJECTED

        if (!isModifiedSinceRejection && product.status != ProductStatus.REQUESTED) {
            throw BadRequestException(ErrorCodes.HAS_NO_PERMITION_TO_READ)
        }

        return product
    }
}