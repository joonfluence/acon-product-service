package com.carpenstreet.domain.product.repository

import com.carpenstreet.application.admin.request.AdminProductGetRequest
import com.carpenstreet.application.product.dto.ProductUserProjection
import com.carpenstreet.application.product.dto.QProductUserProjection
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
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ProductRepositoryCustomImpl : ProductRepositoryCustom,
    QuerydslRepositorySupport(ProductEntity::class.java) {

    val product = QProductEntity.productEntity
    val user = QUserEntity.userEntity
    val translation = QProductTranslationEntity.productTranslationEntity

    override fun findAll(
        request: ProductGetRequest,
        pageable: Pageable,
    ): Page<ProductUserProjection> {
        val sort = pageable.sort.firstOrNull { it.property == "createdAt" }
        val orderSpecifier = when (sort?.direction) {
            Sort.Direction.ASC -> product.createdAt.asc()
            else -> product.createdAt.desc()  // 기본값 DESC
        }

        val query = from(product)
            .select(
                QProductUserProjection(
                    product.id,
                    product.price,
                    product.status,
                    translation.language,
                    translation.title,
                    translation.description,
                    user
                )
            )
            .innerJoin(product.partner, user)
            .leftJoin(translation).on(product.id.eq(translation.product.id))
            .where(
                product.status.eq(
                    ProductStatus.APPROVED
                ),
                translation.language.eq(request.language),
                request.partnerName?.let { user.name.contains(it) },
                request.title?.let { translation.title.contains(it) },
            )
            .orderBy(orderSpecifier)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
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

    override fun findAllWithTranslations(
        request: AdminProductGetRequest,
        pageable: Pageable,
    ): List<ProductUserProjection> {
        val sort = pageable.sort.firstOrNull { it.property == "createdAt" }
        val orderSpecifier = when (sort?.direction) {
            Sort.Direction.ASC -> product.createdAt.asc()
            else -> product.createdAt.desc()  // 기본값 DESC
        }
        val query = from(product)
            .select(
                QProductUserProjection(
                    product.id,
                    product.price,
                    product.status,
                    translation.language,
                    translation.title,
                    translation.description,
                    user
                )
            )
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
            .orderBy(orderSpecifier)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .distinct()

        val content = query.fetch()
        return content
    }

    override fun countDistinctProducts(request: AdminProductGetRequest): Long {
        return from(product)
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
                    translation.title.contains(it) // 언어 조건 없이 전체 title 검색
                }
            )
            .distinct()
            .fetch()
            .map { it } // List<Long> 형태
            .distinct()
            .count()
            .toLong()
    }
}