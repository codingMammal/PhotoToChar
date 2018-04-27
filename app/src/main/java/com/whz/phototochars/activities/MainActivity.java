package com.whz.phototochars.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.whz.phototochars.R;
import com.whz.phototochars.gifUtil.GifDecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    GifDecoder gifDecoder = null;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.bt_process)
    Button btProcess;
    @BindView(R.id.rb_r)
    RadioButton rbR;
    @BindView(R.id.rb_g)
    RadioButton rbG;
    @BindView(R.id.rb_b)
    RadioButton rbB;
    @BindView(R.id.rb_a)
    RadioButton rbA;
    @BindView(R.id.rg_channel)
    RadioGroup rgChannel;
    @BindView(R.id.rb_0)
    RadioButton rb0;
    @BindView(R.id.rb_64)
    RadioButton rb64;
    @BindView(R.id.rb_128)
    RadioButton rb128;
    @BindView(R.id.rb_192)
    RadioButton rb192;
    @BindView(R.id.rb_160)
    RadioButton rb160;
    @BindView(R.id.rg_grayStart)
    RadioGroup rgGrayStart;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rg_scale)
    RadioGroup rgScale;
    Bitmap tmpBitmap = null;
    Handler handler = null;
    ProgressDialog pd = null;
    boolean needLoad = true;
    int rgbCode = 0;
    int startGray = 128;
    int scale = 2;
    int pxSize = 10;
    boolean isDetailed=true;
    boolean isGif = true;
    Context context;
    String path = null;
    @BindView(R.id.rb_px_6)
    RadioButton rbPx6;
    @BindView(R.id.rb_px_10)
    RadioButton rbPx10;
    @BindView(R.id.rb_px_14)
    RadioButton rbPx14;
    @BindView(R.id.rb_px_18)
    RadioButton rbPx18;
    @BindView(R.id.rg_unitpx)
    RadioGroup rgUnitpx;
    @BindView(R.id.rb_stdset)
    RadioButton rbStdset;
    @BindView(R.id.rb_Chineseset)
    RadioButton rbChineseset;
    @BindView(R.id.rb_Engset)
    RadioButton rbEngset;
    @BindView(R.id.rg_charset)
    RadioGroup rgCharset;
    @BindView(R.id.tb_gif)
    ToggleButton tbGif;
    @BindView(R.id.tb_detail)
    ToggleButton tbDetail;
    char zero=' ';
    char full='█';
    String tmpString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ⊙●○①⊕◎Θ⊙¤㊣★☆♀◆◇◣◢◥▲▼△▽⊿◤ ◥ ▂ ▃ ▄ ▅ ▆ ▇ █ █ ■ ▓ 回 □ 〓≡ ╝╚╔ ╗╬ ═ ╓ ╩ ┠ ┨┯ ┷┏ ┓┗ ┛┳⊥﹃﹄┌ ┐└ ┘∟「」↑↓→←↘↙♀♂┇┅ ﹉﹊﹍﹎╭ ╮╰ ╯ *^_^* ^*^ ^-^ ^_^ ^︵^ ∵∴‖︱ ︳︴﹏﹋﹌︵︶︹︺ 【】〖〗＠﹕﹗/ \" _ < > `,·。≈{}~ ～() _ -『』√ $ @ * & # ※ 卐 々∞Ψ ∪∩∈∏ の ℡ ぁ §∮〝〞ミ灬ξ№∑⌒ξζω＊ㄨ ≮≯ ＋－×÷﹢﹣±／＝∫∮∝ ∞ ∧∨ ∑ ∏ ∥∠ ≌ ∽ ≦ ≧ ≒﹤﹥じ☆veve↑↓⊙●★☆■♀『』◆◣◥▲Ψ ※◤ ◥ →№←㊣∑⌒〖〗＠ξζω□∮〓※∴ぷ▂▃▅▆█ ∏卐【】△√ ∩¤々♀♂∞①ㄨ≡↘↙▂ ▂ ▃ ▄ ▅ ▆ ▇ █┗┛╰☆╮、。·ˉˇ¨〃々—～‖…‘’“”〔〕〈 〉《》「」『』〖〗【】±＋－×÷∧∨∑∏∪∩∈√⊥∥∠⌒⊙∫∮≡≌≈∽∝≠≮≯≤≥∞∶∵∴∷♂♀°′″℃＄¤￠￡‰§№☆★〇○●◎◇◆ 回□■△▽⊿▲▼◣◤◢◥▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▓※→←↑↓↖↗↘↙〓ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ①②③④⑤⑥⑦⑧⑨⑩⒈⒉⒊⒋ ⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ！＂＃￥％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨ ＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθ ικλμνξοπρστυφχψω︵︶︹︺︿﹀︽︾﹁﹂﹃﹄︻︼︷︸АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюāáǎàēéěèīíǐìōóǒòūúǔ ùǖǘǚǜüêɑńňɡㄅㄆㄇㄈㄉㄊㄋㄌㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩ︱︳︴﹏﹋﹌─━│┃┄┅┆┇┈┉┊┋┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇ ╈╉╊╋⊕㊣㈱曱甴囍∟┅﹊﹍╭ ╮╰ ╯_ ^︵^﹕﹗ < > `,·。{}~～() -√ $ @ * & # 卐℡ ぁ〝〞ミ灬№＊ㄨ≮≯﹢﹣／∝≌∽≦≧≒﹤﹥じぷ┗┛￥￡§я-―‥…‰′″℅℉№℡∕∝∣═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╱╲╳▔▕〆〒〡〢〣〤〥〦〧〨〩㎎ ㎏ ㎜ ㎝ ㎞ ㎡ ㏄ ㏎我";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/photoToChars");
        if (!file.exists())
            file.mkdir();
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        handler = new Handler();
        //黄油刀失效手动bind
        ivPhoto = findViewById(R.id.iv_photo);
        btProcess = findViewById(R.id.bt_process);
        rgChannel = findViewById(R.id.rg_channel);
        rgGrayStart = findViewById(R.id.rg_grayStart);
        rgScale = findViewById(R.id.rg_scale);
        rgUnitpx = findViewById(R.id.rg_unitpx);
        rbR = findViewById(R.id.rb_r);
        rbG = findViewById(R.id.rb_g);
        rbB = findViewById(R.id.rb_b);
        rbA = findViewById(R.id.rb_a);
        rb0 = findViewById(R.id.rb_0);
        rb64 = findViewById(R.id.rb_64);
        rb128 = findViewById(R.id.rb_128);
        rb160 = findViewById(R.id.rb_160);
        rb192 = findViewById(R.id.rb_192);
        rb1 = findViewById(R.id.rb_1);
        rb2 = findViewById(R.id.rb_2);
        rb4 = findViewById(R.id.rb_4);
        rbPx6 = findViewById(R.id.rb_px_6);
        rbPx10 = findViewById(R.id.rb_px_10);
        rbPx14 = findViewById(R.id.rb_px_14);
        rbPx18 = findViewById(R.id.rb_px_18);
        rgCharset=findViewById(R.id.rg_charset);
        rbStdset=findViewById(R.id.rb_stdset);
        rbChineseset=findViewById(R.id.rb_Chineseset);
        rbEngset=findViewById(R.id.rb_Engset);
        tbDetail=findViewById(R.id.tb_detail);
        tbDetail.setOnCheckedChangeListener(this);
        tbGif = findViewById(R.id.tb_gif);
        tbGif.setOnCheckedChangeListener(this);

        rgUnitpx.setOnCheckedChangeListener(this);
        rgScale.setOnCheckedChangeListener(this);
        rgGrayStart.setOnCheckedChangeListener(this);
        rgChannel.setOnCheckedChangeListener(this);
        rgCharset.setOnCheckedChangeListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
    }

    void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        if (isGif)
            startActivityForResult(intent, 2);
        else startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case 1://选择的图片
                try {
                    if (data != null) {
                        needLoad = false;
                        /**
                         * 该uri是上一个Activity返回的
                         */
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        tmpBitmap = bit;
                        ivPhoto.setImageBitmap(bit);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("tag", e.getMessage());
                    Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
                    needLoad = true;
                }
            case 2:
                try {
                    needLoad = false;
                    if (data != null) {
                        Uri uri = data.getData();
                        gifDecoder = new GifDecoder();
                        gifDecoder.read(getContentResolver().openInputStream(uri));
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        tmpBitmap = bit;
                        ivPhoto.setImageBitmap(bit);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    needLoad = true;
                }
                break;
        }

    }

    public String processGif(int channel, int startGray, int grayScale, int pxS) {
        GifDecoder.GifFrame[] frames = this.gifDecoder.getFrames();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);//start
        localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
        if (frames.length > 1)
            localAnimatedGifEncoder.setDelay(frames[1].delay);
        else localAnimatedGifEncoder.setDelay(frames[0].delay);
        for (int i = 0; i < frames.length; i++) {//逐帧处理
            localAnimatedGifEncoder.addFrame(photoToChars(frames[i].image, channel, startGray, grayScale, pxS, isDetailed));
        }
        localAnimatedGifEncoder.finish();//finish

//        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/LiliNote");
//        if (!file.exists()) file.mkdir();
        File f = new File(Environment.getExternalStorageDirectory()+"/photoToChars", System.currentTimeMillis() + ".gif");
        FileOutputStream fos = null;
        try {
            if (!f.exists())
                f.createNewFile();
            path = f.getPath();
            fos = new FileOutputStream(f.getPath());
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_photo, R.id.bt_process})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:
                break;
            case R.id.bt_process:
                break;
        }
    }

    public void onProcessClick(View v) {
        if (needLoad) {
            handler.post(r3);
            return;
        }
        if (!isGif) {//静态图片
            if (tmpBitmap != null) {
                pd = new ProgressDialog(this, 0);
                pd.setCancelable(false);
                if (!pd.isShowing()) {
                    pd.setTitle("计算中");
                    pd.setMessage("请稍等...");
                    pd.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tmpBitmap = photoToChars(tmpBitmap, rgbCode, startGray, scale, pxSize, isDetailed);
                        path = saveImg(tmpBitmap);
                        onProcessFinish();
                    }
                }).start();
            }
        } else {//gif
            if (tmpBitmap != null) {
                pd = new ProgressDialog(this, 0);
                pd.setCancelable(false);
                if (!pd.isShowing()) {
                    pd.setTitle("计算中");
                    pd.setMessage("请稍等...");
                    pd.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        path = processGif(rgbCode, startGray, scale, pxSize);
                        onProcessFinish();
                    }
                }).start();
            }
        }
    }

    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            ivPhoto.setImageBitmap(tmpBitmap);
        }
    };
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "保存已经至" + path + "下", Toast.LENGTH_SHORT).show();
        }
    };

    Runnable r3 = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "请重新加载图片", Toast.LENGTH_SHORT).show();
        }
    };

    public void onProcessFinish() {
        handler.post(r1);
        pd.dismiss();
        handler.post(r2);
    }

    public String saveImg(Bitmap tmp) {
        FileOutputStream out;
        String path = null;
        File file = new File(Environment.getExternalStorageDirectory()+"/photoToChars", System.currentTimeMillis() + ".jpg");
        try {
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file);
            tmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            path = file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("tag", e.getMessage());
            e.printStackTrace();
        }
        return path;
    }

    public Bitmap photoToChars(Bitmap source, int channel, int startGray, int grayScale, int pxS, boolean isGif) {
        //初始化计算
        int repWidth = pxS, repHeight = pxS;//像素大小
        int minGray = 255, maxGray = 0;
        int[] reflection = new int[256];
        List<List<Character>> cList = new ArrayList<List<Character>>();//存放字符映射
        //读取源图片信息
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        //初始化映射字符列表
        for (int i = 0; i < 256; i++) {
            cList.add(new ArrayList<Character>());
        }
        cList.get(0).add(zero);
        cList.get(255).add(full);
        //初始化计算字符灰度值所用图片
        Bitmap tmpBm = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tmpBm);
        Paint paint = new Paint();//创建画笔
        paint.setColor(Color.BLACK);
        paint.setTextSize(10);//像素大小
        paint.setTextAlign(Paint.Align.LEFT);
        //所用字符
       tmpString.trim();
        //开始计算灰度值
        for (int i = 0; i < tmpString.length(); i++) {
            if (String.valueOf(tmpString.charAt(i)) != " ") {
                paint.setColor(Color.WHITE);//清屏
                canvas.drawRect(0, 0, 10, 10, paint);
                int totalL = 0;
                paint.setColor(Color.BLACK);
                canvas.drawText(String.valueOf(tmpString.charAt(i)), 0, 10 - 2, paint);
                //.drawString(String.valueOf(tmpString.charAt(i)), 0, height-2);//像素大小
                for (int h = 0; h < 10; h++) {
                    for (int w = 0; w < 10; w++) {
                        int color = tmpBm.getPixel(w, h);
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);
                        totalL += (r + g + b) / 3;//积累亮度值
                    }
                }
                int ave = totalL / (10 * 10);
                cList.get(ave).add(tmpString.charAt(i));
                if (ave < minGray)
                    minGray = ave;
                if (maxGray < ave)
                    maxGray = ave;
            }
        }
        //初始化颜色映射数值
        for (int i = 0; i < 256; i++) {
            reflection[i] =255;
            if ((i / grayScale + startGray) < 256 && cList.get(i / grayScale + startGray).size() > 0) {
                reflection[i] = i / grayScale + startGray;
            } else {
                int t = 0;
                while (i + t < 256 && i - t >= 0) {
                    if ((i + t) / grayScale + startGray < ((256 / grayScale + startGray) > 255 ? 255 : (256 / grayScale + startGray)) && cList.get((i + t) / grayScale + startGray).size() > 0) {
                        reflection[i] = (i + t) / grayScale + startGray;
                        break;
                    } else if (i - t >= 0 && (i - t) / grayScale + startGray < 256 && cList.get((i - t) / grayScale + startGray).size() > 0) {
                        reflection[i] = (i - t) / grayScale + startGray;
                        break;
                    }
                    ++t;
                }
            }
        }
        //开始绘制输出图片
        int newWidth;
        int newHeight;
        if (!isGif) {
            newWidth = (width / repWidth) * repWidth;
            newHeight = (height / repHeight) * repHeight;
        } else {//gif图片精度更高
            newWidth = (width / repWidth) * repWidth * 2;
            newHeight = (height / repHeight) * repHeight * 2;
        }
        Bitmap output = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.RGB_565);
        Canvas canvasNew = new Canvas(output);
        Paint paintOut = new Paint();//创建画笔
        paintOut.setColor(Color.WHITE);//清屏
        canvasNew.drawRect(0, 0, newWidth, newHeight, paintOut);
        paintOut.setColor(Color.BLACK);
        paintOut.setTextSize(repWidth);//像素大小
        paintOut.setTextAlign(Paint.Align.LEFT);
        if (!isGif) {
            for (int h = 0; h < newHeight; h += repHeight) {
                for (int w = 0; w < newWidth; w += repWidth) {
                    int total = 0;
                    for (int h1 = h; h1 < h + repHeight && h1 < newHeight; h1++) {
                        for (int w1 = w; w1 < w + repWidth && w1 < newWidth; w1++) {
                            int r = Color.red(pixels[(h1 * width + w1)]);
                            int g = Color.green(pixels[(h1 * width + w1)]);
                            int b = Color.blue(pixels[(h1 * width + w1)]);
                            switch (channel) {
                                case 0://全通道
                                    total += r;
                                    total += g;
                                    total += b;
                                    break;
                                case 1:
                                    total += r;
                                    break;
                                case 2:
                                    total += g;
                                    break;
                                case 3:
                                    total += b;
                                    break;
                            }
                        }
                    }
                    if (channel == 0) {
                        total = total / (3 * repHeight * repWidth);
                    } else total = total / (repHeight * repWidth);
                    total = total > 255 ? 255 : total;
                    Random rnd = new Random();
                    int randInt = rnd.nextInt(cList.get(reflection[total]).size() > 1 ? cList.get(reflection[total]).size() - 1 : cList.get(reflection[total]).size());
                    //out.drawString(String.valueOf(cList.get(reflection[total]).get(randInt)),w,h+height-2);
                    canvasNew.drawText(String.valueOf(cList.get(reflection[total]).get(randInt)), w, h + repHeight - 2, paintOut);
                    //Log.d("tag",total+" "+String.valueOf(cList.get(reflection[total]).get(randInt)));
                }
            }
        } else {
            for (int h = 0; h < newHeight / 2; h += repHeight / 2) {
                for (int w = 0; w < newWidth / 2; w += repWidth / 2) {
                    int total = 0;
                    for (int h1 = h; h1 < h + repHeight / 2 && h1 < newHeight / 2; h1++) {
                        for (int w1 = w; w1 < w + repWidth / 2 && w1 < newWidth / 2; w1++) {
                            int r = Color.red(pixels[(h1 * width + w1)]);
                            int g = Color.green(pixels[(h1 * width + w1)]);
                            int b = Color.blue(pixels[(h1 * width + w1)]);
                            switch (channel) {
                                case 0://全通道
                                    total += r;
                                    total += g;
                                    total += b;
                                    break;
                                case 1:
                                    total += r;
                                    break;
                                case 2:
                                    total += g;
                                    break;
                                case 3:
                                    total += b;
                                    break;
                            }
                        }
                    }
                    if (channel == 0) {
                        total = total / (3 * (repHeight / 2) * (repWidth / 2));
                    } else total = total / ((repHeight / 2) * (repHeight / 2));
                    total = total > 255 ? 255 : total;
                    Random rnd = new Random();
                    int randInt = rnd.nextInt(cList.get(reflection[total]).size() > 1 ? cList.get(reflection[total]).size() - 1 : cList.get(reflection[total]).size());
                    //out.drawString(String.valueOf(cList.get(reflection[total]).get(randInt)),w,h+height-2);
                    canvasNew.drawText(String.valueOf(cList.get(reflection[total]).get(randInt)), w * 2, h * 2 + repHeight - 2, paintOut);
                    //Log.d("tag",total+" "+String.valueOf(cList.get(reflection[total]).get(randInt)));
                }
            }
        }
        return output;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {//radiogrouop监听事件
        if (group.equals(rgChannel)) {
            if (checkedId == rbA.getId()) {
                rgbCode = 0;
                return;
            }
            if (checkedId == rbR.getId()) {
                rgbCode = 1;
                return;
            }
            if (checkedId == rbG.getId()) {
                rgbCode = 2;
                return;
            }
            if (checkedId == rbB.getId()) {
                rgbCode = 3;
                return;
            }
        }
        if (group.equals(rgGrayStart)) {
            if (checkedId == rb0.getId()) {
                startGray = 0;
                return;
            }
            if (checkedId == rb64.getId()) {
                startGray = 64;
                return;
            }
            if (checkedId == rb128.getId()) {
                startGray = 128;
                return;
            }
            if (checkedId == rb160.getId()) {
                startGray = 160;
                return;
            }
            if (checkedId == rb192.getId()) {
                startGray = 192;
                return;
            }
        }
        if (group.equals(rgScale)) {
            if (checkedId == rb1.getId()) {
                scale = 1;
                return;
            }
            if (checkedId == rb2.getId()) {
                scale = 2;
                return;
            }
            if (checkedId == rb4.getId()) {
                scale = 4;
                return;
            }
        }
        if (group.equals(rgCharset)) {
            if (checkedId == rbStdset.getId()) {
                full='█';
                tmpString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ⊙●○①⊕◎Θ⊙¤㊣★☆♀◆◇◣◢◥▲▼△▽⊿◤ ◥ ▂ ▃ ▄ ▅ ▆ ▇ █ █ ■ ▓ 回 □ 〓≡ ╝╚╔ ╗╬ ═ ╓ ╩ ┠ ┨┯ ┷┏ ┓┗ ┛┳⊥﹃﹄┌ ┐└ ┘∟「」↑↓→←↘↙♀♂┇┅ ﹉﹊﹍﹎╭ ╮╰ ╯ *^_^* ^*^ ^-^ ^_^ ^︵^ ∵∴‖︱ ︳︴﹏﹋﹌︵︶︹︺ 【】〖〗＠﹕﹗/ \" _ < > `,·。≈{}~ ～() _ -『』√ $ @ * & # ※ 卐 々∞Ψ ∪∩∈∏ の ℡ ぁ §∮〝〞ミ灬ξ№∑⌒ξζω＊ㄨ ≮≯ ＋－×÷﹢﹣±／＝∫∮∝ ∞ ∧∨ ∑ ∏ ∥∠ ≌ ∽ ≦ ≧ ≒﹤﹥じ☆veve↑↓⊙●★☆■♀『』◆◣◥▲Ψ ※◤ ◥ →№←㊣∑⌒〖〗＠ξζω□∮〓※∴ぷ▂▃▅▆█ ∏卐【】△√ ∩¤々♀♂∞①ㄨ≡↘↙▂ ▂ ▃ ▄ ▅ ▆ ▇ █┗┛╰☆╮、。·ˉˇ¨〃々—～‖…‘’“”〔〕〈 〉《》「」『』〖〗【】±＋－×÷∧∨∑∏∪∩∈√⊥∥∠⌒⊙∫∮≡≌≈∽∝≠≮≯≤≥∞∶∵∴∷♂♀°′″℃＄¤￠￡‰§№☆★〇○●◎◇◆ 回□■△▽⊿▲▼◣◤◢◥▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▓※→←↑↓↖↗↘↙〓ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹ①②③④⑤⑥⑦⑧⑨⑩⒈⒉⒊⒋ ⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ！＂＃￥％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨ ＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθ ικλμνξοπρστυφχψω︵︶︹︺︿﹀︽︾﹁﹂﹃﹄︻︼︷︸АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюāáǎàēéěèīíǐìōóǒòūúǔ ùǖǘǚǜüêɑńňɡㄅㄆㄇㄈㄉㄊㄋㄌㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩ︱︳︴﹏﹋﹌─━│┃┄┅┆┇┈┉┊┋┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇ ╈╉╊╋⊕㊣㈱曱甴囍∟┅﹊﹍╭ ╮╰ ╯_ ^︵^﹕﹗ < > `,·。{}~～() -√ $ @ * & # 卐℡ ぁ〝〞ミ灬№＊ㄨ≮≯﹢﹣／∝≌∽≦≧≒﹤﹥じぷ┗┛￥￡§я-―‥…‰′″℅℉№℡∕∝∣═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╱╲╳▔▕〆〒〡〢〣〤〥〦〧〨〩㎎ ㎏ ㎜ ㎝ ㎞ ㎡ ㏄ ㏎我";;
                return;
            }
            if (checkedId == rbChineseset.getId()) {
                full='罐';
                tmpString = "一乙二十丁 厂 七 卜 人 入 八 九 几 儿 了 力 乃 刀 又三 于 干 亏 士 工 土 才 寸 下 大 丈 与 万 上 小 口 巾 山 千 乞 川 亿 个 勺 久 凡 及 夕 丸 么广亡门 义 之 尸 弓 己 已 子 卫 也 女 飞 刃 习 叉 马 乡丰 王 井 开 夫 天 无 元专 云 扎 艺 木 五 支 厅 不 太 犬 区 历 尤 友 匹 车 巨 牙 屯 比 互 切 瓦止少 日 中 冈 贝 内 水 见 午 牛 手 毛 气 升 长仁 什 片 仆 化 仇 币 仍 仅 斤 爪 反 介 父 从 今 凶 分乏公 仓 月 氏 勿 欠 风 丹 匀 乌 凤 勾 文 六 方 火 为 斗 忆 订 计户 认 心 尺 引 丑 巴 孔 队 办 以 允予劝 双 书 幻玉 刊 示 末 未 击 打 巧正 扑 扒 功 扔 去 甘 世 古 节 本 术 可 丙 左 厉 右 石 布 龙 平 灭 轧 东卡北 占 业 旧 帅 归 且 旦 目 叶 甲 申 叮 电 号田 由 史 只 央 兄 叼 叫 另 叨 叹 四 生 失 禾 丘 付 仗代仙 们 仪 白 仔 他 斥 瓜 乎 丛 令 用 甩 印 乐 句 匆 册 犯 外 处冬 鸟 务 包 饥 主 市 立 闪 兰 半 汁汇头 汉 宁 穴 它 讨 写 让 礼 训 必 议 讯 记 永 司 尼 民 出 辽 奶 奴 加 召 皮 边 发孕 圣 对 台 矛 纠母幼 丝式 刑 动 扛 寺 吉 扣 考托 老 执 巩 圾 扩 扫 地 扬 场 耳 共 芒 亚 芝 朽 朴 机 权 过 臣 再 协 西压厌 在 有 百 存 而 页 匠 夸 夺 灰 达 列 死 成夹 轨 邪 划 迈 毕 至 此 贞 师 尘 尖 劣 光 当 早 吐 吓虫曲 团 同 吊 吃 因 吸 吗 屿 帆 岁 回 岂 刚 则 肉 网 年 朱 先 丢舌 竹 迁 乔 伟 传 乒 乓 休 伍 伏 优伐延 件 任 伤 价 份 华 仰 仿 伙 伪 自 血 向 似 后 行 舟 全 会 杀 合 兆 企 众 爷 伞创 肌 朵 杂 危 旬旨负 各 名 多 争 色 壮 冲 冰 庄 庆 亦 刘 齐 交 次 衣 产 决 充 妄 闭 问 闯 羊 并 关 米 灯 州 汗 污江池汤 忙 兴 宇 守 宅 字 安 讲 军 许 论 农 讽 设 访 寻 那 迅 尽 导 异 孙 阵 阳 收 阶 阴 防 奸 如 妇 好她妈 戏 羽 观 欢买 红 纤 级 约 纪 驰 巡寿 弄 麦 形 进 戒 吞 远违 运 扶 抚 坛 技 坏 扰 拒 找 批 扯 址 走 抄 坝 贡 攻 赤 折 抓 扮 抢 孝均抛 投 坟 抗 坑 坊 抖 护 壳 志 扭 块 声 把 报却 劫 芽 花 芹 芬 苍 芳 严 芦 劳 克 苏 杆 杠 杜 材 村杏极 李 杨 求 更 束 豆 两 丽 医 辰 励 否 还 歼 来 连 步 坚 旱 盯呈 时 吴 助 县 里 呆 园 旷 围 呀 吨足邮 男 困 吵 串 员 听 吩 吹 呜 吧 吼 别 岗 帐 财 针 钉 告 我 乱 利 秃 秀 私 每 兵估 体 何 但 伸 作伯伶 佣 低 你 住 位 伴 身 皂 佛 近 彻 役 返 余 希 坐 谷 妥 含 邻 岔 肝 肚 肠 龟 免 狂 犹 角 删 条卵岛迎 饭 饮 系 言 冻 状 亩 况 床 库 疗 应 冷 这 序 辛 弃 冶 忘 闲 间 闷 判 灶 灿 弟 汪 沙 汽 沃 泛 沟没沈 沉 怀 忧 快完 宋 宏 牢 究 穷 灾 良 证 启 评 补 初 社 识 诉 诊 词 译 君 灵 即 层 尿 尾 迟 局 改张忌 际 陆 阿 陈 阻 附 妙 妖 妨 努忍 劲 鸡 驱 纯 纱 纳 纲 驳 纵 纷 纸 纹 纺 驴 纽奉 玩 环 武 青 责 现 表规 抹 拢 拔 拣 担 坦 押 抽 拐 拖 拍 者 顶 拆 拥 抵 拘 势 抱 垃 拉 拦 拌幸招 坡 披 拨 择 抬 其 取 苦 若 茂 苹 苗 英 范直 茄 茎 茅 林 枝 杯 柜 析 板 松 枪 构 杰 述 枕 丧 或画卧 事 刺 枣 雨 卖 矿 码 厕 奔 奇 奋 态 欧 垄 妻 轰 顷 转 斩 轮软 到 非 叔 肯 齿 些 虎 虏 肾 贤 尚旺具 果 味 昆 国 昌 畅 明 易 昂 典 固 忠 咐 呼 鸣 咏 呢 岸 岩 帖 罗 帜 岭 凯 败 贩购 图 钓 制 知 垂牧物 乖 刮 秆 和 季 委 佳 侍 供 使 例 版 侄 侦 侧 凭 侨 佩 货 依 的 迫 质 欣 征 往 爬 彼 径 所 舍金命斧 爸 采 受 乳 贪 念 贫 肤 肺 肢 肿 胀 朋 股 肥 服 胁 周 昏 鱼 兔 狐 忽 狗 备 饰 饱 饲 变 京 享 店夜庙 府 底 剂 郊废 净 盲 放 刻 育 闸 闹 郑 券 卷 单 炒 炊 炕 炎 炉 沫 浅 法 泄 河 沾 泪 油 泊 沿 泡注泻 泳 泥 沸 波 泼 泽 治 怖 性 怕怜 怪 学 宝 宗 定 宜 审 宙 官 空 帘 实 试 郎 诗 肩 房 诚 衬 衫 视话诞 询 该 详 建 肃 录 隶 居 届 刷 屈 弦 承 孟 孤 陕降 限 妹 姑 姐 姓 始 驾 参 艰 线 练 组 细 驶 织终驻 驼 绍 经 贯奏 春 帮 珍 玻 毒 型 挂封 持 项 垮 挎 城 挠 政 赴 赵 挡 挺 括 拴 拾 挑 指 垫 挣 挤 拼 挖 按 挥挪某 甚 革 荐 巷 带 草 茧 茶 荒 茫 荡 荣 故 胡南 药 标 枯 柄 栋 相 查 柏 柳 柱 柿 栏 树 要 咸 威 歪研砖 厘 厚 砌 砍 面 耐 耍 牵 残 殃 轻 鸦 皆 背 战 点 临 览 竖 省削 尝 是 盼 眨 哄 显 哑 冒 映 星 昨畏趴 胃 贵 界 虹 虾 蚁 思 蚂 虽 品 咽 骂 哗 咱 响 哈 咬 咳 哪 炭 峡 罚 贱 贴 骨 钞钟 钢 钥 钩 卸 缸拜看 矩 怎 牲 选 适 秒 香 种 秋 科 重 复 竿 段 便 俩 贷 顺 修 保 促 侮 俭 俗 俘 信 皇 泉 鬼 侵 追俊盾待 律 很 须 叙 剑 逃 食 盆 胆 胜 胞 胖 脉 勉 狭 狮 独 狡 狱 狠 贸 怨 急 饶 蚀 饺 饼 弯 将 奖 哀 亭亮度 迹 庭 疮 疯疫 疤 姿 亲 音 帝 施 闻 阀 阁 差 养 美 姜 叛 送 类 迷 前 首 逆 总 炼 炸 炮 烂 剃 洁洪洒 浇 浊 洞 测 洗 活 派 洽 染 济洋 洲 浑 浓 津 恒 恢 恰 恼 恨 举 觉 宣 室 宫 宪 突 穿 窃 客 冠 语扁袄 祖 神 祝 误 诱 说 诵 垦 退 既 屋 昼 费 陡 眉 孩除 险 院 娃 姥 姨 姻 娇 怒 架 贺 盈 勇 怠 柔 垒绑绒 结 绕 骄 绘 给 络 骆 绝 绞 统耕 耗 艳 泰 珠 班 素 蚕顽 盏 匪 捞 栽 捕 振 载 赶 起 盐 捎 捏 埋 捉 捆 捐 损 都 哲 逝 捡 换 挽热恐 壶 挨 耻 耽 恭 莲 莫 荷 获 晋 恶 真 框 桂档 桐 株 桥 桃 格 校 核 样 根 索 哥 速 逗 栗 配 翅 辱唇夏 础 破 原 套 逐 烈 殊 顾 轿 较 顿 毙 致 柴 桌 虑 监 紧 党 晒眠 晓 鸭 晃 晌 晕 蚊 哨 哭 恩 唤 啊唉罢 峰 圆 贼 贿 钱 钳 钻 铁 铃 铅 缺 氧 特 牺 造 乘 敌 秤 租 积 秧 秩 称 秘 透 笔笑 笋 债 借 值 倚倾倒 倘 俱 倡 候 俯 倍 倦 健 臭 射 躬 息 徒 徐 舰 舱 般 航 途 拿 爹 爱 颂 翁 脆 脂 胸 胳 脏 胶 脑狸狼逢 留 皱 饿 恋 桨 浆 衰 高 席 准 座 脊 症 病 疾 疼 疲 效 离 唐 资 凉 站 剖 竞 部 旁 旅 畜 阅 羞 瓶拳粉 料 益 兼 烤烘 烦 烧 烛 烟 递 涛 浙 涝 酒 涉 消 浩 海 涂 浴 浮 流 润 浪 浸 涨 烫 涌 悟 悄 悔 悦害宽 家 宵 宴 宾 窄 容 宰 案 请 朗诸 读 扇 袜 袖 袍 被 祥 课 谁 调 冤 谅 谈 谊 剥 恳 展 剧 屑 弱 陵陶陷 陪 娱 娘 通 能 难 预 桑 绢 绣 验 继球 理 捧 堵 描 域 掩 捷排 掉 堆 推 掀 授 教 掏 掠 培 接 控 探 据 掘 职 基 著 勒 黄 萌 萝 菌 菜萄菊 萍 菠 营 械 梦 梢 梅 检 梳 梯 桶 救 副 票戚 爽 聋 袭 盛 雪 辅 辆 虚 雀 堂 常 匙 晨 睁 眯 眼 悬野啦 晚 啄 距 跃 略 蛇 累 唱 患 唯 崖 崭 崇 圈 铜 铲 银 甜 梨 犁移 笨 笼 笛 符 第 敏 做 袋 悠 偿 偶偷您 售 停 偏 假 得 衔 盘 船 斜 盒 鸽 悉 欲 彩 领 脚 脖 脸 脱 象 够 猜 猪 猎 猫 猛馅 馆 凑 减 毫 麻痒痕 廊 康 庸 鹿 盗 章 竟 商 族 旋 望 率 着 盖 粘 粗 粒 断 剪 兽 清 添 淋 淹 渠 渐 混 渔 淘 液 淡深婆梁 渗 情 惜 惭 悼 惧 惕 惊 惨 惯 寇 寄 宿 窑 密 谋 谎 祸 谜 逮 敢 屠 弹 随 蛋 隆 隐 婚 婶 颈 绩 绪续骑 绳 维 绵 绸绿琴 斑 替 款 堪 搭 塔 越趁 趋 超 提 堤 博 揭 喜 插 揪 搜 煮 援 裁 搁 搂 搅 握 揉 斯 期 欺 联 散惹葬 葛 董 葡 敬 葱 落 朝 辜 葵 棒 棋 植 森 椅椒 棵 棍 棉 棚 棕 惠 惑 逼 厨 厦 硬 确 雁 殖 裂 雄 暂雅辈 悲 紫 辉 敞 赏 掌 晴 暑 最 量 喷 晶 喇 遇 喊 景 践 跌 跑 遗蛙 蛛 蜓 喝 喂 喘 喉 幅 帽 赌 赔 黑铸铺 链 销 锁 锄 锅 锈 锋 锐 短 智 毯 鹅 剩 稍 程 稀 税 筐 等 筑 策 筛 筒 答 筋 筝傲 傅 牌 堡 集 焦傍储 奥 街 惩 御 循 艇 舒 番 释 禽 腊 脾 腔 鲁 猾 猴 然 馋 装 蛮 就 痛 童 阔 善 羡 普 粪 尊 道 曾焰港湖 渣 湿 温 渴 滑 湾 渡 游 滋 溉 愤 慌 惰 愧 愉 慨 割 寒 富 窜 窝 窗 遍 裕 裤 裙 谢 谣 谦 属 屡 强粥疏 隔 隙 絮 嫂登 缎 缓 编 骗 缘瑞 魂 肆 摄 摸 填 搏 塌鼓 摆 携 搬 摇 搞 塘 摊 蒜 勤 鹊 蓝 墓 幕 蓬 蓄 蒙 蒸 献 禁 楚 想 槐 榆楼概 赖 酬 感 碍 碑 碎 碰 碗 碌 雷 零 雾 雹 输督 龄 鉴 睛 睡 睬 鄙 愚 暖 盟 歇 暗 照 跨 跳 跪 路 跟遣蛾 蜂 嗓 置 罪 罩 错 锡 锣 锤 锦 键 锯 矮 辞 稠 愁 筹 签 简 毁舅 鼠 催 傻 像 躲 微 愈 遥 腰 腥 腹腾腿 触 解 酱 痰 廉 新 韵 意 粮 数 煎 塑 慈 煤 煌 满 漠 源 滤 滥 滔 溪 溜 滚 滨 粱滩 慎 誉 塞 谨 福群殿 辟 障 嫌 嫁 叠 缝 缠静 碧 璃 墙 撇 嘉 摧 截誓 境 摘 摔 聚 蔽 慕 暮 蔑 模 榴 榜 榨 歌 遭 酷 酿 酸 磁 愿 需 弊 裳 颗嗽蜻 蜡 蝇 蜘 赚 锹 锻 舞 稳 算 箩 管 僚 鼻 魄貌 膜 膊 膀 鲜 疑 馒 裹 敲 豪 膏 遮 腐 瘦 辣 竭 端 旗精歉 熄 熔 漆 漂 漫 滴 演 漏 慢 寨 赛 察 蜜 谱 嫩 翠 熊 凳 骡 缩慧 撕 撒 趣 趟 撑 播 撞撤 增 聪 鞋 蕉 蔬 横 槽 樱 橡 飘 醋 醉 震 霉 瞒 题 暴 瞎 影 踢 踏 踩 踪蝶蝴 嘱 墨 镇 靠 稻 黎 稿 稼 箱 箭 篇 僵 躺 僻德 艘 膝 膛 熟 摩 颜 毅 糊 遵 潜 潮 懂 额 慰 劈操 燕 薯 薪 薄 颠 橘 整融 醒 餐 嘴 蹄 器 赠 默 镜 赞 篮 邀 衡 膨 雕 磨 凝 辨 辩 糖 糕 燃 澡 激懒壁 避 缴戴 擦 鞠 藏 霜 霞 瞧 蹈螺 穗 繁 辫 赢 糟 糠 燥 臂 翼 骤鞭 覆 蹦 镰 翻 鹰警 攀 蹲 颤 瓣 爆 疆壤 耀 躁 嚼 嚷 籍 魔 灌蠢 霸 露囊罐⊙﹕﹗ < > `,·。{}~～() -√ $ @ * & #ξζω□∮℃＄¤￠￡‰§№☆";
                return;
            }
            if (checkedId == rbEngset.getId()) {
                full='@';
                tmpString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz⊙﹕﹗ < > `,·。{}~～() -√ $ @ * & #ξζω□∮℃＄¤￠￡‰§№☆★";
                return;
            }
        }
        if (group.equals(rgUnitpx)) {
            if (checkedId == rbPx6.getId()) {
                pxSize = 6;
                return;
            }
            if (checkedId == rbPx10.getId()) {
                pxSize = 10;
                return;
            }
            if (checkedId == rbPx14.getId()) {
                pxSize = 14;
                return;
            }
            if (checkedId == rbPx18.getId()) {
                pxSize = 18;
                return;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.equals(tbGif)){
            needLoad = true;
            if (isChecked)
                isGif = false;
            else isGif = true;
        }
        else if(buttonView.equals(tbDetail)){
            if (isChecked)
                isDetailed = false;
            else isDetailed = true;
        }
    }
}
