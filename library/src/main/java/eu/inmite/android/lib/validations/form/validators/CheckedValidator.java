package eu.inmite.android.lib.validations.form.validators;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.Checked;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

@ValidatorFor({Checked.class})
public class CheckedValidator extends BaseValidator<Boolean> {


    @Override
    public boolean validate(Annotation annotation, Boolean input) {
        return input;
    }
}
