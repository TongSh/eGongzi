package tong.lan.com.hyperledger.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import tong.lan.com.hyperledger.R;

/**
 *
 * 数据库备份还原类
 * Created by Lizhuang on 2016/12/12.
 *
 * 备份操作
 * new DbBackups(this).execute("backupDatabase");
 * 还原操作
 * new DbBackups(this).execute("restroeDatabase");
 *
 */

public class DbBackups extends AsyncTask<String, Void, Integer> {
    public static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private static final int BACKUP_SUCCESS = 1;
    public static final int RESTORE_SUCCESS = 2;
    private static final int BACKUP_ERROR = 3;
    public static final int RESTORE_NOFLEERROR = 4;
    private Context myContext;

    public DbBackups(Context context) {
        this.myContext = context;
    }

    @Override
    public Integer doInBackground(String... params) {

        // 需要备份的数据库路径
//        String s = myContext.getDatabasePath("WorkManager").getAbsolutePath();
        File dbFile = myContext.getDatabasePath("WorkManager.db").getAbsoluteFile();
//        File dbFile = new File(s, "WorkManager.db");
        // 创建数据库目录路径 */DoorsDb/*.db
        File exportDir = new File(Environment.getExternalStorageDirectory()+ "/hyperledger", "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, "Backup.db");
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                try {
                    backup.createNewFile();
                }catch (IOException e) {
                    Toast.makeText(myContext,"文件创建失败!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                fileCopy(dbFile, backup);
                Toast.makeText(myContext,"数据备份成功!",Toast.LENGTH_LONG).show();
                return BACKUP_SUCCESS;
            } catch (Exception e) {
                Toast.makeText(myContext,"数据备份异常!",Toast.LENGTH_LONG).show();
                return BACKUP_ERROR;
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                Toast.makeText(myContext,"数据恢复成功!",Toast.LENGTH_LONG).show();
                return RESTORE_SUCCESS;
            } catch (Exception e) {
                Toast.makeText(myContext,"数据恢复异常!",Toast.LENGTH_LONG).show();
                return RESTORE_NOFLEERROR;
            }
        } else {
            return null;
        }
    }

    public void initLoad(){
        //把assets目录下的db文件复制到dbpath下
        String EXP_DIR = Environment.getExternalStorageDirectory()+ "/hyperledger/";
        String DB_NAME = "BackupInit.db";
        String dbPath = EXP_DIR + DB_NAME;
        if (!new File(dbPath).exists()) {
            try {
                boolean flag = new File(EXP_DIR).mkdirs();
                boolean newFile = new File(dbPath).createNewFile();
                try {
                    FileOutputStream out = new FileOutputStream(dbPath);
                    InputStream in = myContext.getAssets().open("Backup.db");
                    byte[] buffer = new byte[1024];
                    int readBytes = 0;
                    while ((readBytes = in.read(buffer)) != -1)
                        out.write(buffer, 0, readBytes);
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 软件的数据库
        File dbFile = myContext.getDatabasePath("WorkManager.db").getAbsoluteFile();
        // 从assets导出的数据库
        File backup = new File(EXP_DIR, DB_NAME);

        try {
            fileCopy(backup, dbFile);
            Toast.makeText(myContext,"数据恢复成功!",Toast.LENGTH_LONG).show();
            return;
        } catch (Exception e) {
            Toast.makeText(myContext,"数据恢复异常!",Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            Toast.makeText(myContext,"文件复制异常!",Toast.LENGTH_LONG).show();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        switch (result) {
            case BACKUP_SUCCESS:
                Toast.makeText(myContext,"数据备份成功!",Toast.LENGTH_LONG).show();
                break;
            case BACKUP_ERROR:
                Toast.makeText(myContext,"数据备份失败!",Toast.LENGTH_LONG).show();
                break;
            case RESTORE_SUCCESS:
                Toast.makeText(myContext,"数据恢复成功!",Toast.LENGTH_LONG).show();
                break;
            case RESTORE_NOFLEERROR:
                Toast.makeText(myContext,"数据恢复失败!",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

}
