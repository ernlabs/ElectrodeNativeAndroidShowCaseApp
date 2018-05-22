package com.walmartlabs.ern.showcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;

import static com.walmartlabs.ern.showcase.api.NavigationApiImpl.INITIAL_PROPS;

public class DeepLinkRouterActivity extends ElectrodeCoreActivity {
    private String mAppName;
    private ErnRoute incomingRoute;

    @Override
    String getMiniAppName() {
        return mAppName;
    }

    @Override
    ErnRoute getNavigationContext() {
        return incomingRoute;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Bundle routeBundle = getIntent().getBundleExtra(INITIAL_PROPS);
//
//        incomingRoute = new ErnRoute(routeBundle);

        Uri data = getIntent().getData();

        assert data != null;

        mAppName = getComponentName(data);

        if (mAppName != null) {
            super.onCreate(savedInstanceState);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri data = intent.getData();

        assert data != null;

        mAppName = getComponentName(data);

        if (mAppName != null) {
            super.onNewIntent(intent);
        } else {
            finish();
        }
    }
}
