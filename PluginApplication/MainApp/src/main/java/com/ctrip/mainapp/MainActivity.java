package com.ctrip.mainapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * @author Jocerly.
 *
 */

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE_STORAGE = 1;
    private RecyclerView recyclerView;
    private PluginAdapter adapter;
    private List<File> files = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PluginAdapter(this);
        adapter.setOnItemClickListener(new PluginAdapter.OnItemClickListener() {
            @Override
            public void ItemClick(File file) {
                load(file);
            }
        });
        recyclerView.setAdapter(adapter);

        PluginManager.getInstance().setContext(this);
        initPermission();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (!isGranted) {
                requestStoragePermission(PERMISSION_CODE_STORAGE);
            } else {
                initData();
            }
        } else {
            initData();
        }
    }

    private void initData() {
        files.clear();
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/plugin_test");
        for (File listFile : file.listFiles()) {
            if (listFile.isFile() && listFile.getName().endsWith(".apk")) {
                files.add(listFile);
            }
        }
        adapter.setFiles(files);
    }

    private void requestStoragePermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                onRequestPermissionsResult(
                        requestCode,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new int[]{PackageManager.PERMISSION_GRANTED});
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            }
        } else {
            onRequestPermissionsResult(
                    requestCode,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    //事件绑定load
    private void load(File file) {
        /**
         * 事先放置到SD卡根目录的plugin.apk
         * 现实场景中是有服务端下发
         */
        if (file.exists()) {
            PluginManager.getInstance().loadPath(file.getAbsoluteFile().getPath());
            String className = PluginManager.getInstance().getEntryName();
            Resources resources = PluginManager.getInstance().getResources();
            DexClassLoader dexClassLoader = PluginManager.getInstance().getDexClassLoader();
            if (!TextUtils.isEmpty(className) && resources != null && dexClassLoader != null) {
                Toast.makeText(this, "加载插件成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ProxyActivity.class);
                intent.putExtra("className", className);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "加载插件失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    Toast.makeText(this, "需要授权读取SD卡权限", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}