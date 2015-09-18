package eu.inmite.android.lib.validations.form.adapters;

import android.view.View;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;

/**
 *
 */
public class FieldEqualsAdapter extends JoinedAdapter {

    @Override
    protected View[] getRelevantViewSet(Annotation annotation, View view) {
        int[] viewIds = ((FieldsEqual) annotation).fields();
        View[] views = findViewsInView(viewIds, view);

        return views;
    }
}
