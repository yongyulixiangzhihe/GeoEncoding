package com.geoencoding;

import android.app.Activity;
import com.imagepicker.ImagePickerPackage;
import com.github.yamill.orientation.OrientationPackage;
import com.brentvatne.react.ReactVideoPackage;
import com.AirMaps.AirPackage;
import com.remobile.splashscreen.RCTSplashScreenPackage;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.res.Configuration;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import org.pgsqlite.SQLitePluginPackage;

import com.microsoft.codepush.react.CodePush;
import android.support.v4.app.FragmentActivity;

import com.rnfs.RNFSPackage;

import com.oblador.vectoricons.VectorIconsPackage;


public class MainActivity extends FragmentActivity implements DefaultHardwareBackBtnHandler {

  private ReactInstanceManager mReactInstanceManager;
  private ReactRootView mReactRootView;
  private ImagePickerPackage mImagePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CodePush codePush = new CodePush("9OcY7WuRIwcigP2x6H5z8bTZPLN94yXS9p7Fg", this, BuildConfig.DEBUG);

    mReactRootView = new ReactRootView(this);


      // instantiate package
      mImagePicker = new ImagePickerPackage(this);


    mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                // .setBundleAssetName("index.android.bundle")
                .setJSBundleFile(codePush.getBundleUrl("index.android.bundle"))
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .addPackage(mImagePicker)
                .addPackage(new OrientationPackage(this))
                .addPackage(new ReactVideoPackage())
                .addPackage(new AirPackage())
                .addPackage(new RCTSplashScreenPackage(this))
                .addPackage(new SQLitePluginPackage(this))   // register SQLite Plugin here
                .addPackage(new RNFSPackage())
                .addPackage(new VectorIconsPackage())
                .addPackage(codePush.getReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "GeoEncoding", null);

    setContentView(mReactRootView);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
      mReactInstanceManager.showDevOptionsDialog();
      return true;
    }
    return super.onKeyUp(keyCode, event);
  }

  @Override
  public void onBackPressed() {
    if (mReactInstanceManager != null) {
        mReactInstanceManager.onBackPressed();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void invokeDefaultOnBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (mReactInstanceManager != null) {
      mReactInstanceManager.onPause();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

      if (mReactInstanceManager != null) {
      mReactInstanceManager.onResume(this, this);
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Intent intent = new Intent("onConfigurationChanged");
    intent.putExtra("newConfig", newConfig);
    this.sendBroadcast(intent);
  }

  @Override
  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      mImagePicker.handleActivityResult(requestCode, resultCode, data);
  }
}
