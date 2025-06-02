package org.exchange.platform.rest.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.exchange.platform.enum.AssetType
import org.exchange.platform.service.AssetService
import org.springframework.beans.factory.annotation.Autowired

class AssetTypeValidator(
    @Autowired private val assetService: AssetService,
) : ConstraintValidator<ValidAssetType, String> {

    private var allowFiat: Boolean = true

    override fun initialize(annotation: ValidAssetType) {
        this.allowFiat = annotation.allowFiat
    }

    override fun isValid(assetSymbol: String?, context: ConstraintValidatorContext): Boolean {
        if (assetSymbol.isNullOrBlank()) return false
        try {
            val asset = assetService.getAsset(assetSymbol)
            return if (allowFiat) {
                asset.type == AssetType.FIAT
            } else {
                asset.type != AssetType.FIAT
            }
        } catch (e: Exception) {
            return false
        }
    }
}