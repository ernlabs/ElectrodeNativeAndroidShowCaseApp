package com.walmartlabs.ern.showcase.api;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ErnShowcaseNavigationApi.ern.api.ShowcaseNavigationApi;
import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeRequestHandler;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeResponseListener;
import com.walmartlabs.electrode.reactnative.bridge.None;
import com.walmartlabs.ern.container.ElectrodeReactContainer;
import com.walmartlabs.ern.showcase.DeepLinkRouterActivity;
import com.walmartlabs.ern.showcase.ElectrodeComponentActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NavigationApiImpl {
    public static final String INITIAL_PROPS = "props";
    public static final String INITIAL_NAVBAR = "navbar";

    public static void registerNavigationApi() {
        ShowcaseNavigationApi.requests().registerNavigateRequestHandler(new ElectrodeBridgeRequestHandler<ErnRoute, None>() {
            @Override
            public void onRequest(@Nullable ErnRoute payload, @NonNull ElectrodeBridgeResponseListener<None> responseListener) {
                if (payload != null) {
                    Intent intent = new Intent(ElectrodeReactContainer.getCurrentActivity(), ElectrodeComponentActivity.class);
                    intent.putExtra(INITIAL_PROPS, payload.toBundle());

                    boolean pop = payload.getPopType() != null && (payload.getPopType().getPopToRoot() != null ? payload.getPopType().getPopToRoot() : false);

                    if (pop) {
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                    }

                    ElectrodeReactContainer.startActivitySafely(intent);
                }
            }
        });
    }

    public static void registerNavigationApi2() {
        ShowcaseNavigationApi.requests().registerNavigateRequestHandler(new ElectrodeBridgeRequestHandler<ErnRoute, None>() {
            @Override
            public void onRequest(@Nullable ErnRoute payload, @NonNull ElectrodeBridgeResponseListener<None> responseListener) {
                if (payload != null) {
                    String path = payload.getPath();
                    String uri = "https://ern.walmart.com/" + path;

                    Log.e("TAG", "--------------- Path: " + path);
                    Log.e("TAG", "--------------- URI: " + uri);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.putExtra(INITIAL_PROPS, payload.toBundle());

                    boolean pop = payload.getPopType() != null && (payload.getPopType().getPopToRoot() != null ? payload.getPopType().getPopToRoot() : false);

                    if (pop) {
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                    }

                    ElectrodeReactContainer.startActivitySafely(intent);
                }
            }
        });
    }
}
