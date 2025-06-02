package org.exchange.platform.rest.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AssetTypeValidator::class])
annotation class ValidAssetType(
    val allowFiat: Boolean = true,
    val message: String = "Invalid asset type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)