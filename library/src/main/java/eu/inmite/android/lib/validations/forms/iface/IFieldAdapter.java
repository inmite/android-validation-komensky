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

package eu.inmite.android.lib.validations.forms.iface;

import android.view.View;

import java.lang.annotation.Annotation;

/**
 * Adapter that gets the value from view.
 *
 * @author Tomas Vondracek
 */
public interface IFieldAdapter<V extends View, T> {

	/**
	 * get value of the field view
	 * @param annotation validation annotation that currently is processing and needs the value
	 * @param target target object that contains the field to get the value from
	 * @param fieldView view to get the value from
	 * @return value of field
	 */
	T getFieldValue(Annotation annotation, Object target, V fieldView);
}
