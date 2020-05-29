package com.iterable.iterableapi;

import android.os.AsyncTask;

class IterableNotificationManager extends AsyncTask<IterableNotificationBuilder, Void, Void> {

    @Override
    protected Void doInBackground(IterableNotificationBuilder... params) {
        if (params != null && params[0] != null) {
            IterableNotificationBuilder notificationBuilder = params[0];
            IterableNotificationHelper.postNotificationOnDevice(notificationBuilder.context, notificationBuilder);
        }
        return null;
    }
}
