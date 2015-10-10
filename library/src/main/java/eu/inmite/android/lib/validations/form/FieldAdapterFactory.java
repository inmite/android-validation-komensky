package eu.inmite.android.lib.validations.form;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import eu.inmite.android.lib.validations.form.adapters.CompoundAdapter;
import eu.inmite.android.lib.validations.form.adapters.FieldEqualsAdapter;
import eu.inmite.android.lib.validations.form.adapters.JoinedAdapter;
import eu.inmite.android.lib.validations.form.adapters.SpinnerAdapter;
import eu.inmite.android.lib.validations.form.adapters.TextViewAdapter;
import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;
import eu.inmite.android.lib.validations.form.annotations.Joined;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

/**
 * Created adapters for views with validation.
 *
 * @author Tomas Vondracek
 */
public class FieldAdapterFactory {

	private static JoinedAdapter sJoinedAdapter;
	private static FieldEqualsAdapter sFieldsEqualsAdapter;
	private static TextViewAdapter sTextViewAdapter;
	private static SpinnerAdapter sSpinnerViewAdapter;
	private static CompoundAdapter sCompoundViewAdapter;

	private static Map<Class<? extends View>, IFieldAdapter<? extends View,?>> sExternalAdapters;

	static void registerAdapter(Class<? extends View> viewType, Class<? extends IFieldAdapter<? extends View,?>> adapterClazz) throws IllegalAccessException, InstantiationException {
		if (sExternalAdapters == null) {
			sExternalAdapters = new HashMap<>();
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
		} else if (annotation != null && FieldsEqual.class.equals(annotation.annotationType())) {
			if (sFieldsEqualsAdapter == null) {
				sFieldsEqualsAdapter = new FieldEqualsAdapter();
			}
			adapter = sFieldsEqualsAdapter;
		} else if (sExternalAdapters != null && sExternalAdapters.containsKey(view.getClass())) {
			adapter = sExternalAdapters.get(view.getClass());
        } else if (view instanceof CompoundButton) {
            if (sCompoundViewAdapter == null) {
                sCompoundViewAdapter = new CompoundAdapter();
            }
            adapter = sCompoundViewAdapter;
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
		sFieldsEqualsAdapter = null;
	}
}
