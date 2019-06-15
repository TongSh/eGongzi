package tong.lan.com.hyperledger.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import tong.lan.com.hyperledger.utils.DbBackups;

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
    private LinearLayout tWage;
    private LinearLayout tEmpl;
    private LinearLayout tProd;
    private LinearLayout tMana;
    private LinearLayout tHome;
    // 底部四个图片，点击时更新图片
    private ImageButton mWage;
    private ImageButton mEmpl;
    private ImageButton mProd;
    private ImageButton mMana;
    private ImageButton mHome;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();
        initToolbar();
        initViewPage();
        initEvent();
        getPower();
        //建数据库
        SQLiteDatabase db = Connector.getDatabase();
        Stetho.initializeWithDefaults(this);
        loadDb();
    }

    private void loadDb(){
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit();
            ThreadRecover threadRecover = new ThreadRecover();
            threadRecover.run();
//            Toast.makeText(this,"第一次",Toast.LENGTH_LONG).show();
        }
        else {
//            Toast.makeText(this,"不是第一次",Toast.LENGTH_LONG).show();
        }
    }

    private class ThreadRecover extends Thread{
        public void run(){
            //编写自己的线程代码
            DbBackups backups = new DbBackups(context);
            backups.initLoad();
        }
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
        toolbar.setTitle(R.string.cname);//设置主标题
        toolbar.inflateMenu(R.menu.toolbar_menu);//设置右上角的填充菜单
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    Toast.makeText(MainActivity.this , "搜索" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, WageDetailActivity.class);
//                    startActivity(intent);
                }
//                else if (menuItemId == R.id.add_record) {
//                    Toast.makeText(MainActivity.this , "新增" , Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
//                    startActivity(intent);
//                } else if (menuItemId == R.id.action_item1) {
//                    Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
//                    startActivity(intent);
//                } else if (menuItemId == R.id.action_item2) {
//                    Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
//                    startActivity(intent);
//                }
                return true;
            }
        });
    }

    /* 底部点击事件*/
    private void initEvent() {
        tWage.setOnClickListener(this);
        tEmpl.setOnClickListener(this);
        tProd.setOnClickListener(this);
        tMana.setOnClickListener(this);
        tHome.setOnClickListener(this);
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
                        mWage.setImageResource(R.mipmap.icon_4_27);
                        break;
                    case 3:
                        resetImg();
                        mEmpl.setImageResource(R.mipmap.icon_4_15);
                        break;
                    case 0:
                        resetImg();
                        mHome.setImageResource(R.mipmap.icon_4_9);
                        break;
                    case 2:
                        resetImg();
                        mProd.setImageResource(R.mipmap.icon_4_25);
                        break;
                    case 4:
                        resetImg();
                        mMana.setImageResource(R.mipmap.icon_4_17);
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
        tWage = (LinearLayout) findViewById(R.id.id_tab_wage);
        tEmpl = (LinearLayout) findViewById(R.id.id_tab_empl);
        tProd = (LinearLayout) findViewById(R.id.id_tab_prod);
        tMana = (LinearLayout) findViewById(R.id.id_tab_mana);
        tHome = findViewById(R.id.id_tab_home);
        // 初始化四个按钮
        mWage = (ImageButton) findViewById(R.id.id_img_wage);
        mEmpl = (ImageButton) findViewById(R.id.id_img_empl);
        mProd = (ImageButton) findViewById(R.id.id_img_prod);
        mMana = (ImageButton) findViewById(R.id.id_img_mana);
        mHome = findViewById(R.id.id_img_home);
    }

    /**
     * 初始化ViewPage
     * * 1. 工资 2.员工 3.产品 4.管理
     */
    private void initViewPage() {
        //把Fragment添加到List集合里面
        mFragments = new ArrayList<>();
        mFragments.add(new TabHomeFragment());
        mFragments.add(new TabWageFragment());
        mFragments.add(new TabProdFragment());
        mFragments.add(new TabEmplFragment());
        mFragments.add(new TabManaFragment());

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
            case R.id.id_tab_wage:
                mViewPager.setCurrentItem(1);
                resetImg();
                mWage.setImageResource(R.mipmap.icon_4_27);
                break;
            case R.id.id_tab_empl:
                mViewPager.setCurrentItem(3);
                resetImg();
                mEmpl.setImageResource(R.mipmap.icon_4_15);
                break;
            case R.id.id_tab_prod:
                mViewPager.setCurrentItem(2);
                resetImg();
                mProd.setImageResource(R.mipmap.icon_4_25);
                break;
            case R.id.id_tab_mana:
                mViewPager.setCurrentItem(4);
                resetImg();
                mMana.setImageResource(R.mipmap.icon_4_17);
                break;
            case R.id.id_tab_home:
                mViewPager.setCurrentItem(0);
                resetImg();
                mHome.setImageResource(R.mipmap.icon_4_9);
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
        mWage.setImageResource(R.mipmap.icon_4_28);
        mEmpl.setImageResource(R.mipmap.icon_4_16);
        mProd.setImageResource(R.mipmap.icon_4_26);
        mMana.setImageResource(R.mipmap.icon_4_18);
        mHome.setImageResource(R.mipmap.icon_4_10);
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
