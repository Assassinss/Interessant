package me.zsj.interessant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Locale;

import me.zsj.interessant.utils.PreferenceManager;

/**
 * @author zsj
 */

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    public static final String CACHE_KEY = "cache_with_wifi";

    private Activity context;

    private Preference clearVideoCache;
    private Preference clearPhotoCache;
    private SwitchPreference cacheWithWifi;

    private File cacheVideoFile;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        cacheWithWifi = (SwitchPreference) findPreference("cache_with_wifi");
        cacheWithWifi.setOnPreferenceChangeListener(this);
        boolean checked = PreferenceManager.getBooleanValue(context, CACHE_KEY, true);
        cacheWithWifi.setChecked(checked);

        clearVideoCache  = findPreference("clear_video_cache");
        clearVideoCache.setOnPreferenceClickListener(this);

        float videoFileSize = calculateCacheFileSize("video-cache", true);

        clearVideoCache.setSummary(String.format(Locale.CHINESE, "%.2fMb", videoFileSize));

        clearPhotoCache = findPreference("clear_photo_cache");
        clearPhotoCache.setOnPreferenceClickListener(this);

        float photoFileSize = calculateCacheFileSize("image_manager_disk_cache", false);
        photoFileSize += calculateCacheFileSize("picasso-cache", false);

        clearPhotoCache.setSummary(String.format(Locale.CHINESE, "%.2fMb", photoFileSize));

    }

    private float calculateCacheFileSize(String cachePath, boolean videoPath) {
        final File cacheFile = videoPath ? context.getExternalCacheDir() : context.getCacheDir();
        File videoCacheFile = null;
        assert cacheFile != null;
        for (File file : cacheFile.listFiles()) {
            if (cachePath.equals(file.getName())) {
                videoCacheFile = file;
                break;
            }
        }

        if (videoCacheFile != null && videoPath) {
            this.cacheVideoFile = videoCacheFile;
        }

        float totalSize = 0;  //Mb
        if (videoCacheFile != null && videoCacheFile.isDirectory()) {
            for (File file : videoCacheFile.listFiles()) {
                totalSize += (float) file.length() / (float) (1024 * 1024);
            }
        }
        return totalSize;
    }

    private void clearImageCache(File cacheImageFile) {
        for (File file : cacheImageFile.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        if (cacheImageFile.listFiles().length == 0) {
            clearPhotoCache.setSummary("0.00Mb");
        }
    }

    private void deleteCacheFiles(File cacheFile, boolean videoPath) {
        if (cacheFile != null && cacheFile.isDirectory()) {
            for (File file : cacheFile.listFiles()) {
                if (file.isDirectory()) {
                    clearImageCache(file);
                } else if (file.isFile()) {
                    file.delete();
                }
            }
            if (videoPath && cacheFile.listFiles().length == 0) {
                clearVideoCache.setSummary("0.00Mb");
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("clear_video_cache".equals(preference.getKey())) {
            deleteCacheFiles(cacheVideoFile, true);
        } else if ("clear_photo_cache".equals(preference.getKey())) {
            deleteCacheFiles(context.getCacheDir(), false);
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean checked = (Boolean) newValue;
        cacheWithWifi.setChecked(checked);
        PreferenceManager.putBoolean(context, CACHE_KEY, checked);
        return false;
    }
}
