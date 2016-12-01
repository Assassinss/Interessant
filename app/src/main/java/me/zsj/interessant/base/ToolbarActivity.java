package me.zsj.interessant.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import me.zsj.interessant.R;
import me.zsj.interessant.SearchActivity;

/**
 * @author zsj
 */

public abstract class ToolbarActivity extends RxAppCompatActivity {

    public Toolbar toolbar;
    public ActionBar ab;

    public abstract int providerLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(providerLayoutId());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }

    public void toSearch(Activity context) {
        Intent intent = new Intent(context, SearchActivity.class);
        startActivity(intent);
    }
}
