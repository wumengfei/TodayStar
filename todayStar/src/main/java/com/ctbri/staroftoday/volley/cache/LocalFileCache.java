package com.ctbri.staroftoday.volley.cache;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
/**
 * @ description:本地图片缓存辅助类
 */
public class LocalFileCache extends BaseImageCache{	
	private Context mContext;
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	protected static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";
	private final Config mDecodeConfig;
	private final int mMaxWidth;
    private final int mMaxHeight;
    
	public LocalFileCache(Context context,Config decodeConfig,int mMaxWidth,int mMaxHeight) {
		isCache = false;
		priority = 100;
		mContext = context;
		mDecodeConfig = decodeConfig;
		this.mMaxWidth = mMaxWidth;
		this.mMaxHeight = mMaxHeight;
	}
	
	public LocalFileCache(Context context){
		this(context,null,0,0);
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap res = null;			
		String key = url.replaceFirst("#W[0-9]*#H[0-9]*", "");
		log(key);
		try {
			InputStream in = getStream(key);
			if(in!=null){
				log("其他本地加载成功");			
				res = InputStream2Bitmap(in);		
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		return res;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(isCache)
			log("do nothing!Sorry,you can't write file by this method!");
	}		
	
	/**
	 * 压缩图片
	 * @param is
	 * @return
	 */
	public Bitmap InputStream2Bitmap(InputStream is){
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;
		if (mMaxWidth == 0 && mMaxHeight == 0) {
            decodeOptions.inPreferredConfig = mDecodeConfig;
            bitmap = BitmapFactory.decodeStream(is,null,decodeOptions);
        } else {
            // If we have to resize this image, first get the natural bounds.
            decodeOptions.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(is,null,decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            // Then compute the dimensions we would ideally like to decode to.
            int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                    actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                    actualHeight, actualWidth);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;
            // TODO(ficus): Do we need this or is it okay since API 8 doesn't support it?
            // decodeOptions.inPreferQualityOverSpeed = PREFER_QUALITY_OVER_SPEED;
            decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap =
            		bitmap = BitmapFactory.decodeStream(is,null,decodeOptions);

            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
                    tempBitmap.getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap,
                        desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }
		return bitmap;
	}
	
	// Visible for testing.
    private int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }
    
    private int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
            int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
	
	public InputStream getStream(String imageUri) throws IOException {
		switch (Scheme.ofUri(imageUri)) {			
			case FILE:
				return getStreamFromFile(imageUri);
			case CONTENT:
				return getStreamFromContent(imageUri);
			case ASSETS:
				return getStreamFromAssets(imageUri);
			case DRAWABLE:
				return getStreamFromDrawable(imageUri);
			case UNKNOWN:
			default:
				return getStreamFromOtherSource(imageUri);
		}
	}

	protected InputStream getStreamFromFile(String imageUri) throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		if (isVideoFileUri(imageUri)) {
			return getVideoThumbnailStream(filePath);
		} else {
			BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
			return imageStream;
		}
	}
			
	@TargetApi(Build.VERSION_CODES.FROYO)
	private InputStream getVideoThumbnailStream(String filePath) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			Bitmap bitmap = ThumbnailUtils
					.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
			if (bitmap != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
		}
		return null;
	}
	
	private boolean isVideoFileUri(String uri) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		return mimeType != null && mimeType.startsWith("video/");
	}
		
	protected InputStream getStreamFromContent(String imageUri) throws FileNotFoundException {
		ContentResolver res = mContext.getContentResolver();

		Uri uri = Uri.parse(imageUri);
		if (isVideoContentUri(uri)) { // video thumbnail
			Long origId = Long.valueOf(uri.getLastPathSegment());
			Bitmap bitmap = MediaStore.Video.Thumbnails
					.getThumbnail(res, origId, MediaStore.Images.Thumbnails.MINI_KIND, null);
			if (bitmap != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
		} else if (imageUri.startsWith(CONTENT_CONTACTS_URI_PREFIX)) { // contacts photo
			return getContactPhotoStream(uri);
		}

		return res.openInputStream(uri);
	}
	
	private boolean isVideoContentUri(Uri uri) {
		String mimeType = mContext.getContentResolver().getType(uri);
		return mimeType != null && mimeType.startsWith("video/");
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected InputStream getContactPhotoStream(Uri uri) {
		ContentResolver res = mContext.getContentResolver();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true);
		} else {
			return ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
		}
	}
	
	protected InputStream getStreamFromAssets(String imageUri) throws IOException {
		String filePath = Scheme.ASSETS.crop(imageUri);
		return mContext.getAssets().open(filePath);
	}
	
	protected InputStream getStreamFromDrawable(String imageUri) {		
		String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
		int drawableId = Integer.parseInt(drawableIdString);
		return mContext.getResources().openRawResource(drawableId);
	}
	
	protected InputStream getStreamFromOtherSource(String imageUri){
		return null;
	}
	
	public boolean belongsTo(String url){
		return Scheme.ofUri(url)!=Scheme.UNKNOWN;
	}
	
	/** Represents supported schemes(protocols) of URI. Provides convenient methods for work with schemes and URIs. */
	public enum Scheme {
		FILE("file"),CONTENT("content"),ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");

		private String scheme;
		private String uriPrefix;

		Scheme(String scheme) {
			this.scheme = scheme;
			uriPrefix = scheme + "://";
		}

		/**
		 * Defines scheme of incoming URI
		 *
		 * @param uri URI for scheme detection
		 * @return Scheme of incoming URI
		 */
		public static Scheme ofUri(String uri) {
			if (uri != null) {
				for (Scheme s : values()) {
					if (s.belongsTo(uri)) {
						return s;
					}
				}
			}
			return UNKNOWN;
		}

		private boolean belongsTo(String uri) {
			return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
		}

		/** Appends scheme to incoming path */
		public String wrap(String path) {
			return uriPrefix + path;
		}

		/** Removed scheme part ("scheme://") from incoming URI */
		public String crop(String uri) {
			if (!belongsTo(uri)) {
				throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
			}
			return uri.substring(uriPrefix.length());
		}
	}

	@Override
	public void clear() {
		if(isCache)
		log("do nothing!Sorry,you can't delete file by this method!");
	}

}
