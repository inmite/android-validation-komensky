package eu.inmite.android.lib.validations.util;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

public class DelayedValidatingViewTrigger {

    public static final int DEFAULT_DELAY = 1000;
    private final Fragment fragment;
    private final View targetView;
    private final IValidationCallback callback;
    private final DelayedHandler handler;
    private final int delay;

    public DelayedValidatingViewTrigger(Fragment fragment, View targetView, IValidationCallback callback) {
        this(fragment, targetView, callback, DEFAULT_DELAY);
    }

    public DelayedValidatingViewTrigger(Fragment fragment, View targetView, IValidationCallback callback, int delay) {
        this.fragment = fragment;
        this.targetView = targetView;
        this.callback = callback;
        this.delay = delay;
        handler = new DelayedHandler();
    }

    public void run() {
        handler.removeMessages(handler.REQUEST);
        handler.sendEmptyMessageDelayed(DelayedHandler.REQUEST, delay);
    }

    private class DelayedHandler extends Handler {

        public static final int REQUEST = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST:
                    if (targetView != null) {
                        FormValidator.validate(fragment, targetView, callback);
                    } else {
                        FormValidator.validate(fragment, callback);
                    }
                    break;
            }
        }
    }
}
