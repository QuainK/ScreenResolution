package com.niceqk.screenresolution;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView renderTitle;
    TextView renderContent;
    TextView physicalTitle;
    TextView physicalContent;
    TextView screenTitle;
    TextView screenContent;
    TextView ppiTitle;
    TextView ppiContent;
    Button buttonCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 获取页面控件 */
        renderTitle = findViewById(R.id.render_resolution_title);
        renderContent = findViewById(R.id.render_resolution_content);
        physicalTitle = findViewById(R.id.physical_resolution_title);
        physicalContent = findViewById(R.id.physical_resolution_content);
        screenTitle = findViewById(R.id.screen_size_title);
        screenContent = findViewById(R.id.screen_size_content);
        ppiTitle = findViewById(R.id.ppi_title);
        ppiContent = findViewById(R.id.ppi_content);
        buttonCopy = findViewById(R.id.button_copy);

        // 更新内容
        updateTextView();
        // 添加复制按钮的点击事件监听
        buttonCopy.setOnClickListener(v -> {
            handleClickButtonCopy();
        });
    }

    /**
     * 更新内容
     */
    private void updateTextView() {
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

    /**
     * 处理复制按钮点击事件
     */
    private void handleClickButtonCopy() {
        // 新建剪贴板管理器对象
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 新建需要粘贴的内容
        StringBuilder sb = new StringBuilder();
        sb.append(renderTitle.getText())
            .append("\r\n")
            .append(renderContent.getText())
            .append("\r\n")
            .append(physicalTitle.getText())
            .append("\r\n")
            .append(physicalContent.getText())
            .append("\r\n")
            .append(screenTitle.getText())
            .append("\r\n")
            .append(screenContent.getText())
            .append("\r\n")
            .append(ppiTitle.getText())
            .append("\r\n")
            .append(ppiContent.getText());
        ClipData cd = ClipData.newPlainText("screenInfo", sb);
        try {
            // 写入剪贴板
            cm.setPrimaryClip(cd);
            // toast提示写入成功
            Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // toast提示写入失败
            Toast.makeText(MainActivity.this, "复制失败", Toast.LENGTH_SHORT).show();
            Log.e("copy to clipboard", "failed");
        }
    }
}
