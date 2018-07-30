package com.et.physicalmorse;

import android.Manifest;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button button;
    private static final int REQUEST_CODE_CAMERA = 9;
    private boolean getPermission = false;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        editText = findViewById(R.id.et_data_input);
        button = findViewById(R.id.bt_trigger);

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
            Toast.makeText(MainActivity.this, "this app need camera permission!", Toast.LENGTH_LONG).show();
            return;
        }

        encodeData(editText.getText().toString().trim());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                open();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                close();
//
//            }
//        }).start();
    }

    private void encodeData(String trim) {
        String encode = MorseUtil.encode(trim);

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
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
