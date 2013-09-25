package eu.inmite.android.lib.validations.form;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import eu.inmite.android.lib.validations.form.adapters.JoinedAdapter;
import eu.inmite.android.lib.validations.form.adapters.SpinnerAdapter;
import eu.inmite.android.lib.validations.form.adapters.TextViewAdapter;
import eu.inmite.android.lib.validations.form.annotations.Joined;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created adapters for views with validation.
 *
 * @author Tomas Vondracek
 */
public class FieldAdapterFactory {

	private static JoinedAdapter sJoinedAdapter;
	private static TextViewAdapter sTextViewAdapter;
	private static SpinnerAdapter sSpinnerViewAdapter;

	private static Map<Class<? extends View>, IFieldAdapter<? extends View,?>> sExternalAdapters;

	static void registerAdapter(Class<? extends View> viewType, Class<? extends IFieldAdapter<? extends View,?>> adapterClazz) throws IllegalAccessException, InstantiationException {
		if (sExternalAdapters == null) {
			sExternalAdapters = new HashMap<Class<? extends View>, IFieldAdapter<? extends View,?>>();
		}
		sExternalAdapters.put(viewType, adapterClazz.newInstance());
	}


	public static IFieldAdapter getAdapterForField(View view) {
		return getAdapterForField(view, null);
	}

	public static IFieldAdapter<? extends View,?> getAdapterForField(View view, Annotation annotation) {
		final IFieldAdapter<? extends View,?> adapter;
		if (annotation != null && Joined.class.equals(annotation.annotationType())) {
			if (sJoinedAdapter == null) {
				sJoinedAdapter = new JoinedAdapter();
			}
			adapter = sJoinedAdapter;
		} else if (view instanceof TextView) {
			if (sTextViewAdapter == null) {
				sTextViewAdapter = new TextViewAdapter();
			}
			adapter = sTextViewAdapter;
		} else if (view instanceof Spinner) {
			if (sSpinnerViewAdapter == null) {
				sSpinnerViewAdapter = new SpinnerAdapter();
			}
			adapter = sSpinnerViewAdapter;
		} else if (sExternalAdapters != null && sExternalAdapters.containsKey(view.getClass())) {
			adapter = sExternalAdapters.get(view.getClass());
		} else {
			adapter = null;
		}
		return adapter;
	}

	static void clear() {
		if (sExternalAdapters != null) {
			sExternalAdapters.clear();
		}
		sJoinedAdapter = null;
		sTextViewAdapter = null;
		sSpinnerViewAdapter = null;
	}
}
