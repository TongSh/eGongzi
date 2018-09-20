package tong.lan.com.hyperledger.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.util.DensityUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tong.lan.com.hyperledger.bean.WageBean;
import tong.lan.com.hyperledger.bean.WageDetailBean;

public class SaveImg {

    /**
     * 生成长图
     *
     * @param activity 页面上下文
     * @param view     列表控件
     * @param mList    数据源
     * @return bitmap
     */
    public static Bitmap getRecyclerItemsToBitmap(Activity activity, RecyclerView view, List<WageBean> mList, String title) {
        int allItemsHeight = 0;
        int itemWidth = view.getWidth();
        int px = 5;
        List<Bitmap> bitmaps = new ArrayList<>();
        // 标题
        {
            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);
            TextView titleView = new TextView(activity);
            titleView.setText(title);
            titleView.setTextColor(Color.parseColor("#000000"));
            titleView.setTextSize(38);
            titleView.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleView.setLayoutParams(param);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth, 2);
            line.setLayoutParams(params4);

            //第二层LinearLayout添加TextView
            linear.addView(titleView);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        // 第一行
        {
            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv1 = new TextView(activity);
            tv1.setText("姓名");
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setTextSize(30);
            tv1.setPadding(px, px, px, px);
            tv1.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setLayoutParams(params);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv2 = new TextView(activity);
            tv2.setText("工资");
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(30);
            tv2.setPadding(px, px, px, px);
            tv2.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv2.setLayoutParams(params2);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv3 = new TextView(activity);
            tv3.setText("出勤");
            tv3.setTextColor(Color.parseColor("#000000"));
            tv3.setTextSize(30);
            tv3.setPadding(px, px, px, px);
            tv3.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv3.setLayoutParams(params3);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth, 2);
            line.setLayoutParams(params4);

            //第二层LinearLayout添加两个TextView
            linear.addView(tv1);
            linear.addView(tv2);
            linear.addView(tv3);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        for (int i = 0; i < mList.size(); i++) {

            //得到索引对应条目的JavaBean
            WageBean item = mList.get(i);

            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv1 = new TextView(activity);
            tv1.setText(item.getEmployeeName());
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setTextSize(30);
            tv1.setPadding(px, px, px, px);
            tv1.setGravity(Gravity.LEFT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setLayoutParams(params);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv2 = new TextView(activity);
            tv2.setText((int) item.getWage()+"");
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(30);
            tv2.setPadding(px, px, px, px);
            tv2.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv2.setLayoutParams(params2);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv3 = new TextView(activity);
            tv3.setText((int) item.getWorkDay()+"");
            tv3.setTextColor(Color.parseColor("#000000"));
            tv3.setTextSize(30);
            tv3.setPadding(px, px, px, px);
            tv3.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(itemWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv3.setLayoutParams(params3);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth, 2);
            line.setLayoutParams(params4);

            //第二层LinearLayout添加两个TextView
            linear.addView(tv1);
            linear.addView(tv2);
            linear.addView(tv3);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        //创建大图
        Bitmap bigBitmap = Bitmap.createBitmap(itemWidth, allItemsHeight, Bitmap.Config.ARGB_4444);
        //创建画布
        Canvas bigCanvas = new Canvas(bigBitmap);
        //绘制背景
        bigCanvas.drawColor(Color.parseColor("#FFFFFF"));
        //创建画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < bitmaps.size(); i++) {
            //获取bitmaps集合当前索引得到的图片
            Bitmap bmp = bitmaps.get(i);
            //新建矩阵
            Matrix matrix = new Matrix();
            //对矩阵作位置偏移，移动到底部中间的位置
            matrix.postTranslate(0, bmp.getHeight() * i);
            //绘制到画布上并做矩阵变换
            bigCanvas.drawBitmap(bmp, matrix, paint);
            // 保存状态
            bigCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
            // 恢复状态
            bigCanvas.restore();
            //回收
            bmp.recycle();
        }

        return bigBitmap;
    }

    public static Bitmap getBitmapWageDetail(Activity activity, RecyclerView view, List<WageDetailBean> mList, String title, String total) {
        int allItemsHeight = 0;
        int itemWidth = view.getWidth();
        int px = 5;
        List<Bitmap> bitmaps = new ArrayList<>();
        // 标题
        {
            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);
            TextView titleView = new TextView(activity);
            titleView.setText(title);
            titleView.setTextColor(Color.parseColor("#000000"));
            titleView.setTextSize(25);
            titleView.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleView.setLayoutParams(param);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth, 4);
            line.setLayoutParams(params4);

            //第二层LinearLayout添加TextView
            linear.addView(titleView);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        // 第一行
        {
            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv1 = new TextView(activity);
            tv1.setText("日期");
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setTextSize(20);
            tv1.setPadding(px, px, px, px);
            tv1.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setLayoutParams(params);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv2 = new TextView(activity);
            tv2.setText("产品");
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(20);
            tv2.setPadding(px, px, px, px);
            tv2.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(itemWidth / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv2.setLayoutParams(params2);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv3 = new TextView(activity);
            tv3.setText("数量");
            tv3.setTextColor(Color.parseColor("#000000"));
            tv3.setTextSize(20);
            tv3.setPadding(px, px, px, px);
            tv3.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv3.setLayoutParams(params3);

            TextView tv4 = new TextView(activity);
            tv4.setText("工资");
            tv4.setTextColor(Color.parseColor("#000000"));
            tv4.setTextSize(20);
            tv4.setPadding(px, px, px, px);
            tv4.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv4.setLayoutParams(params4);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params5 = new ViewGroup.LayoutParams(itemWidth, 4);
            line.setLayoutParams(params5);

            //第二层LinearLayout添加两个TextView
            linear.addView(tv1);
            linear.addView(tv2);
            linear.addView(tv3);
            linear.addView(tv4);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        for (int i = 0; i < mList.size(); i++) {

            //得到索引对应条目的JavaBean
            WageDetailBean item = mList.get(i);

            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv1 = new TextView(activity);
            tv1.setText(item.getDate());
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setTextSize(20);
            tv1.setPadding(px, px, px, px);
            tv1.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setLayoutParams(params);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv2 = new TextView(activity);
            tv2.setText(item.getProduct());
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(20);
            tv2.setPadding(px, px, px, px);
            tv2.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(itemWidth / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv2.setLayoutParams(params2);

            //创建TextView2,设置属性，并设置宽度为三分之一
            TextView tv3 = new TextView(activity);
            tv3.setText(item.getAmount()+"");
            tv3.setTextColor(Color.parseColor("#000000"));
            tv3.setTextSize(20);
            tv3.setPadding(px, px, px, px);
            tv3.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv3.setLayoutParams(params3);

            TextView tv4 = new TextView(activity);
            tv4.setText((int)item.getWage()+"");
            tv4.setTextColor(Color.parseColor("#000000"));
            tv4.setTextSize(20);
            tv4.setPadding(px, px, px, px);
            tv4.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv4.setLayoutParams(params4);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params5 = new ViewGroup.LayoutParams(itemWidth, 2);
            line.setLayoutParams(params5);

            //第二层LinearLayout添加两个TextView
            linear.addView(tv1);
            linear.addView(tv2);
            linear.addView(tv3);
            linear.addView(tv4);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        // 总和
        {
            //创建条目最外层布局LinearLayout
            LinearLayout childView = new LinearLayout(activity);
            //设置最外层LinearLayout方向为垂直
            childView.setOrientation(LinearLayout.VERTICAL);
            //创建第二层LinearLayout，默认水平
            LinearLayout linear = new LinearLayout(activity);
            TextView tv1 = new TextView(activity);
            tv1.setText("工资总计");
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setTextSize(25);
            tv1.setGravity(Gravity.LEFT);
            ViewGroup.LayoutParams param1 = new ViewGroup.LayoutParams(itemWidth/2, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setLayoutParams(param1);

            TextView tv2 = new TextView(activity);
            tv2.setText(total+" 元");
            tv2.setTextColor(Color.parseColor("#000000"));
            tv2.setTextSize(25);
            tv2.setGravity(Gravity.RIGHT);
            ViewGroup.LayoutParams param2 = new ViewGroup.LayoutParams(itemWidth/2, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv2.setLayoutParams(param2);

            //创建条目分割线，设置颜色及高度
            View line = new View(activity);
            line.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(itemWidth, 4);
            line.setLayoutParams(params4);

            //第二层LinearLayout添加TextView
            linear.addView(tv1);
            linear.addView(tv2);

            //最外层LinearLayout添加第二层LinearLayout和分割线
            childView.addView(linear);
            childView.addView(line);

            //指定条目测量规则，进行布局
            childView.measure(0, 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            //设置缓存机制开启
            childView.setDrawingCacheEnabled(true);
            //创建缓存
            childView.buildDrawingCache();
            //将View缓存得到的bitmap添加到bitmaps集合中
            bitmaps.add(childView.getDrawingCache());
            //大图增加一个条目的高度
            allItemsHeight += childView.getMeasuredHeight();
        }
        allItemsHeight += 30;
        //创建大图
        Bitmap bigBitmap = Bitmap.createBitmap(itemWidth, allItemsHeight, Bitmap.Config.ARGB_4444);
        //创建画布
        Canvas bigCanvas = new Canvas(bigBitmap);
        //绘制背景
        bigCanvas.drawColor(Color.parseColor("#FFFFFF"));
        //创建画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < bitmaps.size(); i++) {
            //获取bitmaps集合当前索引得到的图片
            Bitmap bmp = bitmaps.get(i);
            //新建矩阵
            Matrix matrix = new Matrix();
            //对矩阵作位置偏移，移动到底部中间的位置
            matrix.postTranslate(0, bmp.getHeight() * i);
            //绘制到画布上并做矩阵变换
            bigCanvas.drawBitmap(bmp, matrix, paint);
            // 保存状态
            bigCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
            // 恢复状态
            bigCanvas.restore();
            //回收
            bmp.recycle();
        }

        return bigBitmap;
    }
    //保存在本地并一键分享
    public static String sharePic(Bitmap cachebmp,String child) {
        final File qrImage = new File(Environment.getExternalStorageDirectory()+ "/hyperledger", child+".jpg");
        if(qrImage.exists())
        {
            qrImage.delete();
        }
        try {
            qrImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(qrImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(cachebmp == null)
        {
            return "";
        }
        cachebmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qrImage.getPath();
    }

    /** * 将图片存到本地 */
    public static Uri saveBitmap(Bitmap bm, String picName) {
        try {
            String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/hyperledger/"+picName+".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(f);
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();    }
        return null;
    }

}
