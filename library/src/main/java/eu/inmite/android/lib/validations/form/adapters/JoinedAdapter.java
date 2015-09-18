package eu.inmite.android.lib.validations.form.adapters;

import android.view.View;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.FieldAdapterFactory;
import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;
import eu.inmite.android.lib.validations.form.annotations.Joined;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

/**
 * Adapter that can be used together with {@link Joined} annotation. It gets values from multiple views.
 * @author Tomas Vondracek
 */
public class JoinedAdapter implements IFieldAdapter<View, String[]> {

	@Override
	public String[] getFieldValue(Annotation annotation, View fieldView) {
		int[] viewIds = new int[0];
		if (annotation instanceof Joined) {
			viewIds = ((Joined) annotation).value();
		} else if (annotation instanceof FieldsEqual) {
			viewIds = ((FieldsEqual) annotation).fields();
		}

		final View[] views = findViewsInView(viewIds, fieldView);

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
			return String.valueOf(adapter.getFieldValue(null, view));
		}
		return null;
	}

	private static View[] findViewsInView(int[] viewIds, View target) {
		final View container = target.getRootView();
		final View[] views = new View[viewIds.length];
		for (int i = 0; i < viewIds.length; i++) {
			int id = viewIds[i];
			views[i] = container.findViewById(id);
		}
		return views;
	}
}
