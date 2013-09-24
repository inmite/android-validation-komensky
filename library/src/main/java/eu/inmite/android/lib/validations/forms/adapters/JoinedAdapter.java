package eu.inmite.android.lib.validations.forms.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import eu.inmite.android.lib.validations.forms.FieldAdapterFactory;
import eu.inmite.android.lib.validations.forms.annotations.Joined;
import eu.inmite.android.lib.validations.exception.FormsValidationException;
import eu.inmite.android.lib.validations.forms.iface.IFieldAdapter;

import java.lang.annotation.Annotation;

/**
 * Adapter that can be used together with {@link Joined} annotation. It gets values from multiple views.
 * @author Tomas Vondracek
 */
public class JoinedAdapter implements IFieldAdapter<View, String[]> {

	@Override
	public String[] getFieldValue(Annotation annotation, Object target, View fieldView) {
		final int[] viewIds = ((Joined) annotation).value();
		final View[] views;

		if (target instanceof Activity) {
			views = new View[viewIds.length];
			Activity activity = (Activity) target;
			for (int i = 0; i < viewIds.length; i++) {
				int id = viewIds[i];
				views[i] = activity.findViewById(id);
			}
		} else if (target instanceof Fragment) {
			Fragment fragment = (Fragment) target;
			View view = fragment.getView();
			views = findViewsInView(viewIds, view);
		} else if (target instanceof View) {
			views = findViewsInView(viewIds, (View) target);
		} else {
			throw new FormsValidationException("unknown target " + target);
		}

		final String[] fieldValues = new String[views.length];
		for (int i = 0; i < views.length; i++) {
			View view = views[i];
			fieldValues[i] = valueFromView(view);
		}
		return fieldValues;
	}

	private String valueFromView(View view) {
		IFieldAdapter adapter = FieldAdapterFactory.getAdapterForField(view, null);
		if (adapter != null) {
			return String.valueOf(adapter.getFieldValue(null, null, view));
		}
		return null;
	}

	private View[] findViewsInView(int[] viewIds, View target) {
		View[] views = new View[viewIds.length];
		for (int i = 0; i < viewIds.length; i++) {
			int id = viewIds[i];
			views[i] = target.findViewById(id);
		}
		return views;
	}
}
