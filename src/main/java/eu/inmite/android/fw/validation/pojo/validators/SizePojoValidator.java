package eu.inmite.android.fw.validation.pojo.validators;

import javax.validation.constraints.Size;
import java.lang.reflect.Field;

/**
 * @author Tomáš Kypta
 * @since 04/05/2013
 */
public class SizePojoValidator implements IPojoValidator {

	@Override
	public boolean validateField(Object object, Field field) {
		Size size = (Size) field.getAnnotation(Size.class);
		if (size != null) {
			try {
				field.setAccessible(true);
				if (field.getType().equals(String.class)) {
					String str = (String) field.get(object);
					if (str != null) {
						if (str.length() < size.min() || str.length() > size.max()) {
							return false;
						}
					}
				}
			} catch (IllegalArgumentException e) {

			} catch (IllegalAccessException e) {

			}
		}
		return true;
	}
}
