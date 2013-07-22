package eu.inmite.android.fw.validation.forms.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tomas Vondracek
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface RegExp {
	String value();
	int messageId() default 0;
	int order() default 1000;
}



