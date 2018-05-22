package com.walmartlabs.ern.showcase.menu;

import android.view.Menu;

public class ActionBarMenu {
    public static final String TAG = ActionBarMenu.class.getCanonicalName();

    private static volatile ActionBarMenu sInstance;

    private Menu mMenu;

    private ActionBarMenu() {
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    @SuppressWarnings("unused")
    protected ActionBarMenu readResolve() {
        return getInstance(); /* Make singleton safe from serialize and deserialize operation. */
    }

    public boolean onCreateMenu(Menu menu) {
        mMenu = menu;

        return true;
    }

    private static ActionBarMenu checkMenuInstance(ActionBarMenu instance) {
        if (instance == null) {
            throw new RuntimeException("ActionBarMenu: checkMenuInstance(): onCreateMenu() should be called first before calling getMenu().");
        }

        return instance;
    }

    public static ActionBarMenu getInstance() {
        if (sInstance == null) {
            synchronized (ActionBarMenu.class) {
                if (sInstance == null) sInstance = new ActionBarMenu();
            }
        }

        return sInstance;
    }

    public static Menu getMenu() {
        return checkMenuInstance(sInstance).mMenu;
    }
}
