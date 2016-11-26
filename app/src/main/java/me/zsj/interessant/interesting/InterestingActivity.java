package me.zsj.interessant.interesting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.zsj.interessant.MainActivity;
import me.zsj.interessant.R;
import me.zsj.interessant.base.ToolbarActivity;

/**
 * Created by zsj on 2016/10/10.
 */

public class InterestingActivity extends ToolbarActivity {

    public static final String RELATED_VIDEO = "related";
    public static final String RELATED_HEADER_VIDEO = "related_header";

    private static int categoryId;
    private static boolean related;
    private static boolean relatedHeader;


    @Override
    public int providerLayoutId() {
        return R.layout.interesting_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryId = getIntent().getExtras().getInt(MainActivity.CATEGORY_ID);
        related = getIntent().getBooleanExtra(RELATED_VIDEO, false);
        relatedHeader = getIntent().getBooleanExtra(RELATED_HEADER_VIDEO, false);
        String title = getIntent().getStringExtra(MainActivity.TITLE);

        if (title == null) ab.setTitle("Interesting");
        else {
            if (relatedHeader) ab.setTitle(title);
            else ab.setTitle(title.substring(1));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new TimeListFragment(), getResources().getString(R.string.time_category));
        adapter.addFragment(new ShareListFragment(), getResources().getString(R.string.share_category));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        private void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragments.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.CATEGORY_ID, categoryId);
            bundle.putBoolean(RELATED_VIDEO, related);
            bundle.putBoolean(RELATED_HEADER_VIDEO, relatedHeader);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.search_action) {
            toSearch(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
