package com.example.frontend.SupportingClasses;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Class for an LruCache of Bitmaps indexed by their String names
 *
 * @author Dan Rosenhamer
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

    /**
     * Returns the default size of an LruCache
     *
     * @return size
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    /**
     * Constructs LruBitmapCache of default size
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * Constructs LruBitmapCache of given size
     *
     * @param sizeInKiloBytes size of LruBitmapCache to be constructed
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    /**
     * Returns the size of a given Bitmap
     *
     * @param key   name of the Bitmap
     * @param value given Bitmap
     * @return size of given Bitmap
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /**
     * Returns a Bitmap at a given url
     *
     * @param url given url
     * @return Bitmap at given url
     */
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    /**
     * Puts a Bitmap into the LruBitmapCache
     *
     * @param url    url of a Bitmap
     * @param bitmap Bitmap
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}

