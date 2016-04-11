package com.presisco.clicktabslayout;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        mFramework = new ClickTabsFramework();
        mFramework.setContentItems(getContentFragments());
        mFramework.setDistributeEvenly(even_tabs);
        mFramework.setTabLayout(R.layout.click_tabs_item);
        mFramework.setCustomTabDraw(new CustomTabDraw());

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.click_tabs_layout_frame, mFramework);
        trans.commit();
    }

    private void prepareContentPages() {
        for (int i = 0; i < page_count; ++i) {
            String title = "Page" + i;
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
