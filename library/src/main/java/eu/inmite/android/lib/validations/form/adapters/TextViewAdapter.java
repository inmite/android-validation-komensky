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

package eu.inmite.android.lib.validations.form.adapters;

import android.widget.TextView;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

import java.lang.annotation.Annotation;

/**
 * Adapter for all views inherited from {@link TextView}
 *
 * @author Tomas Vondracek
 */
public class TextViewAdapter implements IFieldAdapter<TextView, String> {

	@Override
	public String getFieldValue(Annotation annotation, Object target, TextView fieldView) {
		return fieldView.getText().toString();
	}
}
