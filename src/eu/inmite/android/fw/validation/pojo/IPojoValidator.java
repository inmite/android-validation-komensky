package eu.inmite.android.fw.validation.pojo;

import java.lang.reflect.Field;

/**
 * @author Tomáš Kypta
 * @since 04/05/2013
 */
public interface IPojoValidator {

	boolean validateField(Object object, Field field);
}
