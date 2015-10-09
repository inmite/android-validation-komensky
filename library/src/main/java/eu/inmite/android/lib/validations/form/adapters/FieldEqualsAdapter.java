package eu.inmite.android.lib.validations.form.adapters;

import android.view.View;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.FieldsEqual;

/**
 * Adapter to provide views for the fields equal annotation. Ensures that source view gets passed along
 * with other views specified in the annotation.
 */
public class FieldEqualsAdapter extends JoinedAdapter {

    @Override
    protected View[] getRelevantViewSet(Annotation annotation, View sourceView) {
        int[] viewIds = ((FieldsEqual) annotation).fields();
        View[] views = findViewsInView(viewIds, sourceView);

        View[] outSet = new View[views.length + 1];
        outSet[0] = sourceView;
        System.arraycopy(views, 0, outSet, 1, views.length);

        return outSet;
    }
}
