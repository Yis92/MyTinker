package com.sunny.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载
 * Created by Administrator on 2017/7/21.
 */

public class DownloadFileService extends IntentService {

    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K

    public DownloadFileService() {
        super("DownloadFileService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            String urlStr = intent.getStringExtra("apk_url");
            Log.i("http", "tinker补丁地址:" + urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.connect();
            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            in = urlConnection.getInputStream();

//            File dir = StorageUtils.getCacheDirectory(this);
//            File apkFile = new File(dir, "patch_signed_7zip.apk");
            File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
            out = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);
            }
            //下载完成
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
            Log.i("http", "补丁下载成功....");
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
