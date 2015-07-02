package eu.inmite.android.lib.validations.util;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.ref.WeakReference;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

public class DelayedValidatingViewTrigger {

    public static final int DEFAULT_DELAY = 1000;
    private final WeakReference<Fragment> fragmentRef;
    private final WeakReference<View> targetViewRef;
    private final WeakReference<IValidationCallback> callbackRef;
    private final DelayedHandler handler;
    private final int delay;

    public DelayedValidatingViewTrigger(Fragment fragment, View targetView, IValidationCallback callback) {
        this(fragment, targetView, callback, DEFAULT_DELAY);
    }

    public DelayedValidatingViewTrigger(Fragment fragment, View targetView, IValidationCallback callback, int delay) {
        this.fragmentRef = new WeakReference<>(fragment);
        this.targetViewRef = new WeakReference<>(targetView);
        this.callbackRef = new WeakReference<>(callback);
        this.delay = delay;
        handler = new DelayedHandler();
    }

    public void run() {
        handler.removeMessages(DelayedHandler.REQUEST);
        handler.sendEmptyMessageDelayed(DelayedHandler.REQUEST, delay);
    }

    private class DelayedHandler extends Handler {

        public static final int REQUEST = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST:
                    // check if target fragment is not destroyed
                    Fragment fragment = fragmentRef.get();
                    if (fragment == null) return;
                    // try validate
                    View targetView = targetViewRef.get();
                    IValidationCallback callback = callbackRef.get();
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
