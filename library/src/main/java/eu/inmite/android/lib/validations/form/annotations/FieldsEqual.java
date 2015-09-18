package eu.inmite.android.lib.validations.form.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validate that 2 or more field values are all equal to each other
 * @author Andrew Watson
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface FieldsEqual {

    /**
     * Ids of all views that will need to be equal to pass validation.
     */
    int[] fields();
    int messageId() default 0;
    int order() default 1000;
}
