package com.sunny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sunny.service.DownloadFileService;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

public class MainActivity extends AppCompatActivity {

    private Button btnLoad;
    private Button btnExit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoad = (Button) findViewById(R.id.btn_load);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvResult = (TextView) findViewById(R.id.tv_result);

        //加载补丁
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启补丁下载
                Intent intent = new Intent(MainActivity.this, DownloadFileService.class);
                intent.putExtra("apk_url", "http://dl.yulela.net/app/patch_signed_7zip.apk?" + System.currentTimeMillis());
                startService(intent);
            }
        });

        //退出app
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        tvResult.setText("现在是北京时间8月10号.....tinker 补丁加载成功");
//        tvResult.setText("现在是北京时间8月10号");
    }
}
