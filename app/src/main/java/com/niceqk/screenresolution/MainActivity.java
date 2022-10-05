package com.niceqk.screenresolution;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        /* 获取页面控件 */
        TextView renderContent = findViewById(R.id.render_resolution_content);
        TextView physicalContent = findViewById(R.id.physical_resolution_content);
        TextView screenContent = findViewById(R.id.screen_size_content);
        TextView ppiContent = findViewById(R.id.ppi_content);


        /* 获取屏幕信息 */
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
        // 物理宽高，勾股定理，对角线物理长度
        double screenSize = Math.sqrt(Math.pow(physicalX, 2) + Math.pow(physicalY, 2));

        /* 屏幕像素密度 */
        int ppi = dm.densityDpi;

        /* 转换成字符串 */
        StringBuilder sb = new StringBuilder();

        // 渲染
        sb.append(renderX);
        String renderXStr = sb.toString();
        sb.setLength(0);
        sb.append(renderY);
        String renderYStr = sb.toString();

        // 物理
        sb.setLength(0);
        sb.append(String.format(Locale.CHINA, "%.1f", physicalX));
        String physicalWidthStr = sb.toString();
        sb.setLength(0);
        sb.append(String.format(Locale.CHINA, "%.1f", physicalY));
        String physicalHeightStr = sb.toString();

        // 对角线
        sb.setLength(0);
        sb.append(String.format(Locale.CHINA, "%.1f", screenSize));
        String screenStr = sb.toString();

        // 密度
        sb.setLength(0);
        sb.append(ppi);
        String ppiStr = sb.toString();


        /* 更新结果到屏幕上 */
        renderContent.setText(String.format(getString(R.string.render_resolution_content), renderXStr, renderYStr));
        physicalContent.setText(String.format(getString(R.string.physical_resolution_content), physicalWidthStr, physicalHeightStr));
        screenContent.setText(String.format(getString(R.string.screen_size_content), screenStr));
        ppiContent.setText(String.format(getString(R.string.ppi_content), ppiStr));
    }
}
