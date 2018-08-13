package com.et.physicalmorse;

import android.Manifest;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private Switch mSwitch;
    private static final int REQUEST_CODE_CAMERA = 9;
    private boolean getPermission = false;
    private Camera camera;
    private boolean isLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.et_data_input);
        button = findViewById(R.id.bt_trigger);
        mSwitch = findViewById(R.id.sw_loop);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isLoop = b;
            }
        });

        button.setOnClickListener(this);
        requestPermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        List<String> strings = PermissionUtils.checkMorePermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        if (strings.size() > 0) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
            getPermission = false;
        } else {
            getPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (!PermissionUtils.isPermissionRequestSuccess(grantResults)) {    // 权限申请成功
                    Toast.makeText(MainActivity.this, "this app need camera permission!", Toast.LENGTH_LONG).show();
                    getPermission = false;
                } else {
                    getPermission = true;
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (!getPermission) {
            Toast.makeText(MainActivity.this, getString(R.string.tip_permission_request), Toast.LENGTH_LONG).show();
            return;
        }
        String trim = editText.getText().toString().trim();
        if (trim == null || "".equalsIgnoreCase(trim)) {
            Toast.makeText(MainActivity.this, getString(R.string.tip_input_request), Toast.LENGTH_LONG).show();
            return;
        }
        encodeData(trim);

    }

    private void encodeData(String trim) {
        if (MorseUtil.isChinese(trim)) {
            Toast.makeText(this, getString(R.string.tip_not_support_cn), Toast.LENGTH_LONG).show();
            return;
        }

        String encode = MorseUtil.encode(trim);
        final ArrayList<FlashUtil.FlashBean> flashData = FlashUtil.getFlashData(encode);

        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    for (FlashUtil.FlashBean bean : flashData) {
                        if (bean.isFlag()) {
                            open();
                            goSleep(bean.getTime());
                        } else {
                            close();
                            goSleep(bean.getTime());
                        }
                    }
                    close();
                    goSleep(1300);
                } while (isLoop);
            }
        }).start();
    }

    /**
     * 打开闪光灯
     *
     * @return
     */
    private void open() {
        try {
            camera = Camera.open();
            camera.startPreview();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭闪光灯
     *
     * @return
     */
    private void close() {
        try {
            if (camera == null) {
                return;
            }
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goSleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
