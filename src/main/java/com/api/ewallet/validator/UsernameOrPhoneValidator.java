package com.api.ewallet.validator;

import com.api.ewallet.model.wallet.SendMoneyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameOrPhoneValidator implements ConstraintValidator<UsernameOrPhoneRequired, SendMoneyRequest> {

    @Override
    public boolean isValid(SendMoneyRequest request, ConstraintValidatorContext context) {
        boolean hasUsername = request.getUsername() != null && !request.getUsername().isBlank();
        boolean hasPhone = request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank();
        return (hasUsername || hasPhone) && !(hasUsername && hasPhone);
    }
}
