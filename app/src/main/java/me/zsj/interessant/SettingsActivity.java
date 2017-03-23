package me.zsj.interessant;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.zsj.interessant.base.ToolbarActivity;

/**
 * @author zsj
 */

public class SettingsActivity extends ToolbarActivity {

    @Override
    public int providerLayoutId() {
        return R.layout.settings_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.settings));

        toolbar.setNavigationOnClickListener(v -> finish());

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

    }

}
