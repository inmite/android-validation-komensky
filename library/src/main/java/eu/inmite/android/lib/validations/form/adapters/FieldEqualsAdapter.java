package eu.inmite.android.lib.validations.form.adapters;

import android.view.View;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;

/**
 *
 */
public class FieldEqualsAdapter extends JoinedAdapter {

    @Override
    protected View[] getRelevantViewSet(Annotation annotation, View sourceView) {
        int[] viewIds = ((FieldsEqual) annotation).fields();
        View[] views = findViewsInView(viewIds, sourceView);

        View[] newViewSet = new View[views.length + 1];
        newViewSet[newViewSet.length - 1] = sourceView;

        return newViewSet;
    }
}
