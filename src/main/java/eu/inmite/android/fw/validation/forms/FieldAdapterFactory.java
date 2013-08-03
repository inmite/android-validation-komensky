package eu.inmite.android.fw.validation.forms;

import android.view.View;
import android.widget.TextView;
import eu.inmite.android.fw.validation.forms.adapters.JoinedAdapter;
import eu.inmite.android.fw.validation.forms.adapters.TextViewAdapter;
import eu.inmite.android.fw.validation.forms.annotations.Joined;
import eu.inmite.android.fw.validation.forms.iface.IFieldAdapter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomas Vondracek
 */
public class FieldAdapterFactory {

	// TODO make it extensible for external adapters

	private static JoinedAdapter sJoinedAdapter;
	private static TextViewAdapter sTextViewAdapter;

	private static Map<Class<? extends View>, IFieldAdapter> sAdapters;

	static void registerAdapter(Class<? extends View> viewType, Class<? extends IFieldAdapter> adapterClazz) throws IllegalAccessException, InstantiationException {
		if (sAdapters == null) {
			sAdapters = new HashMap<Class<? extends View>, IFieldAdapter>();
		}
		sAdapters.put(viewType, adapterClazz.newInstance());
	}

	public static IFieldAdapter getAdapterForField(View view, Annotation annotation) {
		final IFieldAdapter adapter;
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
		} else if (sAdapters != null && sAdapters.containsKey(view.getClass())) {
			adapter = sAdapters.get(view.getClass());
		} else {
			adapter = null;
		}
		return adapter;
	}

	static void clear() {
		if (sAdapters != null) {
			sAdapters.clear();
		}
		sJoinedAdapter = null;
		sTextViewAdapter = null;
	}
}
