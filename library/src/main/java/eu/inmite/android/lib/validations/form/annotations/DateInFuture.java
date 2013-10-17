package eu.inmite.android.lib.validations.form.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DateFormat;

/**
 * @author Tomas Vondracek
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DateInFuture {
	/**
	 * date pattern that will be used to parse input date
	 */
	String datePattern() default "";

	/**
	 * if no pattern is defined date style will be used to initialize {@link java.text.DateFormat}
	 */
	int dateStyle() default DateFormat.MEDIUM;
	int messageId() default 0;
	int order() default 1000;
	boolean allowToday() default true;
}
