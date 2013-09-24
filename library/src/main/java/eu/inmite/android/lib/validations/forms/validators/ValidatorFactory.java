package eu.inmite.android.lib.validations.forms.validators;

import android.support.v4.util.LruCache;
import eu.inmite.android.lib.validations.forms.annotations.ValidatorFor;
import eu.inmite.android.lib.validations.forms.iface.IValidator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomas Vondracek
 */
public class ValidatorFactory {

	private static final int INSTANCE_CACHE_SIZE = 4;

	private static final LruCache<Class<? extends IValidator>, IValidator> sCachedValidatorInstances = new LruCache<Class<? extends IValidator>, IValidator>(INSTANCE_CACHE_SIZE);
	private static final Map<Class<? extends Annotation>, Class<? extends IValidator>> sValidators = new HashMap<Class<? extends Annotation>, Class<? extends IValidator>>();

	static {
		// our default validators:

		//noinspection unchecked
		registerValidatorClasses(
				CustomValidator.class,
				LengthValidator.class,
				NumberValueValidator.class,
				LengthValidator.class,
				ValueValidator.class,
				NumberValueValidator.class,
				LengthValidator.class,
				ValueValidator.class,
				NotEmptyValidator.class,
				DateValidator.class,
				RegExpValidator.class);
	}

	public static void registerValidatorClasses(Class<? extends IValidator>... classes) {
		if (classes == null || classes.length == 0) {
			return;
		}

		for (Class<? extends IValidator> clazz : classes) {
			final Annotation[] annotations = clazz.getAnnotations();

			// search for @ValidatorFor annotation and read supported validations
			for (Annotation annotation : annotations) {
				if (annotation instanceof ValidatorFor) {
					Class<? extends Annotation>[] validationAnnotations = ((ValidatorFor) annotation).value();
					for (Class<? extends Annotation> validationAnnotation : validationAnnotations) {
						sValidators.put(validationAnnotation, clazz);
					}
					break;
				}
			}
		}
	}

	public static IValidator getValidator(Annotation annotation) throws IllegalAccessException, InstantiationException {
		if (annotation == null) {
			return null;
		}

		final Class<? extends IValidator> clazz = sValidators.get(annotation.annotationType());

		IValidator validator = null;
		if (clazz != null) {
			validator = sCachedValidatorInstances.get(clazz);
			if (validator == null) {
				validator = clazz.newInstance();
				sCachedValidatorInstances.put(clazz, validator);
			}
		}
		return validator;
	}
}
