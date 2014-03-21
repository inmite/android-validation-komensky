package eu.inmite.android.lib.validations.util;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

public class ValidatingTextWatcher extends DelayedValidatingViewTrigger implements TextWatcher {

    public ValidatingTextWatcher(Fragment fragment, View targetView, IValidationCallback callback) {
        super(fragment, targetView, callback);
    }

    public ValidatingTextWatcher(Fragment fragment, View targetView, IValidationCallback callback, int delay) {
        super(fragment, targetView, callback, delay);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        run();
    }

}
