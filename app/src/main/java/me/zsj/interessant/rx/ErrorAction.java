package me.zsj.interessant.rx;

import android.content.Context;
import android.widget.Toast;

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
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        };
    }
}
