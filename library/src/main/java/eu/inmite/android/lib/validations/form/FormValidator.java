/*
 * Copyright (c) 2013, Inmite s.r.o. (www.inmite.eu).
 *
 * All rights reserved. This source code can be used only for purposes specified
 * by the given license contract signed by the rightful deputy of Inmite s.r.o.
 * This source code can be used only by the owner of the license.
 *
 * Any disputes arising in respect of this agreement (license) shall be brought
 * before the Municipal Court of Prague.
 */

package eu.inmite.android.lib.validations.form;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import eu.inmite.android.lib.validations.exception.FormsValidationException;
import eu.inmite.android.lib.validations.exception.NoFieldAdapterException;
import eu.inmite.android.lib.validations.form.annotations.Condition;
import eu.inmite.android.lib.validations.form.iface.ICondition;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;
import eu.inmite.android.lib.validations.form.iface.IValidator;
import eu.inmite.android.lib.validations.form.validators.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <p>
 *      Validate forms. Use either {@link #validate(android.support.v4.app.Fragment, eu.inmite.android.lib.validations.form.iface.IValidationCallback)} methods
 *      to validate all views at once or start live validation by calling {@link #startLiveValidation(android.support.v4.app.Fragment, eu.inmite.android.lib.validations.form.iface.IValidationCallback)}.
 * </p>
 * <p>
 *      Views inherited from {@link android.widget.TextView} are automatically recognized and can be validated, for other views you need to provide adapter and
 *      register it by calling {@link #registerViewAdapter(Class, Class)}.
 * </p>
 * <p>
 *     To validate multiple views at once by one validator, use {@link eu.inmite.android.lib.validations.form.annotations.Joined} annotation. <br/>
 *     You can also add condition to view and the validation will proceed only of the given condition is met, see {@link Condition}.
 * </p>
 * <p>
 *      For all available validations see package {@link eu.inmite.android.lib.validations.form.annotations}.
 *      To add custom validation you can use {@link eu.inmite.android.lib.validations.form.annotations.Custom} annotation and provide own {@link IValidator}.
 * </p>
 *
 * @author Tomas Vondracek
 */
public class FormValidator {

	private static Map<Object, ViewGlobalFocusChangeListener> sLiveValidations;

	/**
	 * Register custom validator. This is only usable if you want to use custom validation annotations.
	 * Otherwise you can use {@link eu.inmite.android.lib.validations.form.annotations.Custom} annotation.
	 *
	 * @param validator validator class that is annotated with {@link eu.inmite.android.lib.validations.form.annotations.ValidatorFor} annotation.
	 * @throws IllegalArgumentException if validator is null
	 */
	@SuppressWarnings("unchecked")
	public static void registerValidator(Class<? extends IValidator<?>> validator) {
		if (validator == null) {
			throw new IllegalArgumentException("validator cannot be null");
		}

		ValidatorFactory.registerValidatorClasses(validator);
	}

	/**
	 * Register adapter that can be used to get value from view.
	 * @param viewType type of view adapter is determined to get values from
	 * @param adapterClass class of adapter to register
	 *
	 * @throws IllegalArgumentException if adapterClass is null or viewType is null
	 * @throws FormsValidationException when there is a problem when accessing adapter class
	 */
	public static void registerViewAdapter(Class<? extends View> viewType, Class<? extends IFieldAdapter<? extends View,?>> adapterClass) {
		if (viewType == null || adapterClass == null) {
			throw new IllegalArgumentException("arguments must not be null");
		}

		try {
			FieldAdapterFactory.registerAdapter(viewType, adapterClass);
		} catch (IllegalAccessException e) {
			throw new FormsValidationException(e);
		} catch (InstantiationException e) {
			throw new FormsValidationException(e);
		}
	}

	/**
	 * remove all adapters that have been registered with {@link #registerValidator(Class)}
	 */
	public static void clearViewAdapters() {
		FieldAdapterFactory.clear();
	}

	public static void clear() {
		ValidatorFactory.clearCachedValidators();
	}


	/**
	 * Clear fields cache for all targets. In general this isn't necessary since cache is freed automatically.
	 */
	public static boolean clearCaches() {
		return FieldFinder.clearCache();
	}

	/**
	 * Start live validation - whenever focus changes from view with validations upon itself, validators will run. <br/>
	 * Don't forget to call {@link #stopLiveValidation(Object)} once you are done.
	 * @param fragment fragment with views to validate, there can be only one continuous validation per target object (fragment)
	 * @param callback callback invoked whenever there is some validation fail
	 */
	public static void startLiveValidation(final Fragment fragment, final IValidationCallback callback) {
		startLiveValidation(fragment, fragment.getView(), callback);
	}

	/**
	 * Start live validation - whenever focus changes from view with validations upon itself, validators will run.<br/>
 	 * Don't forget to call {@link #stopLiveValidation(Object)} once you are done.
	 *
	 * @param target target with views to validate, there can be only one continuous validation per target
	 * @param formContainer view that contains our form (views to validate)
	 * @param callback callback invoked whenever there is some validation fail, can be null

	 * @throws IllegalArgumentException if formContainer or target is null
	 */
	public static void startLiveValidation(final Object target, final View formContainer, final IValidationCallback callback) {
		if (formContainer == null) {
			throw new IllegalArgumentException("form container view cannot be null");
		}
		if (target == null) {
			throw new IllegalArgumentException("target cannot be null");
		}

		if (sLiveValidations == null) {
			sLiveValidations = new HashMap<Object, ViewGlobalFocusChangeListener>();
		} else if (sLiveValidations.containsKey(target)) {
			// validation is already running
			return;
		}

		final Map<View, FieldInfo> infoMap = FieldFinder.getFieldsForTarget(target);
		final ViewGlobalFocusChangeListener listener = new ViewGlobalFocusChangeListener(infoMap, formContainer, target, callback);
		final ViewTreeObserver observer = formContainer.getViewTreeObserver();
		observer.addOnGlobalFocusChangeListener(listener);

		sLiveValidations.put(target, listener);
	}

	public static boolean isLiveValidationRunning(final Object target) {
		return sLiveValidations != null && sLiveValidations.containsKey(target);
	}

	/**
	 * stop previously started live validation by {@link #startLiveValidation(Object, android.view.View, eu.inmite.android.lib.validations.form.iface.IValidationCallback)}
	 * @param target live validation is recognized by target object
	 * @return true if there was live validation to stop
	 */
	public static boolean stopLiveValidation(final Object target) {
		if (sLiveValidations == null || ! sLiveValidations.containsKey(target)) {
			return false;
		}
		final ViewGlobalFocusChangeListener removed = sLiveValidations.remove(target);
		final ViewTreeObserver treeObserver = removed.formContainer.getViewTreeObserver();
		if (treeObserver.isAlive()) {
			treeObserver.removeOnGlobalFocusChangeListener(removed);
			return true;
		}

		return false;
	}

	/**
	 * Perform validation over the views of the activity.
	 *
	 * @param activity activity with views to validate
	 * @param callback callback the will receive result of validation, results are ordered by order param in annotation.
	 * @return whether the validation succeeded
	 */
	public static boolean validate(Activity activity, IValidationCallback callback) {
		return validate(activity, activity, callback);
	}

	/**
	 * Perform validation over the views of given fragment
	 *
	 * @param fragment fragment with views to validate
	 * @param callback callback the will receive result of validation, results are ordered by order param in annotation.
	 * @return whether the validation succeeded
	 */
	public static boolean validate(Fragment fragment, IValidationCallback callback) {
		return validate(fragment.getActivity(), fragment, callback);
	}

	/**
	 * Perform validation over all fields on the target object. <br/>
	 * Please note that if you have used joined validations (see {@link eu.inmite.android.lib.validations.form.annotations.Joined} over several fields at the same time,
	 * target needs to be of type {@link Activity}, {@link Fragment} or {@link android.view.ViewGroup}. Exception will be thrown otherwise.
	 *
	 * @return whether the validation succeeded
	 *
	 * @throws FormsValidationException if there is problem in validation process
	 * @throws IllegalArgumentException if context or target is null
	 */
	public synchronized static boolean validate(Context context, Object target, IValidationCallback callback) throws FormsValidationException {
		if (context == null) {
			throw new IllegalArgumentException("context cannot ben null");
		}
		if (target == null) {
			throw new IllegalArgumentException("target cannot be null");
		}

		final List<ValidationFail> failedValidations = new ArrayList<ValidationFail>();
		boolean result = true;

		final Map<View, FieldInfo> infoMap = FieldFinder.getFieldsForTarget(target);
		for (Map.Entry<View, FieldInfo> entry : infoMap.entrySet()) {
			final FieldInfo fieldInfo = entry.getValue();
			final View view = entry.getKey();
			
			if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
				// don't run validation on views that are not visible
				continue;
			}

			ValidationFail fieldResult = performFieldValidations(context, target, fieldInfo, view);
			if (fieldResult != null) {
				failedValidations.add(fieldResult);
				result = false;
			} // else validation passed

		}

		if (callback != null) {
			Collections.sort(failedValidations, new Comparator<ValidationFail>() {
				@Override
				public int compare(ValidationFail lhs, ValidationFail rhs) {
					return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
				}
			});
			callback.validationComplete(result, Collections.unmodifiableList(failedValidations));
		}
		return result;
	}

	/**
	 * perform all validations on single field
	 */
	@SuppressWarnings("unchecked")
	private static ValidationFail performFieldValidations(Context context, Object target, FieldInfo fieldInfo, View view) {
		// first, we need to check if condition is not applied for all validations on field
		if (fieldInfo.condition != null && fieldInfo.condition.validationAnnotation().equals(Condition.class)) {
			// condition is applied to all validations on field
			boolean evaluation = evaluateCondition(target, fieldInfo.condition);
			if (! evaluation) {
				// go to next field
				return null;
			}
		}

		// field validations
		for (ValidationInfo valInfo : fieldInfo.validationInfoList) {
			final Annotation annotation = valInfo.annotation;
			if (fieldInfo.condition != null && fieldInfo.condition.validationAnnotation().equals(annotation.annotationType())) {
				boolean evaluation = evaluateCondition(target, fieldInfo.condition);

				if (! evaluation) {
					// continue to next annotation
					continue;
				}
			}
			final IFieldAdapter adapter = FieldAdapterFactory.getAdapterForField(view, annotation);
			if (adapter == null) {
				throw new NoFieldAdapterException(view, annotation);
			}

			final Object value = adapter.getFieldValue(annotation, target, view);
			final boolean isValid = valInfo.validator.validate(annotation, value);

			if (! isValid) {
				final String message = valInfo.validator.getMessage(context, annotation, value);
				final int order = valInfo.validator.getOrder(annotation);

				// no more validations on this field
				return new ValidationFail(view, message, order);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static boolean evaluateCondition(Object target, Condition conditionAnnotation) {
		final int viewId = conditionAnnotation.viewId();
		final View conditionView;

		if (target instanceof Activity) {
			conditionView = ((Activity) target).findViewById(viewId);
		} else if (target instanceof Fragment) {
			conditionView = ((Fragment) target).getView().findViewById(viewId);
		} else if (target instanceof View) {
			conditionView = ((View) target).findViewById(viewId);
		} else {
			throw new FormsValidationException("unknown target " + target);
		}

		final Class<? extends ICondition> clazz = conditionAnnotation.value();
		final IFieldAdapter adapter = FieldAdapterFactory.getAdapterForField(conditionView);
		final Object value = adapter.getFieldValue(null, target, conditionView);
		try {
			return clazz.newInstance().evaluate(value);
		} catch (InstantiationException e) {
			throw new FormsValidationException(e);
		} catch (IllegalAccessException e) {
			throw new FormsValidationException(e);
		}
	}

	static class ValidationInfo {
		private final Annotation annotation;
		private final IValidator validator;
		final int order;

		ValidationInfo(Annotation annotation, IValidator validator) {
			this.annotation = annotation;
			this.validator = validator;
			this.order = validator.getOrder(annotation);
		}
	}

	static class FieldInfo {
		private final Condition condition;
		private final List<ValidationInfo> validationInfoList;

		FieldInfo(Condition condition, List<ValidationInfo> validationInfoList) {
			this.condition = condition;
			this.validationInfoList = validationInfoList;
		}
	}

	/**
	 * Holds information about failed validation on concrete view
	 */
	public static final class ValidationFail {
		public final View view;
		public final String message;
		final int order;

		private ValidationFail(View view, String message, int order) {
			this.view = view;
			this.message = message;
			this.order = order;
		}
	}

	static class ViewGlobalFocusChangeListener implements ViewTreeObserver.OnGlobalFocusChangeListener {

		private final Map<View, FieldInfo> infoMap;
		private final View formContainer;
		private final Object target;
		private final IValidationCallback callback;

		private View currentlyFocusedView;

		public ViewGlobalFocusChangeListener(Map<View, FieldInfo> infoMap, View formContainer, Object target, IValidationCallback callback) {
			this.infoMap = infoMap;
			this.formContainer = formContainer;
			this.target = target;
			this.callback = callback;
			this.currentlyFocusedView = formContainer.findFocus();
		}

		@Override
		public void onGlobalFocusChanged(View oldFocus, View newFocus) {
			// dunno why, but oldFocus is absolutely wrong
			if (this.currentlyFocusedView != null && this.currentlyFocusedView != newFocus) {
				FieldInfo info = this.infoMap.get(this.currentlyFocusedView);
				if (info != null) {
					final ValidationFail validationFail = performFieldValidations(this.formContainer.getContext(), target, info, this.currentlyFocusedView);

					if (validationFail != null && callback != null) {
						// we have a failed validation
						this.callback.validationComplete(false, Collections.singletonList(validationFail));
					}
				}
			}

			this.currentlyFocusedView = newFocus;
		}
	}
}
