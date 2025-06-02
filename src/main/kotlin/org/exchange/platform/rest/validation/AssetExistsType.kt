package org.exchange.platform.rest.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AssetExistsValidator::class])
annotation class AssetExistsType(
    val message: String = "Invalid asset",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)