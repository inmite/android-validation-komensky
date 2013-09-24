package eu.inmite.android.lib.validations.forms.annotations;

import java.lang.annotation.*;

/**
 * Annotation for validators - this way we can specify what validation is validator able to perform.
 *
 * @author Tomas Vondracek
 */
@Inherited
@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ValidatorFor {

	Class<? extends Annotation>[] value();
}
