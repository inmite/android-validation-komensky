package eu.inmite.android.fw.validation.pojo.validators;

import javax.validation.constraints.Min;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author Tomáš Kypta
 * @since 04/05/2013
 */
public class MinPojoValidator implements IPojoValidator {

	@Override
	public boolean validateField(Object object, Field field) {
		Min min = (Min) field.getAnnotation(Min.class);
		if (min != null) {
			try {
				field.setAccessible(true);
				if (field.getType().isPrimitive()) {
					if (field.getType().equals(Long.TYPE)) {
						long val = field.getLong(object);
						if (min.value() > val) {
							return false;
						}
					} else if (field.getType().equals(Integer.TYPE)) {
						int val = field.getInt(object);
						if (min.value() > val) {
							return false;
						}
					}
					// TODO add other primitive types
				} else {
					Object fieldValue = field.get(object);
					if (fieldValue != null) {
						if (fieldValue instanceof BigDecimal) {
							BigDecimal val = (BigDecimal) fieldValue;
							if (new BigDecimal(min.value()).compareTo(val) > 0) {
								return false;
							}
						} else if (fieldValue instanceof Long) {
							Long val = (Long) fieldValue;
							if (min.value() > val) {
								return false;
							}
						} else if (fieldValue instanceof Integer) {
							Integer val = (Integer) fieldValue;
							if (min.value() > val) {
								return false;
							}
						}
						// TODO add other numerical types
					}
				}
			} catch (IllegalArgumentException e) {

			} catch (IllegalAccessException e) {

			}
		}
		return true;
	}
}
