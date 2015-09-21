package eu.inmite.android.lib.validations.form.adapters;

import android.view.View;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;

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

        ArrayList<View> viewSet = new ArrayList<>(Arrays.asList(views));
        viewSet.add(sourceView);

        return viewSet.toArray(new View[viewSet.size()]);
    }
}
