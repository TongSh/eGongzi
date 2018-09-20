package tong.lan.com.egongzi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import tong.lan.com.egongzi.R;
import tong.lan.com.egongzi.bean.RecordBean;
import tong.lan.com.egongzi.domain.Employee;
import tong.lan.com.egongzi.domain.Make;
import tong.lan.com.egongzi.domain.Product;
import tong.lan.com.egongzi.utils.RecordAdapter;
import tong.lan.com.egongzi.utils.RecordDialog;
import tong.lan.com.egongzi.utils.update;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    private update nettool;
    private RecordDialog createUserDialog;
    private Context context;
    private RecordAdapter adapter;
    private List<RecordBean> lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        nettool = new update(this);
        context = this;
        //建数据库
        SQLiteDatabase db = Connector.getDatabase();
        //填充首页ListView
        listView = findViewById(R.id.record_list);

        fillInList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();//当前时间
                String date_now = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                //弹出新增对话框
                createUserDialog = new RecordDialog((Activity) context,0,0,date_now,0,R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onClick(View view){
                        switch (view.getId()) {
                            case R.id.btn_cancel_pop:
                                createUserDialog.dismiss();
                                Log.i("点击================","取消");
                                break;
                            case R.id.btn_save_pop:
                                int employeeId = Integer.parseInt(createUserDialog.getRecordEmployee().getSelectedItem().toString().split(" ")[0]);
                                int productId = Integer.parseInt(createUserDialog.getRecordProduct().getSelectedItem().toString().split(" ")[0]);
                                final String date = createUserDialog.getRecordDate().getText().toString().trim();
                                final int amount = Integer.parseInt(createUserDialog.getRecordAmount().getText().toString().trim());
                                //创建要保存的类对象
                                final Product product = DataSupport.find(Product.class,productId);
                                final Employee employee = DataSupport.find(Employee.class,employeeId);
                                Log.i("产品id------------",""+product.getId());

                                //确认对话框
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2.setTitle("添加生产记录");
                                builder2.setIcon(R.drawable.addrecord);
                                builder2.setMessage("记录信息确认:\n时间："+date+"\n生产者："+employee.getEmployeeName()+"\n产量："+amount+" "+product.getProductName()+product.getProductType());
                                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Make make = null;
                                        try {
                                            make = new Make(employee,
                                                    product,
                                                    new SimpleDateFormat("yyyy-MM-dd").parse(date),
                                                    amount);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        assert make != null;
                                        if (make.save()) {
                                            //存储成功
                                            lists.add(0,new RecordBean(make.getId(),product,employee,date,amount));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder2.show();

                                createUserDialog.dismiss();
                                break;

                            default:
                                createUserDialog.dismiss();
                                break;
                        }
                    }
                });
                createUserDialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init(){
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.employee_wage) {
            Intent intent = new Intent(this,WageActivity.class);
            startActivity(intent);
        } else if (id == R.id.product_wage) {
            Intent intent = new Intent(this,ProductTotalActivity.class);
            startActivity(intent);
        } else if (id == R.id.employee_manage) {
            Intent intent = new Intent(this,ListEmployeeActivity.class);
            startActivity(intent);
        } else if (id == R.id.product_manage) {
            Intent intent = new Intent(this, ListProductActivity.class);
            startActivity(intent);
        }else if(id == R.id.record_manage){
            Intent intent = new Intent(this, ListRecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            nettool.upload();
        } else if (id == R.id.nav_send) {
            nettool.download();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //填充首页的listView
    public void fillInList(){
        lists=new ArrayList<RecordBean>();
        List<Make> myList = DataSupport.findAll(Make.class,true);
        for (int i=myList.size()-1;i>=0;--i){
            lists.add(new RecordBean(myList.get(i).getId(),myList.get(i).getProduct(),myList.get(i).getEmployee(),myList.get(i).getMakeDate().toString(),myList.get(i).getMakeAmount()));
        }
        adapter=new RecordAdapter(this,lists);
        listView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
