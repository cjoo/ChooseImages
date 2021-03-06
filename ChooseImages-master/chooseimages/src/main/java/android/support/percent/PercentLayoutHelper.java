//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;

import com.cj.android.chooseimages.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PercentLayoutHelper {
    private static final String TAG = "PercentLayout";
    private final ViewGroup mHost;

    public PercentLayoutHelper(ViewGroup host) {
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(LayoutParams params, TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (Log.isLoggable("PercentLayout", 3)) {
            Log.d("PercentLayout", "adjustChildren: " + this.mHost + " widthMeasureSpec: " + MeasureSpec.toString(widthMeasureSpec) + " heightMeasureSpec: " + MeasureSpec.toString(heightMeasureSpec));
        }

        int widthHint = MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = MeasureSpec.getSize(heightMeasureSpec);
        int i = 0;

        for (int N = this.mHost.getChildCount(); i < N; ++i) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable("PercentLayout", 3)) {
                Log.d("PercentLayout", "should adjust " + view + " " + params);
            }

            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable("PercentLayout", 3)) {
                    Log.d("PercentLayout", "using " + info);
                }

                if (info != null) {
                    if (params instanceof MarginLayoutParams) {
                        info.fillMarginLayoutParams((MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }

    }

    private static Point screenSize = new Point();
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;

    @IntDef({WIDTH, HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    private static float getPercent(@Direction int direction, String value) {
        float percent = -1;
        if (direction == WIDTH) {
            if (value.endsWith("w")) {
                percent = Float.parseFloat(value.replace("w", ""));
            } else if (value.endsWith("h")) {
                percent = Float.parseFloat(value.replace("h", ""));
                percent = percent / screenSize.x * screenSize.y;
            }
        } else if (direction == HEIGHT) {
            if (value.endsWith("w")) {
                percent = Float.parseFloat(value.replace("w", ""));
                percent = percent / screenSize.y * screenSize.x;
            } else if (value.endsWith("h")) {
                percent = Float.parseFloat(value.replace("h", ""));
            }
        }
        return percent;
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context, AttributeSet attrs) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getSize(screenSize);//getSize(Point) API level 13 以上
        screenSize.x = wm.getDefaultDisplay().getWidth();
        screenSize.y = wm.getDefaultDisplay().getHeight();

        PercentLayoutInfo info = null;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        String value = array.getString(R.styleable.PercentLayout_Layout_layout_widthPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent width: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.widthPercent = getPercent( WIDTH, value);
        }
        value = array.getString(R.styleable.PercentLayout_Layout_layout_heightPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent height: " + value);
            }
            info = info != null ? info : new PercentLayoutInfo();
            info.heightPercent = getPercent( HEIGHT, value);
        }

        value = array.getString(R.styleable.PercentLayout_Layout_layout_marginPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.leftMarginPercent = getPercent( WIDTH, value);
            info.topMarginPercent = getPercent( HEIGHT, value);
            info.rightMarginPercent = getPercent( WIDTH, value);
            info.bottomMarginPercent = getPercent( HEIGHT, value);
        }

        value = array.getString(R.styleable.PercentLayout_Layout_layout_marginLeftPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent left margin: " + value);
            }
            info = info != null ? info : new PercentLayoutInfo();
            info.leftMarginPercent = getPercent( WIDTH, value);
        }

        value = array.getString(R.styleable.PercentLayout_Layout_layout_marginTopPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent top margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.topMarginPercent = getPercent( HEIGHT, value);
        }

        value = array.getString(R.styleable.PercentLayout_Layout_layout_marginRightPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent right margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.rightMarginPercent = getPercent( WIDTH, value);
        }

        value = array.getString(R.styleable.PercentLayout_Layout_layout_marginBottomPercent);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent bottom margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.bottomMarginPercent = getPercent( HEIGHT, value);
        }

        float valueF = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0F);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent start margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.startMarginPercent = valueF;
        }

        valueF = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0F);
        if (!TextUtils.isEmpty(value)) {
            if (Log.isLoggable("PercentLayout", 2)) {
                Log.v("PercentLayout", "percent end margin: " + value);
            }

            info = info != null ? info : new PercentLayoutInfo();
            info.endMarginPercent = valueF;
        }

        array.recycle();
        if (Log.isLoggable("PercentLayout", 3)) {
            Log.d("PercentLayout", "constructed: " + info);
        }

        return info;
    }

    public void restoreOriginalParams() {
        int i = 0;

        for (int N = this.mHost.getChildCount(); i < N; ++i) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable("PercentLayout", 3)) {
                Log.d("PercentLayout", "should restore " + view + " " + params);
            }

            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable("PercentLayout", 3)) {
                    Log.d("PercentLayout", "using " + info);
                }

                if (info != null) {
                    if (params instanceof MarginLayoutParams) {
                        info.restoreMarginLayoutParams((MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }

    }

    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        int i = 0;

        for (int N = this.mHost.getChildCount(); i < N; ++i) {
            View view = this.mHost.getChildAt(i);
            LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable("PercentLayout", 3)) {
                Log.d("PercentLayout", "should handle measured state too small " + view + " " + params);
            }

            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = -2;
                    }

                    if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = -2;
                    }
                }
            }
        }

        if (Log.isLoggable("PercentLayout", 3)) {
            Log.d("PercentLayout", "should trigger second measure pass: " + needsSecondMeasure);
        }

        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view) & -16777216;
        return state == 16777216 && info.widthPercent >= 0.0F && info.mPreservedParams.width == -2;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredHeightAndState(view) & -16777216;
        return state == 16777216 && info.heightPercent >= 0.0F && info.mPreservedParams.height == -2;
    }

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    public static class PercentLayoutInfo {
        public float widthPercent = -1.0F;
        public float heightPercent = -1.0F;
        public float leftMarginPercent = -1.0F;
        public float topMarginPercent = -1.0F;
        public float rightMarginPercent = -1.0F;
        public float bottomMarginPercent = -1.0F;
        public float startMarginPercent = -1.0F;
        public float endMarginPercent = -1.0F;
        final MarginLayoutParams mPreservedParams = new MarginLayoutParams(0, 0);

        public PercentLayoutInfo() {
        }

        public void fillLayoutParams(LayoutParams params, int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            if (this.widthPercent >= 0.0F) {
                params.width = (int) ((float) screenSize.x * this.widthPercent);
            }

            if (this.heightPercent >= 0.0F) {
                params.height = (int) ((float) screenSize.y * this.heightPercent);
            }

            if (Log.isLoggable("PercentLayout", 3)) {
                Log.d("PercentLayout", "after fillLayoutParams: (" + params.width + ", " + params.height + ")");
            }

        }

        public void fillMarginLayoutParams(MarginLayoutParams params, int widthHint, int heightHint) {
            this.fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent >= 0.0F) {
                params.leftMargin = (int) ((float) screenSize.x * this.leftMarginPercent);
            }

            if (this.topMarginPercent >= 0.0F) {
                params.topMargin = (int) ((float) screenSize.y * this.topMarginPercent);
            }

            if (this.rightMarginPercent >= 0.0F) {
                params.rightMargin = (int) ((float) screenSize.x * this.rightMarginPercent);
            }

            if (this.bottomMarginPercent >= 0.0F) {
                params.bottomMargin = (int) ((float) screenSize.y * this.bottomMarginPercent);
            }

            if (this.startMarginPercent >= 0.0F) {
                MarginLayoutParamsCompat.setMarginStart(params, (int) ((float) screenSize.x * this.startMarginPercent));
            }

            if (this.endMarginPercent >= 0.0F) {
                MarginLayoutParamsCompat.setMarginEnd(params, (int) ((float) screenSize.x * this.endMarginPercent));
            }

            if (Log.isLoggable("PercentLayout", 3)) {
                Log.d("PercentLayout", "after fillMarginLayoutParams: (" + params.width + ", " + params.height + ")");
            }

        }

        public String toString() {
            return String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)", new Object[]{Float.valueOf(this.widthPercent), Float.valueOf(this.heightPercent), Float.valueOf(this.leftMarginPercent), Float.valueOf(this.topMarginPercent), Float.valueOf(this.rightMarginPercent), Float.valueOf(this.bottomMarginPercent), Float.valueOf(this.startMarginPercent), Float.valueOf(this.endMarginPercent)});
        }

        public void restoreMarginLayoutParams(MarginLayoutParams params) {
            this.restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(LayoutParams params) {
            params.width = this.mPreservedParams.width;
            params.height = this.mPreservedParams.height;
        }
    }
}
