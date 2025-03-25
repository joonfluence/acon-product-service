package com.carpenstreet.common.extension

import org.springframework.data.repository.CrudRepository

fun <T, ID> CrudRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id!!).orElse(null)

fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(
    id: ID,
    e: Exception,
): T = findById(id!!).orElseThrow { e }