package org.exchange.platform.rest.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.exchange.platform.service.AssetService
import org.springframework.beans.factory.annotation.Autowired

class AssetExistsValidator(
    @Autowired private val assetService: AssetService,
) : ConstraintValidator<AssetExistsType, String> {
    override fun isValid(assetSymbol: String?, context: ConstraintValidatorContext): Boolean {
        if (assetSymbol.isNullOrBlank()) return false
        try {
            assetService.getAsset(assetSymbol);
            return true;
        } catch (ex: Exception) {
            return false
        }
    }
}