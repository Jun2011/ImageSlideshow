# ImageSlideshow
一个自定义的图片轮播控件

### 运行效果
![](https://github.com/Jun2011/ImageSlideshow/raw/master/images/img_show.gif)

### 实现功能
- 1）自动播放；
- 2）无限滑动；
- 3）手指拖拽图片时暂停自动轮播，松开后继续自动轮播；
- 4）含动画效果的小圆点指示器。

具体的实现方法请看我的博文
[使用ViewPager实现图片轮播](http://www.jianshu.com/p/c083aa9ddd83)

### 使用方法
- 1、传入数据
 - addImageUrl(String imageUrl)：只传入图片
 - addImageTitle(String imageUrl, String title)：传入图片和标题

- 2、设置小圆点属性
 - setDotSize(int dotSize)：设置小圆点的大小
 - setDotSpace(int dotSpace)：设置小圆点的间距

- 3、设置播放的间隔时间
 - setDelay(int delay)

- 4、设置点击监听
 - setOnItemClickListener(OnItemClickListener listener)

- 5、提交
 - commit();

### 使用示范
```
<!--访问网络的权限-->
<uses-permission android:name="android.permission.INTERNET"/>
```

```
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <jun.imageslideshow.ImageSlideshow.ImageSlideshow
        android:id="@+id/is_gallery"
        android:layout_width="match_parent"
        android:layout_height="212dp"/>
</FrameLayout>
```

```
public class MainActivity extends AppCompatActivity {

    private ImageSlideshow imageSlideshow;
    private List<String> imageUrlList;
    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        imageSlideshow = (ImageSlideshow) findViewById(R.id.is_gallery);
        imageUrlList = new ArrayList<>();
        titleList = new ArrayList<>();

        // 初始化数据
        initData();

        // 设置ImageSlideshow
        imageSlideshow.setDotSpace(12);
        imageSlideshow.setDotSize(12);
        imageSlideshow.setDelay(3000);
        imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this,Activity_1.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,Activity_2.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,Activity_3.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this,Activity_4.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,Activity_5.class));
                        break;
                }
            }
        });
        imageSlideshow.commit();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String[] imageUrls = {"http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
                "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
                "http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg",
                "http://pic1.zhimg.com/b6f59c017b43937bb85a81f9269b1ae8.jpg",
                "http://pic2.zhimg.com/a62f9985cae17fe535a99901db18eba9.jpg"};
        String[] titles = {"读读日报 24 小时热门 TOP 5 · 余文乐和「香港贾玲」乌龙绯闻",
                "写给产品 / 市场 / 运营的数据抓取黑科技教程",
                "学做这些冰冰凉凉的下酒宵夜，简单又方便",
                "知乎好问题 · 有什么冷门、小众的爱好？",
                "欧洲都这么发达了，怎么人均收入还比美国低"};
        for (int i = 0; i < 5; i++) {
            imageSlideshow.addImageTitle(imageUrls[i], titles[i]);
        }
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        imageSlideshow.releaseResource();
        super.onDestroy();
    }
}
```

### 关于我
[李俊的博客](http://www.jianshu.com/users/32702f750012/latest_articles)