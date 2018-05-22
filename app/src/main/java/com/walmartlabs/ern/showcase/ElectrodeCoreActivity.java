package com.walmartlabs.ern.showcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.ErnShowcaseNavigationApi.ern.model.NavBar;
import com.ErnShowcaseNavigationApi.ern.model.NavBarButton;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.walmartlabs.ern.container.ElectrodeReactActivityDelegate;
import com.walmartlabs.ern.container.ElectrodeReactContainer;
import com.walmartlabs.ern.showcase.menu.ActionBarMenu;

import java.util.List;

import static com.walmartlabs.ern.showcase.api.NavigationApiImpl.INITIAL_PROPS;

public abstract class ElectrodeCoreActivity extends AppCompatActivity implements ElectrodeReactActivityDelegate.BackKeyHandler, PermissionAwareActivity {
    private static final String TAG = ElectrodeCoreActivity.class.getCanonicalName();

    private ElectrodeReactActivityDelegate mReactActivityDelegate;

    /**
     * Method that helps to pass bundle to react native side.
     *
     * @param intent Intent that will start the activity
     * @param bundle Bundle that you would like to pass to react native.
     * @deprecated use
     */
    public static void addInitialProps(@NonNull Intent intent, @NonNull Bundle bundle) {
        intent.putExtra(INITIAL_PROPS, bundle);
    }

    abstract String getMiniAppName();
    abstract ErnRoute getNavigationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle routeBundle = getIntent().getBundleExtra(INITIAL_PROPS);
        ErnRoute incomingRoute = routeBundle != null ? new ErnRoute(routeBundle) : null;

        mReactActivityDelegate = new ElectrodeReactActivityDelegate(this);
        mReactActivityDelegate.setBackKeyHandler(this);

        Bundle bundle = new Bundle();
        if(incomingRoute != null) {
            bundle.putString("payload", getNavigationContext().getPayload());
        }

        View reactRootView = mReactActivityDelegate.createMiniAppRootView(getMiniAppName(), bundle);

        if (reactRootView != null) {
            setContentView(reactRootView);
        }
    }

    protected String getComponentName(String path) {
        String componentName = null;

        if (path != null && path.length() != 0) {
            boolean pathPrefix = path.indexOf("ern/") == 0;
            boolean pathPostfix = path.endsWith("/");

            componentName = pathPrefix ? path.substring("ern/".length()) : path;
            componentName = pathPostfix ? componentName.substring(0, componentName.length() - 1) : componentName;
        }

        return componentName;
    }

    protected String getComponentName(Uri data) {
        String componentName = null;

        String path = data.getPath();

        if (path != null && path.length() != 0) {
            boolean pathPrefix = path.indexOf("/ern/") == 0;
            boolean pathPostfix = path.endsWith("/");

            componentName = pathPrefix ? path.substring("/ern/".length()) : path;
            componentName = pathPostfix ? componentName.substring(0, componentName.length() - 1) : componentName;
        }

        return componentName;
    }

    private void setupMenu(Menu menu) {
        if (getNavigationContext() != null && getNavigationContext().getNavBar() != null) {
            NavBar navBar = getNavigationContext().getNavBar();

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(navBar.getTitle());

            NavBarButton leftButton = navBar.getLeftButton();
            if (leftButton != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            List<NavBarButton> rightButtons = navBar.getRightButtons();
            if (rightButtons != null) {
                menu.clear();

                int menuId = 0;
                int order = 0;

                for (NavBarButton navBarButton : rightButtons) {
                    MenuItem menuItem = menu.add(Menu.NONE, menuId++, order++, navBarButton.getName());
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                    menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return true;
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactActivityDelegate.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactActivityDelegate.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactActivityDelegate.onDestroy();
    }

    @Override
    @CallSuper
    public boolean onCreateOptionsMenu(final Menu menu) {
        ActionBarMenu.getInstance().onCreateMenu(menu);

        setupMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mReactActivityDelegate.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mReactActivityDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final boolean isMenuKey = (keyCode == KeyEvent.KEYCODE_MENU);

        if (isMenuKey
                && ElectrodeReactContainer.isReactNativeDeveloperSupport()
                && mReactActivityDelegate.canShowDeveloperMenu()) {
            mReactActivityDelegate.showDeveloperMenu();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackKey() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mReactActivityDelegate.requestPermissions(permissions, requestCode, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mReactActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
