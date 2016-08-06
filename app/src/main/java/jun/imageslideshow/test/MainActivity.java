package jun.imageslideshow.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import jun.imageslideshow.ImageSlideshow.ImageSlideshow;
import jun.imageslideshow.R;

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

        // 为ImageSlideshow设置数据
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
