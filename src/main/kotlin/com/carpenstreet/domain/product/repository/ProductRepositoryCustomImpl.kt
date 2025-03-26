package com.carpenstreet.domain.product.repository

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.product.request.ProductGetRequest
import com.carpenstreet.domain.common.enums.Language
import com.carpenstreet.domain.product.entity.ProductEntity
import com.carpenstreet.domain.product.entity.QProductEntity
import com.carpenstreet.domain.product.entity.QProductTranslationEntity
import com.carpenstreet.domain.product.enums.ProductStatus
import com.carpenstreet.domain.user.entity.QUserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ProductRepositoryCustomImpl : ProductRepositoryCustom,
    QuerydslRepositorySupport(ProductEntity::class.java) {

    val product = QProductEntity.productEntity
    val user = QUserEntity.userEntity
    val translation = QProductTranslationEntity.productTranslationEntity

    override fun findAll(
        request: ProductGetRequest,
        pageable: Pageable,
    ): Page<ProductEntity> {
        val query = from(product)
            .select(product)
            .innerJoin(product.partner, user)
            .leftJoin(translation).on(product.id.eq(translation.product.id))
            .where(
                product.status.eq(
                    ProductStatus.APPROVED
                ),
                request.partnerName?.let { user.name.contains(it) },
                request.title?.let {
                    translation.language.eq(Language.KO)
                        .and(translation.title.contains(it))
                }
            )
            .distinct()

        val content = query.fetch()

        val count = from(product)
            .select(product.id)
            .innerJoin(product.partner, user)
            .leftJoin(translation).on(product.id.eq(translation.product.id))
            .where(
                product.status.eq(
                    ProductStatus.APPROVED
                ),
                request.partnerName?.let { user.name.contains(it) },
                request.title?.let {
                    translation.language.eq(Language.KO)
                        .and(translation.title.contains(it))
                }
            )
            .distinct()
            .fetchCount()

        return PageImpl(content, pageable, count)
    }

    // TODO : Projection 활용하여 화면에 필요한 정보 조합하기
    override fun findAllWithTranslations(
        request: AdminProductGetRequest,
        pageable: Pageable,
    ): Page<ProductEntity> {
        val query = from(product)
                .select(product)
                .innerJoin(product.partner, user)
                .leftJoin(translation).on(product.id.eq(translation.product.id))
                .where(
                    product.status.`in`(
                        ProductStatus.REQUESTED,
                        ProductStatus.REVIEWING,
                        ProductStatus.REJECTED,
                        ProductStatus.APPROVED
                    ),
                    request.status?.let { product.status.eq(it) },
                    request.partnerName?.let { user.name.contains(it) },
                    request.title?.let {
                        translation.language.eq(Language.KO)
                            .and(translation.title.contains(it))
                    }
                )
                .distinct()

        val content = query.fetch()

        val count = from(product)
            .select(product.id)
            .innerJoin(product.partner, user)
            .leftJoin(translation).on(product.id.eq(translation.product.id))
            .where(
                product.status.`in`(
                    ProductStatus.REQUESTED,
                    ProductStatus.REVIEWING,
                    ProductStatus.REJECTED,
                    ProductStatus.APPROVED
                ),
                request.status?.let { product.status.eq(it) },
                request.partnerName?.let { user.name.contains(it) },
                request.title?.let {
                    translation.language.eq(Language.KO)
                        .and(translation.title.contains(it))
                }
            )
            .distinct()
            .fetchCount()

        return PageImpl(content, pageable, count)
    }
}