package eu.inmite.android.fw.validation.adapters;

import android.view.View;
import android.widget.TextView;
import eu.inmite.android.fw.validation.annotations.Joined;
import eu.inmite.android.fw.validation.iface.IFieldAdapter;

import java.lang.annotation.Annotation;

/**
 * @author Tomas Vondracek
 */
public class FieldAdapterFactory {

	private static JoinedAdapter sJoinedAdapter;
	private static TextViewAdapter sTextViewAdapter;

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
		} else {
			adapter = null;
		}
		return adapter;
	}
}
