package eu.inmite.android.fw.validation.forms.annotations;

import java.lang.annotation.*;

/**
 * Annotation for validators - this way they can specify what validation are they able to perform.
 *
 * @author Tomas Vondracek
 */
@Inherited
@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ValidatorFor {
	Class<? extends Annotation>[] value();
}
