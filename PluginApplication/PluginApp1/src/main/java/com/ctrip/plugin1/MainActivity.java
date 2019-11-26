package com.ctrip.plugin1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private ImageView img;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (that == null) ? this : that;

        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "我是第一个插件图片-蚂蚁森林", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
