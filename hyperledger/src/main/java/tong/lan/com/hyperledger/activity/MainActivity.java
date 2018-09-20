package tong.lan.com.hyperledger.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import tong.lan.com.hyperledger.R;
import tong.lan.com.hyperledger.adapter.TabFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements
        android.view.View.OnClickListener{

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    private Toolbar toolbar;
    private ViewPager mViewPager;// 用来放置界面切换
    private TabFragmentPagerAdapter mPagerAdapter;// 初始化View适配器
    private List<Fragment> mFragments;// 用来存放Tab01-04的Fragment
    // 底部四个用于点击的按钮，包含图片和文字，所以是layout
    private LinearLayout mTab1;
    private LinearLayout mTab2;
    private LinearLayout mTab3;
    private LinearLayout mTab4;
    private LinearLayout mTabHome;
    // 底部四个图片，点击时更新图片
    private ImageButton mImg1;
    private ImageButton mImg2;
    private ImageButton mImg3;
    private ImageButton mImg4;
    private ImageButton mImgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        initViewPage();
        initEvent();
        getPower();
        //建数据库
        SQLiteDatabase db = Connector.getDatabase();
        Stetho.initializeWithDefaults(this);
    }

    private void getPower(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    /* toolBar初始化 */
    private void initToolbar(){
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
//        toolbar.setSubtitle("Subtitle");//设置子标题
        toolbar.setTitle(R.string.cname);//设置主标题
        toolbar.inflateMenu(R.menu.toolbar_menu);//设置右上角的填充菜单
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    Toast.makeText(MainActivity.this , "搜索" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, WageDetailActivity.class);
                    startActivity(intent);
//                    finish();
                } else if (menuItemId == R.id.action_notification) {
                    Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItemId == R.id.action_item1) {
                    Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItemId == R.id.action_item2) {
                    Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    /* 底部点击事件*/
    private void initEvent() {
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mTab4.setOnClickListener(this);
        mTabHome.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *ViewPage左右滑动时
             * * * 1. 工资 2.员工 3.产品 4.管理
             */
            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                switch (currentItem) {
                    case 1:
                        resetImg();
                        mImg1.setImageResource(R.mipmap.icon_4_27);
                        break;
                    case 3:
                        resetImg();
                        mImg2.setImageResource(R.mipmap.icon_4_15);
                        break;
                    case 0:
                        resetImg();
                        mImgHome.setImageResource(R.mipmap.icon_4_9);
                        break;
                    case 2:
                        resetImg();
                        mImg3.setImageResource(R.mipmap.icon_4_25);
                        break;
                    case 4:
                        resetImg();
                        mImg4.setImageResource(R.mipmap.icon_4_17);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

     /* 初始化控件*/
    private void initView() {
        toolbar = findViewById(R.id.tool_bar_main);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
        // 初始化四个LinearLayout
        mTab1 = (LinearLayout) findViewById(R.id.id_tab_1);
        mTab2 = (LinearLayout) findViewById(R.id.id_tab_2);
        mTab3 = (LinearLayout) findViewById(R.id.id_tab_3);
        mTab4 = (LinearLayout) findViewById(R.id.id_tab_4);
        mTabHome = findViewById(R.id.id_tab_home);
        // 初始化四个按钮
        mImg1 = (ImageButton) findViewById(R.id.id_tab_weixin_img);
        mImg2 = (ImageButton) findViewById(R.id.id_tab_address_img);
        mImg3 = (ImageButton) findViewById(R.id.id_tab_frd_img);
        mImg4 = (ImageButton) findViewById(R.id.id_tab_settings_img);
        mImgHome = findViewById(R.id.id_tab_home_img);
    }

    /**
     * 初始化ViewPage
     * * 1. 工资 2.员工 3.产品 4.管理
     */
    private void initViewPage() {
        //把Fragment添加到List集合里面
        mFragments = new ArrayList<>();
        mFragments.add(new TabHomeFragment());
        mFragments.add(new TabOneFragment());
        mFragments.add(new TabThreeFragment());
        mFragments.add(new TabTwoFragment());
        mFragments.add(new TabFourFragment());

        mPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);  //初始化显示第一个页面
    }

    /**
     * 判断哪个要显示，及设置按钮图片
     * * 1. 工资 2.员工 3.产品 4.管理
     */
    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.id_tab_1:
                mViewPager.setCurrentItem(1);
                resetImg();
                mImg1.setImageResource(R.mipmap.icon_4_27);
                break;
            case R.id.id_tab_2:
                mViewPager.setCurrentItem(3);
                resetImg();
                mImg2.setImageResource(R.mipmap.icon_4_15);
                break;
            case R.id.id_tab_3:
                mViewPager.setCurrentItem(2);
                resetImg();
                mImg3.setImageResource(R.mipmap.icon_4_25);
                break;
            case R.id.id_tab_4:
                mViewPager.setCurrentItem(4);
                resetImg();
                mImg4.setImageResource(R.mipmap.icon_4_17);
                break;
            case R.id.id_tab_home:
                mViewPager.setCurrentItem(0);
                resetImg();
                mImgHome.setImageResource(R.mipmap.icon_4_9);
                break;
            default:
                break;
        }
    }

    /**
     * 把所有图片变暗
     * 1. 工资 2.员工 3.产品 4.管理
     */
    private void resetImg() {
        mImg1.setImageResource(R.mipmap.icon_4_28);
        mImg2.setImageResource(R.mipmap.icon_4_16);
        mImg3.setImageResource(R.mipmap.icon_4_26);
        mImg4.setImageResource(R.mipmap.icon_4_18);
        mImgHome.setImageResource(R.mipmap.icon_4_10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
