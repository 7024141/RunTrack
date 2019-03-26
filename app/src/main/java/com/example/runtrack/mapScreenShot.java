package com.example.runtrack;

import android.graphics.Bitmap;
import android.os.Environment;

import com.amap.api.maps2d.AMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mapScreenShot {
    static String filePath = "";

    public static String aMapScreenShot(AMap aMap) {
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    filePath = Environment.getExternalStorageDirectory() +
                            "/DCIM/test_" + sdf.format(new Date()) + ".png";
                    // 保存在SD卡根目录下，图片为png格式。
                    FileOutputStream fos = new FileOutputStream(filePath);
                    boolean ifSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });
        return filePath;
    }
}
