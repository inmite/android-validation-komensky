package eu.inmite.android.fw.validation.forms;

import android.view.View;
import eu.inmite.android.fw.validation.exception.FormsValidationException;
import eu.inmite.android.fw.validation.forms.annotations.Condition;
import eu.inmite.android.fw.validation.forms.iface.IValidator;
import eu.inmite.android.fw.validation.forms.validators.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Tomas Vondracek
 */
class FieldFinder {

	private static final WeakHashMap<Object, Map<View, FormsValidator.FieldInfo>> sCachedFieldsByTarget = new WeakHashMap<Object, Map<View, FormsValidator.FieldInfo>>();

	static boolean clearCache() {
		final boolean cleaned = ! sCachedFieldsByTarget.isEmpty();
		sCachedFieldsByTarget.clear();

		return cleaned;
	}

	static Map<View, FormsValidator.FieldInfo> getFieldsForTarget(Object target) {
		Map<View, FormsValidator.FieldInfo> infoMap = sCachedFieldsByTarget.get(target);
		if (infoMap == null) {
			infoMap = findFieldsToValidate(target);
			sCachedFieldsByTarget.put(target, infoMap);
		}
		return infoMap;
	}

	/**
	 * find fields on target to validate and prepare for their validation
	 */
	private static Map<View, FormsValidator.FieldInfo> findFieldsToValidate(Object target) {
		final Field[] fields = target.getClass().getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return Collections.emptyMap();
		}

		final WeakHashMap<View, FormsValidator.FieldInfo> infoMap = new WeakHashMap<View, FormsValidator.FieldInfo>(fields.length);
		for (Field field : fields) {
			final List<FormsValidator.ValidationInfo> infos = new ArrayList<FormsValidator.ValidationInfo>();
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
						FormsValidator.ValidationInfo info = new FormsValidator.ValidationInfo(annotation, validator);
						infos.add(info);
					}
				}

				final Condition conditionAnnotation = field.getAnnotation(Condition.class);
				if (infos.size() > 0) {
					Collections.sort(infos, new Comparator<FormsValidator.ValidationInfo>() {
						@Override
						public int compare(FormsValidator.ValidationInfo lhs, FormsValidator.ValidationInfo rhs) {
							return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
						}
					});
				}
				final FormsValidator.FieldInfo fieldInfo = new FormsValidator.FieldInfo(conditionAnnotation, infos);
				infoMap.put(view, fieldInfo);
			}
		}
		return infoMap;
	}
}
