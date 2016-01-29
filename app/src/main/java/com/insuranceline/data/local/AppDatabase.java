package com.insuranceline.data.local;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by zeki on 17/01/2016.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "demo_db";

    public static final int VERSION = 1;
}
