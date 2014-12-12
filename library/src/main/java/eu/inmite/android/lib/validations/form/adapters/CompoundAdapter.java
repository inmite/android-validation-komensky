package eu.inmite.android.lib.validations.form.adapters;

import android.widget.CompoundButton;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.annotations.Checked;
import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

public class CompoundAdapter implements IFieldAdapter<CompoundButton, Boolean> {

    @Override
    public Boolean getFieldValue(Annotation annotation, Object target, CompoundButton fieldView) {
        return ((Checked) annotation).value() == fieldView.isChecked();
    }
}
