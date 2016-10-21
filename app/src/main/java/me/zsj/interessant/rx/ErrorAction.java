package me.zsj.interessant.rx;

import android.content.Context;
import android.widget.Toast;

import rx.functions.Action1;

/**
 * Created by zsj on 2016/10/11.
 */

public class ErrorAction {

    private ErrorAction() {
        throw new AssertionError("No instances.");
    }

    public static Action1<Throwable> errorAction(final Context context) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
