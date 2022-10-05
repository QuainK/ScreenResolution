package com.niceqk.screenresolution;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateTextView();
    }

    /**
     * 更新内容
     */
    private void updateTextView() {
        // 获取页面控件
        TextView renderContent = findViewById(R.id.render_resolution_content);
        TextView physicalContent = findViewById(R.id.physical_resolution_content);
        TextView screenContent = findViewById(R.id.screen_size_content);


        // 获取屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();

        /* 渲染分辨率（像素） */
        int renderX = dm.widthPixels;

        // 注释的这个属性不太行
        // 高度获取的是app可渲染高度（不含系统状态栏）
        // 而不是整个手机屏幕的高度
        // int renderY = dm.heightPixels;

        // 获取屏幕完整高度
        // 这个不错
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(outSize);
        int renderY = outSize.y;

        /* 物理分辨率（英寸） */
        // 渲染比例（每英寸像素）
        float dpiX = dm.xdpi;
        float dpiY = dm.ydpi;
        // 物理尺寸 = 渲染尺寸 / 渲染比例
        float physicalX = renderX / dpiY;
        float physicalY = renderY / dpiX;

        /* 屏幕尺寸（对角线长度） */
        // [√(宽^2+高^2)] / DPI
        double screenSize = Math.sqrt(Math.pow(physicalX, 2) + Math.pow(physicalY, 2));


        // 拼接字符串
        StringBuilder sb = new StringBuilder();

        sb.append(renderX)
            .append("p × ")
            .append(renderY)
            .append("p");
        String renderStr = sb.toString();

        // 清空str
        sb.setLength(0);
        sb.append(String.format(Locale.CHINA, "%.1f", physicalX))
            .append(" 英寸 × ")
            .append(String.format(Locale.CHINA, "%.1f", physicalY))
            .append(" 英寸");
        String physicalStr = sb.toString();

        // 清空str
        sb.setLength(0);
        sb.append(String.format(Locale.CHINA, "%.1f", screenSize))
            .append(" 英寸");
        String screenStr = sb.toString();


        // 更新结果到屏幕上
        renderContent.setText(String.format(getString(R.string.render_resolution_content), renderStr));
        physicalContent.setText(String.format(getString(R.string.physical_resolution_content), physicalStr));
        screenContent.setText(String.format(getString(R.string.screen_size_content), screenStr));
    }
}
