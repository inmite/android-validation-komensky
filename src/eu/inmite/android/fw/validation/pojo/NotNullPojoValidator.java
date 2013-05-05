package eu.inmite.android.fw.validation.pojo;

import javax.validation.constrains.NotNull;
import java.lang.reflect.Field;

/**
 * @author Tomáš Kypta
 * @since 04/05/2013
 */
public class NotNullPojoValidator implements IPojoValidator {

	@Override
	public boolean validateField(Object object, Field field) {
		NotNull notNull = (NotNull) field.getAnnotation(NotNull.class);
		if (notNull != null) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (fieldValue == null) {
					return false;
				}
			} catch (IllegalArgumentException e) {

			} catch (IllegalAccessException e) {

			}
		}
		return true;
	}

}
