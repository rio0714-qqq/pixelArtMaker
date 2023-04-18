package com.example.pixelartmaker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class SquareView extends View {

    private boolean mAdjustWidth;
    private ArrayList<Point> points = new ArrayList<Point>();
    private Path path; // 1回層
    int squareSize;
    private static int measureSpec;
    // グローバル変数に持たせる
    float dx;
    float dy;
    int x;
    int y;
    int CELL_kazu = 24;
    int BLK_SIZE = 1080 / CELL_kazu; // 360
    public static int count;
    private Boolean gridClearFlg;

    // ビットマップ用 いらない？
//    private Paint paintArt;
    // ペイント用
    private Paint paint;

    // canvas処理(bitmap、canvas)
    //  グリッド用
    private Bitmap gridBitmap;
    private Canvas gridCanvas;
    //  作業用
    private Bitmap devBitmap;
    private Canvas devCanvas;
    //  保存用
    public Bitmap masterBitmap;
    public Canvas masterCanvas;

    public SquareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFocusable(true);

        mAdjustWidth = false;
        if (attrs != null) {
            TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.SquareView);
            mAdjustWidth = attrsArray.getBoolean(R.styleable.SquareView_adjust_width, false);
            attrsArray.recycle();
        }

        this.path = new Path();
        paint = new Paint();

//        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 端末の横幅に合わせて正方形になるように縦幅指定
        squareSize = MeasureSpec.getSize(widthMeasureSpec);
        measureSpec = MeasureSpec.makeMeasureSpec(squareSize, MeasureSpec.EXACTLY);

        super.onMeasure(measureSpec, measureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Grid用Bitmap、canvas作成
        this.gridBitmap = null;
        this.gridBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.gridCanvas = null;
        this.gridCanvas = new Canvas(this.gridBitmap);

        // 作業用Bitmap、canvas作成
        this.devBitmap = null;
        this.devBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.devCanvas = null;
        this.devCanvas = new Canvas(this.devBitmap);

        // 保存用Bitmap、canvas作成
        this.masterBitmap = null;
        this.masterBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.masterCanvas = null;
        this.masterCanvas = new Canvas(this.masterBitmap);

        super.onSizeChanged(w, h, oldw, oldh);
        Log.v("LifeCycle", "onResume");
    }

    public void setAdjustWidth(boolean adjustWidth) {
        mAdjustWidth = adjustWidth;
    }

    // Viewのカスタム
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        // グリッド線のpaint
        Paint paintGrid = new Paint();

        //お絵かきのpaint
        Paint paintArt = new Paint();

        Point po = null;

        // グリッド描画　on/offはフラグ管理
        drawGrid(paintGrid, gridCanvas);

        // 作業用にドット絵描画 for文処理は作業用だけに ページ遷移時は呼ばない
        if (dx != 0 && dy != 0) {
            paintArt.setColor(Color.rgb(Integer.parseInt(PixelArtMakeActivity.colorR),
                    Integer.parseInt(PixelArtMakeActivity.colorG),
                    Integer.parseInt(PixelArtMakeActivity.colorB)));
            paintArt.setStyle(Paint.Style.FILL);
        }
        if (!PixelArtMakeActivity.forFlg) {
            drawPixelArt(paintArt, devCanvas);
        }

        // 保存用にマージ&作業用空
        mergePixelArt(paintGrid, paintArt, canvas, path);

        // grid線表示/非表示
        if (gridClearFlg) {
            gridCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            gridBitmap.eraseColor(Color.TRANSPARENT);
            gridClearFlg = false;
        }
        canvas.drawPath(path, paint);

//        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        dx = event.getX();
        dy = event.getY();

        Log.i("dx", String.valueOf(dx));
        Log.i("dy", String.valueOf(dy));

        x = (int) (dx / BLK_SIZE); // タッチした座標を丸める
        y = (int) (dy / BLK_SIZE); //

        Log.i("a", String.valueOf(x));
        Log.i("a", String.valueOf(y));
        points.add(new Point(x, y));

        path.reset();
        invalidate();

        PixelArtMakeActivity.forFlg = false;
        return true;
    }

    // マス目を生成
    public void drawGrid(Paint paint, Canvas canvas) {
        for (int i = 0; i <= CELL_kazu; i++) {
            for (int j = 0; j <= CELL_kazu; j++) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(3);
                paint.setStyle(Paint.Style.STROKE);
                gridCanvas.drawRect(i * BLK_SIZE, j * BLK_SIZE, BLK_SIZE, BLK_SIZE, paint);
                // 格子状のビットマップ作成
                gridCanvas.drawBitmap(this.gridBitmap, 0, 0, paint);
            }
        }
    }

    // ドット絵描写　for文回す処理　作業用のみ
    // 左上から順にマスを見ていっているから色が変わってしまう
    // canvasを別でもつ
    public void drawPixelArt(Paint paint, Canvas canvas) {
        if (dx != 0 && dy != 0) {
//            paint.setColor(Color.rgb(Integer.parseInt(PixelArtMakeActivity.colorR),
//                    Integer.parseInt(PixelArtMakeActivity.colorG),
//                    Integer.parseInt(PixelArtMakeActivity.colorB)));
//            paint.setStyle(Paint.Style.FILL);
            for(int i=0; i<points.size(); i++) {
                Point cc = points.get(i);
                Rect rect = new Rect(cc.x * BLK_SIZE, cc.y * BLK_SIZE,
                        cc.x * BLK_SIZE + BLK_SIZE, cc.y * BLK_SIZE + BLK_SIZE);
                devCanvas.drawRect(rect, paint);
                devCanvas.drawBitmap(this.devBitmap, 0, 0, paint);
            }
        }
        points.clear();
    }
    // 保存用にマージ&作業用空
    public void mergePixelArt(Paint paintGrid, Paint paintArt, Canvas canvas, Path path) {


        // 合成する 保存用に重ねていくイメージ
//        masterCanvas.drawBitmap(gridBitmap, 0, 0, paint);
        masterCanvas.drawBitmap(devBitmap, 0, 0, paint);
        // 作業用を空に　この時点で表示されない
        devCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        devBitmap.eraseColor(Color.TRANSPARENT);

        // キャンバスに保存用を表示
        canvas.drawBitmap(this.masterBitmap, 0,0, paint);
        canvas.drawBitmap(this.gridBitmap, 0,0, paint);
    }

    // grid無効
    public void gridFalse() {

    }
    // 色指定
    public void setPathColor(int color) {
        paint.setColor(color);
    }

    public void gridClear(boolean flg) {
        gridClearFlg = flg;
        path.reset();
        invalidate();
    }

    public void saveAsPngImage(Bitmap saveImage) throws IOException {
        try {
            File extStrageDir =
                    Environment.getExternalStorageDirectory();
            File file = new File(
                    extStrageDir.getAbsolutePath()
                            + "/" + Environment.DIRECTORY_PICTURES,
                    getFileName());
            FileOutputStream outStream = new FileOutputStream(file);
            masterBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            Toast.makeText(
                    getContext(),
                    "Image saved",
                    Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getFileName(){
        Calendar c = Calendar.getInstance();
        String s = c.get(Calendar.YEAR)
                + "_" + (c.get(Calendar.MONTH)+1)
                + "_" + c.get(Calendar.DAY_OF_MONTH)
                + "_" + c.get(Calendar.HOUR_OF_DAY)
                + "_" + c.get(Calendar.MINUTE)
                + "_" + c.get(Calendar.SECOND)
                + "_" + c.get(Calendar.MILLISECOND)
                + ".png";
        return s;

    }


}