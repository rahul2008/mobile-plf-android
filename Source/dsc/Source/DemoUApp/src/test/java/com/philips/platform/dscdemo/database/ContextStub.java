package com.philips.platform.dscdemo.database;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class ContextStub extends Context {

    public SharedPreferencesStub sharedPreferencesStub = new SharedPreferencesStub();

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(final int i) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(final String s, final int i) {
        return sharedPreferencesStub;
    }

    @Override
    public boolean moveSharedPreferencesFrom(final Context context, final String s) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(final String s) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(final String s) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(final String s, final int i) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(final String s) {
        return false;
    }

    @Override
    public File getFileStreamPath(final String s) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable final String s) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(final String s) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(final String s, final int i) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(final String s, final int i, final SQLiteDatabase.CursorFactory cursorFactory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(final String s, final int i, final SQLiteDatabase.CursorFactory cursorFactory, @Nullable final DatabaseErrorHandler databaseErrorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(final Context context, final String s) {
        return false;
    }

    @Override
    public boolean deleteDatabase(final String s) {
        return false;
    }

    @Override
    public File getDatabasePath(final String s) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(final Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(final InputStream inputStream) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(final Intent intent) {

    }

    @Override
    public void startActivity(final Intent intent, @Nullable final Bundle bundle) {

    }

    @Override
    public void startActivities(final Intent[] intents) {

    }

    @Override
    public void startActivities(final Intent[] intents, final Bundle bundle) {

    }

    @Override
    public void startIntentSender(final IntentSender intentSender, @Nullable final Intent intent, final int i, final int i1, final int i2) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(final IntentSender intentSender, @Nullable final Intent intent, final int i, final int i1, final int i2, @Nullable final Bundle bundle) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(final Intent intent) {

    }

    @Override
    public void sendBroadcast(final Intent intent, @Nullable final String s) {

    }

    @Override
    public void sendOrderedBroadcast(final Intent intent, @Nullable final String s) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull final Intent intent, @Nullable final String s, @Nullable final BroadcastReceiver broadcastReceiver, @Nullable final Handler handler, final int i, @Nullable final String s1, @Nullable final Bundle bundle) {

    }

    @Override
    public void sendBroadcastAsUser(final Intent intent, final UserHandle userHandle) {

    }

    @Override
    public void sendBroadcastAsUser(final Intent intent, final UserHandle userHandle, @Nullable final String s) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(final Intent intent, final UserHandle userHandle, @Nullable final String s, final BroadcastReceiver broadcastReceiver, @Nullable final Handler handler, final int i, @Nullable final String s1, @Nullable final Bundle bundle) {

    }

    @Override
    public void sendStickyBroadcast(final Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(final Intent intent, final BroadcastReceiver broadcastReceiver, @Nullable final Handler handler, final int i, @Nullable final String s, @Nullable final Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcast(final Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(final Intent intent, final UserHandle userHandle) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(final Intent intent, final UserHandle userHandle, final BroadcastReceiver broadcastReceiver, @Nullable final Handler handler, final int i, @Nullable final String s, @Nullable final Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcastAsUser(final Intent intent, final UserHandle userHandle) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter, final int i) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter, @Nullable final String s, @Nullable final Handler handler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter, @Nullable final String s, @Nullable final Handler handler, final int i) {
        return null;
    }

    @Override
    public void unregisterReceiver(final BroadcastReceiver broadcastReceiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(final Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(final Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(final Intent intent) {
        return false;
    }

    @Override
    public boolean bindService(final Intent intent, @NonNull final ServiceConnection serviceConnection, final int i) {
        return false;
    }

    @Override
    public void unbindService(@NonNull final ServiceConnection serviceConnection) {

    }

    @Override
    public boolean startInstrumentation(@NonNull final ComponentName componentName, @Nullable final String s, @Nullable final Bundle bundle) {
        return false;
    }

    @Nullable
    @Override
    public Object getSystemService(@NonNull final String s) {
        return null;
    }

    @Nullable
    @Override
    public String getSystemServiceName(@NonNull final Class<?> aClass) {
        return null;
    }

    @Override
    public int checkPermission(@NonNull final String s, final int i, final int i1) {
        return 0;
    }

    @Override
    public int checkCallingPermission(@NonNull final String s) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfPermission(@NonNull final String s) {
        return 0;
    }

    @Override
    public int checkSelfPermission(@NonNull final String s) {
        return 0;
    }

    @Override
    public void enforcePermission(@NonNull final String s, final int i, final int i1, @Nullable final String s1) {

    }

    @Override
    public void enforceCallingPermission(@NonNull final String s, @Nullable final String s1) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull final String s, @Nullable final String s1) {

    }

    @Override
    public void grantUriPermission(final String s, final Uri uri, final int i) {

    }

    @Override
    public void revokeUriPermission(final Uri uri, final int i) {

    }

    @Override
    public void revokeUriPermission(final String s, final Uri uri, final int i) {

    }

    @Override
    public int checkUriPermission(final Uri uri, final int i, final int i1, final int i2) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(final Uri uri, final int i) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(final Uri uri, final int i) {
        return 0;
    }

    @Override
    public int checkUriPermission(@Nullable final Uri uri, @Nullable final String s, @Nullable final String s1, final int i, final int i1, final int i2) {
        return 0;
    }

    @Override
    public void enforceUriPermission(final Uri uri, final int i, final int i1, final int i2, final String s) {

    }

    @Override
    public void enforceCallingUriPermission(final Uri uri, final int i, final String s) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(final Uri uri, final int i, final String s) {

    }

    @Override
    public void enforceUriPermission(@Nullable final Uri uri, @Nullable final String s, @Nullable final String s1, final int i, final int i1, final int i2, @Nullable final String s2) {

    }

    @Override
    public Context createPackageContext(final String s, final int i) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createContextForSplit(final String s) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(@NonNull final Configuration configuration) {
        return null;
    }

    @Override
    public Context createDisplayContext(@NonNull final Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }
}
