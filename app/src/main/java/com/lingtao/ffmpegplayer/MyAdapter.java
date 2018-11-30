package com.lingtao.ffmpegplayer;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 王松 on 2016/9/13.
 */
public class MyAdapter extends PagerAdapter {
    private List<ImageView> list;

    public MyAdapter(List<ImageView> list) {
        this.list = list;
    }

    //返回ViewPager中一共有多少项
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //执行item的销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("google_lenve_fb", "destroyItem: " + position);
        //object表示即将移出页面的View
//        container.removeView(list.get(position % 5));
        container.removeView((View) object);
    }

    //实例化一个item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("google_lenve_fb", "instantiateItem: " + position);
        ImageView iv = list.get(position % list.size());
        //将ImageView添加到容器中
        container.addView(iv);
        return iv;
    }
}
