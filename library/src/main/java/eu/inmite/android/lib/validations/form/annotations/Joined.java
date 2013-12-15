package eu.inmite.android.lib.validations.form.annotations;

import eu.inmite.android.lib.validations.form.iface.IValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Special case when you need to join several form views and validate them together.
 * For this case, you need specify {@link eu.inmite.android.lib.validations.form.iface.IValidator} class.
 * @author Tomas Vondracek
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Joined {

	/**
	 * View ids of all views that will participate with values passed to validator.
	 */
	int[] value();
	Class<? extends IValidator<String[]>> validator();
	int messageId() default 0;
	int order() default 1000;
}
