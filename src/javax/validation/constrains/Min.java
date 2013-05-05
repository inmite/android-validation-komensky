package javax.validation.constrains;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tomáš Kypta
 * @since 17/04/2013
 */
@Target(value= ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Min {
	long value();
}
