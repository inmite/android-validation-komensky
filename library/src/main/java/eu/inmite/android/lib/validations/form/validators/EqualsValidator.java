package eu.inmite.android.lib.validations.form.validators;

import android.util.Log;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;
import eu.inmite.android.lib.validations.form.annotations.ValidatorFor;

/**
 * @author Andrew Watson
 */
@ValidatorFor(FieldsEqual.class)
public class EqualsValidator extends BaseValidator<String[]> {

    @Override
    public boolean validate(Annotation annotation, String[] fieldValues) {
        if (fieldValues == null || fieldValues.length < 2) {
            return false;
        }

        Log.v("Andrew", "You got here");

        return false;
    }
}
