package com.example.pixelartmaker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;

public class SquareView extends View {

    private boolean mAdjustWidth;

    // Viewのサイズ
    private static int measureSpec;
    float squareSize;

    // タッチした座標
    float dx;
    float dy;
    // 座標のセル
    int x;
    int y;
    // タッチした座標を配列で保持
    private ArrayList<Point> points = new ArrayList<Point>();

    // セルの数
    public static int numberOfCell;
    // セルのサイズ
    float cellSize;

    // パレット用カウント
    public static int count;
    // グリッド線非表示フラグ
    private Boolean gridClearFlg;

    // ペイント
    private Paint paint;
    // パス
    private Path path;

    // グリッド用
    private Bitmap gridBitmap;
    private Canvas gridCanvas;
    // 作業用
    private Bitmap devBitmap;
    private Canvas devCanvas;
    // 保存用
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

        // ペイントの設定
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 端末の横幅に合わせて正方形になるように縦幅指定
        squareSize = MeasureSpec.getSize(widthMeasureSpec);
        measureSpec = MeasureSpec.makeMeasureSpec((int) squareSize, MeasureSpec.EXACTLY);

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

        // サイズを変更したタイミングでgrid設定
//        numberOfCell = 20;
        cellSize = squareSize / numberOfCell;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setAdjustWidth(boolean adjustWidth) {
        mAdjustWidth = adjustWidth;
    }

    // Viewのカスタム
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // グリッド線のpaint
        Paint paintGrid = new Paint();

        // お絵かきのpaint
        Paint paintArt = new Paint();

//        Point po = null;

        // グリッド描画
        drawGrid(paintGrid);

        // 新規色指定
        if ((dx != 0.0 && dy != 0.0)
                && PixelArtMakeActivity.colorRGB != null
                    && PixelArtMakeActivity.colorRGB.get(0) != null
                        && PixelArtMakeActivity.colorRGB.get(1) != null
                            && PixelArtMakeActivity.colorRGB.get(2) != null) {
            paintArt.setColor(Color.rgb((PixelArtMakeActivity.colorRGB.get(0)),
                            (PixelArtMakeActivity.colorRGB.get(1)),
                    (PixelArtMakeActivity.colorRGB.get(2))));
            paintArt.setStyle(Paint.Style.FILL);
        } else {
            paintArt.setColor(Color.alpha(0));
        }
        // 作業用にドット絵描画  drawPixelArtの処理は作業用だけに適用する
        if (!PixelArtMakeActivity.forFlg) {
            drawPixelArt(paintArt, devCanvas);
        }

        // 保存用にマージ&作業用空
        mergePixelArt(canvas);

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

        // タッチした座標をセルサイズに丸める
        x = (int) (dx / cellSize);
        y = (int) (dy / cellSize);

        Log.i("a", String.valueOf(x));
        Log.i("a", String.valueOf(y));
        points.add(new Point(x, y));

        // パスをリセット
        path.reset();
        // onDraw呼ぶ
        invalidate();

        // drawPixelArt呼び出し処理用フラグ
        PixelArtMakeActivity.forFlg = false;
        return true;
    }

    // grid生成
    public void drawGrid(Paint paint) {
        for (int i = 0; i <= numberOfCell; i++) {
            for (int j = 0; j <= numberOfCell; j++) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(3);
                paint.setStyle(Paint.Style.STROKE);
                gridCanvas.drawRect(i * cellSize, j * cellSize, cellSize, cellSize, paint);
                // 格子状のgridビットマップ作成
                gridCanvas.drawBitmap(this.gridBitmap, 0, 0, paint);
            }
        }
    }

    // ドット絵描写　作業用のみ
    // 左上から順にマスを見ていく処理で色が変わってしまうため
    public void drawPixelArt(Paint paint, Canvas canvas) {
        if (dx != 0 && dy != 0) {
            for(int i=0; i<points.size(); i++) {
                Point cc = points.get(i);
                devCanvas.drawRect(cc.x * cellSize, cc.y * cellSize,
                        cc.x * cellSize + cellSize, cc.y * cellSize + cellSize, paint);
                devCanvas.drawBitmap(this.devBitmap, 0, 0, paint);
            }
        }
        // 座標の配列を空に
        points.clear();
    }


    // 保存用にマージ&作業用空
    public void mergePixelArt(Canvas canvas) {
        // 合成する 保存用に重ねていく
        masterCanvas.drawBitmap(devBitmap, 0, 0, paint);

        // 作業用を空に(透明で上書き)
        devCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        devBitmap.eraseColor(Color.TRANSPARENT);

        // キャンバスに保存用を表示
        canvas.drawBitmap(this.masterBitmap, 0,0, paint);
        canvas.drawBitmap(this.gridBitmap, 0,0, paint);
    }

    // 色指定
    public void setPathColor(int color) {
        paint.setColor(color);
    }

    // grid線非表示
    public void gridClear(boolean flg) {
        gridClearFlg = flg;
        path.reset();
        invalidate();
    }

    // ファイル書き出し処理
    public void saveAsPngImage(Bitmap saveImage) throws IOException {
        try {
            File extStrageDir =
                    Environment.getExternalStorageDirectory();
            File file = new File(extStrageDir.getAbsolutePath()
                            + "/" + Environment.DIRECTORY_PICTURES, getFileName());
            FileOutputStream outStream = new FileOutputStream(file);
            masterBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            Toast.makeText(
                    getContext(), "保存完了!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイル名取得
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