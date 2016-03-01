package com.cj.android.chooseimages.displayimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jian.cao on 2016/2/25.
 */
public class DefaultDisplayImage extends DisplayImage<ImageView> {
    private static LruCache<String, Bitmap> lruCache;
    private Context context;
    private float imageWidth;
    private static BitmapFactory.Options justDecodeOptions;
    private static ExecutorService executors;

    public DefaultDisplayImage(Context context) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        imageWidth = wm.getDefaultDisplay().getWidth() * 3 / 10;
        if (lruCache == null) {
            lruCache = new LruCache<String, Bitmap>(
                    (int) (Runtime.getRuntime().maxMemory() / 8)) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap
                        newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    if (oldValue != null && !oldValue.isRecycled()) {
                        oldValue.recycle();
                    }
                }
            };
        }
        if (justDecodeOptions == null) {
            justDecodeOptions = new BitmapFactory.Options();
            justDecodeOptions.inJustDecodeBounds = true;
        }
        if (executors == null) {
            executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        }
    }

    @Override
    public void display(ImageView view, String path) {
        Bitmap bitmap = lruCache.get(path);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        } else {
            view.setImageResource(android.R.drawable.stat_notify_error);
            view.setTag(path);
            executors.submit(new MyAsyncTask(new ImagePath(view, path)));
        }
    }

    @Override
    public void destroy() {
        lruCache.evictAll();
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            ImagePath imagePath = (ImagePath) msg.obj;
            if (imagePath.path.equals(imagePath.imageView.getTag())) {
                Bitmap bitmap = lruCache.get(imagePath.path);
                if (bitmap != null) {
                    imagePath.imageView.setImageBitmap(bitmap);
                }
            }
        }
    };

    class ImagePath {
        public ImageView imageView;
        public String path;

        public ImagePath(ImageView imageView, String path) {
            this.imageView = imageView;
            this.path = path;
        }
    }

    class MyAsyncTask implements Runnable {
        private ImagePath imagePath;

        public MyAsyncTask(ImagePath imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        public void run() {
            BitmapFactory.decodeFile(imagePath.path, justDecodeOptions);
            int scale;

            scale = (int) Math.min(justDecodeOptions.outWidth / imageWidth, justDecodeOptions.outHeight / imageWidth);
            if (scale < 1) {
                scale = 1;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scale;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(imagePath.path, options);
            } catch (OutOfMemoryError error) {
                lruCache.evictAll();
            }
            if (bitmap != null) {
                lruCache.put(imagePath.path, bitmap);
                Message message = Message.obtain();
                message.obj = imagePath;
                handler.sendMessage(message);
            }
        }
    }
}
