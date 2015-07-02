package eu.inmite.android.lib.validations.form;

import android.view.View;
import eu.inmite.android.lib.validations.exception.FormsValidationException;
import eu.inmite.android.lib.validations.form.annotations.Condition;
import eu.inmite.android.lib.validations.form.iface.IValidator;
import eu.inmite.android.lib.validations.form.validators.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Finds and cache fields by targets so we don't need to go through reflection all the time.
 *
 * @author Tomas Vondracek
 */
class FieldFinder {

	private static final WeakHashMap<Object, Map<View, FormValidator.FieldInfo>> sCachedFieldsByTarget = new WeakHashMap<>();

	static boolean clearCache() {
		final boolean cleaned = ! sCachedFieldsByTarget.isEmpty();
		sCachedFieldsByTarget.clear();

		return cleaned;
	}

	/**
	 * get map of field information on view for given target
	 */
	static Map<View, FormValidator.FieldInfo> getFieldsForTarget(Object target) {
		Map<View, FormValidator.FieldInfo> infoMap = sCachedFieldsByTarget.get(target);

		if (infoMap != null) {
			for (View view : infoMap.keySet()) {
				// view has been removed from the window - we will need to scan fields of target again
				if (view.getWindowToken() == null) {
					infoMap = null;

					break;
				}
			}
		}

		if (infoMap == null) {
			infoMap = findFieldsToValidate(target);
			sCachedFieldsByTarget.put(target, infoMap);
		}
		return infoMap;
	}

	/**
	 * find fields on target to validate and prepare for their validation
	 */
	@SuppressWarnings("TryWithIdenticalCatches")
	private static Map<View, FormValidator.FieldInfo> findFieldsToValidate(Object target) {
		final Field[] fields = target.getClass().getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return Collections.emptyMap();
		}

		final WeakHashMap<View, FormValidator.FieldInfo> infoMap = new WeakHashMap<>(fields.length);
		for (Field field : fields) {
			final List<FormValidator.ValidationInfo> infos = new ArrayList<>();
			final Annotation[] annotations = field.getDeclaredAnnotations();
			if (annotations.length > 0) {
				if (! View.class.isAssignableFrom(field.getType())) {
					// next field
					continue;
				}
				final View view;
				try {
					field.setAccessible(true);
					view = (View) field.get(target);
				} catch (IllegalAccessException e) {
					throw new FormsValidationException(e);
				}
				if (view == null) {
					continue;
				}

				for (Annotation annotation : annotations) {
					final IValidator validator;
					try {
						validator = ValidatorFactory.getValidator(annotation);
					} catch (IllegalAccessException e) {
						throw new FormsValidationException(e);
					} catch (InstantiationException e) {
						throw new FormsValidationException(e);
					}
					if (validator != null) {
						FormValidator.ValidationInfo info = new FormValidator.ValidationInfo(annotation, validator);
						infos.add(info);
					}
				}

				final Condition conditionAnnotation = field.getAnnotation(Condition.class);
				if (infos.size() > 0) {
					Collections.sort(infos, new Comparator<FormValidator.ValidationInfo>() {
						@Override
						public int compare(FormValidator.ValidationInfo lhs, FormValidator.ValidationInfo rhs) {
							return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
						}
					});
				}
				final FormValidator.FieldInfo fieldInfo = new FormValidator.FieldInfo(conditionAnnotation, infos);
				infoMap.put(view, fieldInfo);
			}
		}
		return infoMap;
	}
}
