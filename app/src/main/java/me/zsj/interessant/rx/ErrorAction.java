package me.zsj.interessant.rx;

import android.content.Context;

import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Consumer;

/**
 * @author zsj
 */

public class ErrorAction {

    private ErrorAction() {
        throw new AssertionError("No instances.");
    }

    public static Consumer<Throwable> error(Context context) {
        return throwable -> {
            throwable.printStackTrace();
            Toasty.error(context, throwable.getMessage()).show();
        };
    }
}
