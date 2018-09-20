package tong.lan.com.egongzi.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import tong.lan.com.egongzi.R;
import tong.lan.com.egongzi.domain.Employee;
import tong.lan.com.egongzi.domain.Make;
import tong.lan.com.egongzi.utils.DateUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WageDetailActivity extends AppCompatActivity {
    private int employee;
    private int month;
    private int year;

    private ListView wage_list;//总计
    private TextView total_wage;
    private TextView name;
    private TextView month_now;

    private DecimalFormat df = new DecimalFormat("#.0");
    private DecimalFormat mondf = new DecimalFormat("00");

    private List<Make> makeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_wage_detail);

        // 设置toolbar，否则不会调用onCreateOptionsMenu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wage_detail);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        employee= intent.getIntExtra("employee",0);
        month= intent.getIntExtra("month", 0);
        year = intent.getIntExtra("year",0);
        wage_list = findViewById(R.id.detail_list);
        total_wage = findViewById(R.id.wage_total);
        name = findViewById(R.id.wage_employee);
        month_now = findViewById(R.id.wage_month);
        findWageDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_item1:
                Toast.makeText(this,"item1",Toast.LENGTH_LONG).show();
                ArrayList<String> s = new ArrayList<>();
                for(Make make : makeList) {
                    s.add(make.getMakeDate().toString());
                    s.add("产品" + make.getProduct().getProductType()+make.getProduct().getProductName());
                    s.add("数量" + make.getMakeAmount());
                    s.add("工资" + df.format(make.getMakeAmount()*make.getProduct().getProductWage()));
                }
                if (!checkPermission()) {
                    writeImage(s);
                } else {
                    if (checkPermission()) {
                        requestPermissionAndContinue();
                    } else {
                        writeImage(s);
                    }
                }

            default:break;
        }
        return true;
    }

    //按月查询工资
    @SuppressLint("SimpleDateFormat")
    private void findWageDetail()
    {
        long startDay = DateUtil.date2stamp(DateUtil.getString(year,month,1));
        long endDay = DateUtil.date2stamp(DateUtil.getString(year,month+1,1));
        makeList = DataSupport.
                where("makedate >= ? " +
                        "And makedate < ? " +
                        "And employee_id = ?",
                        startDay+"",
                        endDay+"",
                        employee+"").find(Make.class,true);

        //算工资
        double sum = 0;
        List<Map<String,Object>> wageList = new ArrayList<Map<String,Object>>();
        for (Make make : makeList){
            double eWage = make.getMakeAmount()*make.getProduct().getProductWage();
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("date",make.getMakeDate().toString());
            item.put("product",make.getProduct().getProductType() + make.getProduct().getProductName());
            item.put("amount",make.getMakeAmount());
            item.put("wage",df.format(eWage));
            wageList.add(item);
            sum += eWage;
        }
        SimpleAdapter sa=new SimpleAdapter(this,
                wageList,
                R.layout.wage_detail,
                new String[]{"date","product","amount","wage"},
                new int[]{R.id.wage_name,R.id.detail_product,R.id.detail_amount,R.id.wage_item});
        wage_list.setAdapter(sa);
        total_wage.setText(df.format(sum));
        Employee e = DataSupport.find(Employee.class,employee);
        if(e != null)
            name.setText(e.getEmployeeName());
        month_now.setText(month+"");
    }

    /**
     * @param data 保存数据
     * */
    public void writeImage(ArrayList<String> data){
        try {
            int height = data.size()*20;     //图片高
            Bitmap bitmap = Bitmap.createBitmap(270, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);   //背景颜色

            Paint p = new Paint();
            p.setColor(Color.BLACK);   //画笔颜色
            p.setTextSize(15);         //画笔粗细
            for(int i=0;i<data.size();i++){
                canvas.drawText(data.get(i), 20, (i+1)*20, p);
            }

            //将Bitmap保存为png图片
            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee.png");
            FileOutputStream out = new FileOutputStream(appDir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            Log.e("done", "done");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("permission_necessary");
                alertBuilder.setMessage("storage_permission_is_encessary_to_wrote_event");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(WageDetailActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(WageDetailActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            openActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    openActivity();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openActivity() {
        //add your further process after giving permission or to download images from remote server.
    }
}
