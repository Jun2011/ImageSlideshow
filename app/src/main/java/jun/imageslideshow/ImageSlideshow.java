package jun.imageslideshow;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun on 2016/8/4.
 */
public class ImageSlideshow extends FrameLayout {

    private static final int NORMAL = 0;
    private static final int lARGE = 1;
    private static final int SMALL = 2;

    private Context context;
    private View contentView;
    private ViewPager vpImageTitle;
    private LinearLayout llDot;
    private int count;
    private List<View> viewList;
    private boolean isAutoPlay;
    private Handler handler;
    private int currentItem;
    private Animator animatorToLarge;
    private Animator animatorToSmall;
    private SparseBooleanArray isLarge;

    public ImageSlideshow(Context context) {
        this(context, null);
    }

    public ImageSlideshow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageSlideshow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // 初始化View
        initView();
        // 初始化Animator
        initAnimator();
    }

    private void initAnimator() {
        animatorToLarge = AnimatorInflater.loadAnimator(context, R.animator.scale_to_large);
        animatorToSmall = AnimatorInflater.loadAnimator(context, R.animator.scale_to_small);
    }

    /**
     * 初始化View
     */
    private void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.content_layout, this, true);
        vpImageTitle = (ViewPager) findViewById(R.id.vp_image_title);
        llDot = (LinearLayout) findViewById(R.id.ll_dot);
    }

    public void setImageTitleData(List<ImageTitleBean> imageTitleBeanList) {
        count = imageTitleBeanList.size();
        // 设置ViewPager
        setViewPager(imageTitleBeanList);
        // 设置指示器
        setIndicator();
        // 开始播放
        starPlay();
    }

    /**
     * 设置指示器
     */
    private void setIndicator() {
        isLarge = new SparseBooleanArray();
        for (int i = 0; i < count; i++) {
            View view = new View(context);
            view.setBackgroundResource(R.drawable.dot_unselected);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(16, 16);
            layoutParams.leftMargin = 16;
            layoutParams.rightMargin = 16;
            layoutParams.topMargin = 16;
            layoutParams.bottomMargin = 16;
            llDot.addView(view, layoutParams);
            isLarge.put(i, false);
        }
        llDot.getChildAt(0).setBackgroundResource(R.drawable.dot_selected);
        animatorToLarge.setTarget(llDot.getChildAt(0));
        animatorToLarge.start();
        isLarge.put(0, true);
    }

    /**
     * 开始自动播放图片
     */
    private void starPlay() {
        isAutoPlay = true;
        handler = new Handler();
        handler.postDelayed(task, 3000);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                // 位置循环
                currentItem = currentItem % (count + 1) + 1;
                // 正常每隔3秒播放一张图片
                vpImageTitle.setCurrentItem(currentItem);
                handler.postDelayed(task, 3000);
            } else {
                // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
                handler.postDelayed(task, 5000);
            }
        }
    };

    class ImageTitlePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    /**
     * 设置ViewPager
     *
     * @param imageTitleBeanList
     */
    private void setViewPager(List<ImageTitleBean> imageTitleBeanList) {
        // 设置View列表
        setViewList(imageTitleBeanList);
        vpImageTitle.setAdapter(new ImageTitlePagerAdapter());
        // 从第1张图片开始（位置刚好也是1，注意：0位置现在是最后一张图片）
        currentItem = 1;
        vpImageTitle.setCurrentItem(1);
        vpImageTitle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 遍历一遍子View，设置相应的背景。
                for (int i = 0; i < llDot.getChildCount(); i++) {
                    if (i == position - 1) {// 被选中
                        llDot.getChildAt(i).setBackgroundResource(R.drawable.dot_selected);
                        if (!isLarge.get(i)) {
                            animatorToLarge.setTarget(llDot.getChildAt(i));
                            animatorToLarge.start();
                            isLarge.put(i, true);
                        }
                    } else {// 未被选中
                        llDot.getChildAt(i).setBackgroundResource(R.drawable.dot_unselected);
                        if (isLarge.get(i)) {
                            animatorToSmall.setTarget(llDot.getChildAt(i));
                            animatorToSmall.start();
                            isLarge.put(i, false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    // 闲置中
                    case ViewPager.SCROLL_STATE_IDLE:
                        // “偷梁换柱”
                        if (vpImageTitle.getCurrentItem() == 0) {
                            vpImageTitle.setCurrentItem(count, false);
                        } else if (vpImageTitle.getCurrentItem() == count + 1) {
                            vpImageTitle.setCurrentItem(1, false);
                        }
                        currentItem = vpImageTitle.getCurrentItem();
                        isAutoPlay = true;
                        break;
                    // 拖动中
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    // 设置中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
    }

    /**
     * 根据出入的数据设置View列表
     *
     * @param imageTitleBeanList
     */
    private void setViewList(List<ImageTitleBean> imageTitleBeanList) {
        viewList = new ArrayList<>();
        for (int i = 0; i < count + 2; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_title_layout, null);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            if (i == 0) {// 将最前面一页设置成本来最后的那页
                Glide.with(context).
                        load(imageTitleBeanList.get(count - 1).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(count - 1).getTitle());
            } else if (i == count + 1) {// 将最后面一页设置成本来最前的那页
                Glide.with(context).
                        load(imageTitleBeanList.get(0).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(0).getTitle());
            } else {
                Glide.with(context).
                        load(imageTitleBeanList.get(i - 1).getImageUrl()).into(ivImage);
                tvTitle.setText(imageTitleBeanList.get(i - 1).getTitle());
            }
            // 将设置好的View添加到View列表中
            viewList.add(view);
        }
    }

    /**
     * 释放资源
     */
    public void releaseResource() {
        handler.removeCallbacksAndMessages(null);
        context = null;
    }
}
