package eu.inmite.android.fw.validation;

import eu.inmite.android.fw.validation.exception.PojoValidationException;
import eu.inmite.android.fw.validation.pojo.*;

import java.lang.reflect.Field;
import java.util.*;

/** Validate the data entity according to API specification
 * 
 * @author Tomáš Kypta
 * @since 17/04/2013
 */
public class PojoValidator {

	private static final LinkedHashMap<String, IPojoValidator> sPojoValidators;

	static {
		sPojoValidators = new LinkedHashMap<String, IPojoValidator>();
		sPojoValidators.put("NotNull", new NotNullPojoValidator());
		sPojoValidators.put("Min", new MinPojoValidator());
		sPojoValidators.put("Max", new MaxPojoValidator());
		sPojoValidators.put("Size", new SizePojoValidator());
	}


	public static void validateEntity(Object entity) throws IllegalArgumentException, PojoValidationException {
		Set<Object> visitedReferences = new HashSet<Object>();
		validateEntity(entity, visitedReferences, true);
	}

	private static void validateEntity(Object entity, Set<Object> visitedReferences, boolean isRoot) throws IllegalArgumentException, PojoValidationException {
		if (entity == null) {
			throw new IllegalArgumentException("Validated entity cannot be null");
		}

		if (entity.getClass().isEnum()) {
			return;
		}
		if (entity.getClass().getName().startsWith("java")) {
			return;
		}

		if (visitedReferences.contains(entity)) {
			return;
		}
		visitedReferences.add(entity);
		
		List<Field> fields = getAllFields(new ArrayList<Field>(), entity.getClass());
		Map<String, List<String>> invalidFields = new LinkedHashMap<String, List<String>>();
		for (Field field : fields) {
			List<String> errorsForField = new ArrayList<String>();

			// run all POJO validators
			for (Map.Entry<String, IPojoValidator> entry : sPojoValidators.entrySet()) {
				if (!entry.getValue().validateField(entity, field)) {
					errorsForField.add(entry.getKey());
				}
			}

			if (!errorsForField.isEmpty()) {
				invalidFields.put(field.getName(), errorsForField);
			}


			Map<String, List<String>> map = validateSubItem(field, entity, visitedReferences);
			if (map != null) {
				invalidFields.putAll(map);
			}

		}

		if (invalidFields.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String fieldName : invalidFields.keySet()) {
				if (sb.length() > 0) {
					sb.append(',');
				}
				sb.append(fieldName);
			}
			throw new PojoValidationException(entity.getClass(), invalidFields);
		}
	}


	private static Map<String, List<String>> validateSubItem(Field field, Object entity, Set<Object> visitedReferences) {
		Class<?> type = field.getType();

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			if (!type.isPrimitive() && !type.isArray() && !type.isEnum()) {
				if (List.class.isAssignableFrom(type)) {
					field.setAccessible(true);
					List list = (List) field.get(entity);
					if (list != null) {
						for (int i = 0; i< list.size(); i++) {
							Object listItem = list.get(i);
							try {
								validateEntity(listItem, visitedReferences, false);
							} catch (PojoValidationException e) {
								Map<String, List<String>> mapSubItem = e.getInvalidFields();
								for (String key : mapSubItem.keySet()) {
									map.put(field.getName() + "[" + i + "]." + key, mapSubItem.get(key));
								}
							} catch (IllegalArgumentException e) {
								// do nothing
							}
						}
					}
				} else {
					field.setAccessible(true);
					Object item = field.get(entity);
					try {
						validateEntity(item, visitedReferences, false);
					} catch (PojoValidationException e) {
						Map<String, List<String>> mapSubItem = e.getInvalidFields();
						for (String key : mapSubItem.keySet()) {
							map.put(field.getName() + "." + key, mapSubItem.get(key));
						}
					} catch (IllegalArgumentException e) {
						// do nothing
					}
				}
			}
		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
		return map;
	}

	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		Field[] entityFields = type.getDeclaredFields();
		for (Field field : entityFields) {
			fields.add(field);
		}

		return fields;
	}
}
