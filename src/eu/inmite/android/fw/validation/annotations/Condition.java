package eu.inmite.android.fw.validation.annotations;

import eu.inmite.android.fw.validation.iface.ICondition;

import java.lang.annotation.*;

/**
 * This annotation makes conditioned validation possible. It can be attached to single validation or all on field.
 *
 * @author Tomas Vondracek
 */
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Condition {

	Class<? extends ICondition> value();

	/**
	 * if there is no target annotation mentioned we will apply condition for all annotations
	 */
	Class<? extends Annotation> validationAnnotation() default Condition.class;
	int viewId();
}
