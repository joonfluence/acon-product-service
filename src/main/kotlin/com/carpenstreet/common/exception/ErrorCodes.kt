package com.carpenstreet.common.exception

enum class ErrorCodes(
    val code: String,
    val message: String,
) {
    GLOBAL_ERROR("GLOBAL_001", "요청 처리 중 오류가 발생했습니다. 페이지를 새로 고치거나 잠시 후 다시 시도해 주세요."),
    INVALID_PARAMETER_VALUE("GLOBAL_002", "요청하신 값이 올바르지 않습니다."),
    MISSING_REQUIRED_FIELD("GLOBAL_003", "필수 값이 입력되지 않았습니다."),
    VALIDATION_FAILED("GLOBAL_004", "입력 값이 올바르지 않습니다."),
    BAD_REQUEST("GLOBAL_005", "잘못된 요청입니다."),
    CONSTRAINT_VIOLATION("GLOBAL_006", "제약 조건을 위반했습니다."),
    HAS_NO_TRANSITION_AUTHORITY("PRODUCT_01", "해당 권한으로는 상태 전이가 불가능합니다."),
    PRODUCT_NOT_FOUND("PRODUCT_02", "상품이 존재하지 않습니다."),
    HAS_NO_PRODUCT_EDIT_AUTHORITY("PRODUCT_03", "본인의 상품만 수정할 수 있습니다."),
    HAS_NO_PERMITION_TO_READ("PRODUCT_04", "상품을 조회할 수 없습니다."),
}