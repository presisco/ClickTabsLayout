package com.presisco.clicktabslayout;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ClickTabsFramework mFramework;
    List<ContentPage> mContentPages = new ArrayList<>();
    int defaultTabColor = 0;
    int page_count = 4;
    boolean even_tabs = true;
    ColorGenerator colorGen = new ColorGenerator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorGen.initColorsFromResArray(this, R.array.template_colors);

        prepareContentPages();
        defaultTabColor = Color.TRANSPARENT;
        prepareFramework();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra("tabs_count", page_count);
                intent.putExtra("is_even", even_tabs);
                startActivityForResult(intent, 100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 0) {
                page_count = data.getIntExtra("tabs_count", 4);
                even_tabs = data.getBooleanExtra("is_even", true);
                mContentPages.clear();
                prepareContentPages();
                prepareFramework();
            }
        }
    }

    private void prepareContentPages() {
        Random random = new Random();
        for (int i = 0; i < page_count; ++i) {
            String title = "Page";
            if (!even_tabs) {
                int length = random.nextInt(6);
                for (int j = 0; j < length; ++j) {
                    title += " ";
                }
            }
            title += i;
            int color = colorGen.getRandomColor();
            mContentPages.add(new ContentPage(title,
                    color,
                    ContentPageFragment.newInstance(title, color)));
        }
    }

    private List<Fragment> getContentFragments() {
        List<Fragment> list = new ArrayList<>();
        for (ContentPage item : mContentPages) {
            list.add(item.getFragment());
        }
        return list;
    }

    private void prepareFramework() {
        mFramework = new ClickTabsFramework();
        mFramework.setContentItems(getContentFragments());
        mFramework.setDistributeEvenly(even_tabs);
        mFramework.setTabLayout(R.layout.click_tabs_item);
        mFramework.setCustomTabDraw(new CustomTabDraw());

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.click_tabs_layout_frame, mFramework);
        trans.commit();
    }

    static class ContentPage {
        final Fragment fragment;
        final String title;
        final int pickedColor;

        ContentPage(String t, int c, Fragment f) {
            title = t;
            pickedColor = c;
            fragment = f;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public String getTitle() {
            return title;
        }

        public int getPickedColor() {
            return pickedColor;
        }
    }

    private class CustomTabDraw implements ClickTabsFramework.TabDraw {
        @Override
        public void initDraw(View v, int pos) {
            v.setBackgroundColor(defaultTabColor);
            ((TextView) v.findViewById(R.id.tabTitle)).setText(mContentPages.get(pos).getTitle());
        }

        @Override
        public void onClickedDraw(View last, int lastpos, View now, int pos) {
            if (last != null && lastpos != -1) {
                last.setBackgroundColor(defaultTabColor);
            }
            now.setBackgroundColor(mContentPages.get(pos).getPickedColor());
        }
    }


}
