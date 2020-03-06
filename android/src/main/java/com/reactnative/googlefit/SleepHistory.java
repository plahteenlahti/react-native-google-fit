/**
 * Copyright (c) 2020-present, Perttu Lähteenlahti perttu@lahteenlahti.com
 * All rights reserved.
 *
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 **/
package com.reactnative.googlefit;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.google.android.gms.fitness.data.Device.TYPE_WATCH;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;

public class SleepHistory {

    private ReactContext mReactContext;
    private GoogleFitManager googleFitManager;
    
    private static final String TAG = "RNGoogleFit";

    public SleepHistory(ReactContext reactContext, GoogleFitManager googleFitManager) {
        this.mReactContext = reactContext;
        this.googleFitManager = googleFitManager;
    }

    public ReadableArray getSleepSamples(long startTime, long endTime) {
        WritableArray results = Arguments.createArray();

        SessionReadRequest.Builder sessionBuilder = new SessionReadRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS).read(DataType.TYPE_ACTIVITY_SEGMENT)
                .readSessionsFromAllApps().enableServerQueries();

        SessionReadRequest readRequest = sessionBuilder.build();

        SessionReadResult sessionReadResult = Fitness.SessionsApi
                .readSession(googleFitManager.getGoogleApiClient(), readRequest).await(120, TimeUnit.SECONDS);

        List<Session> sessions = sessionReadResult.getSessions();
        for (Session session : sessions) {

           if(session.getActivity().equals(FitnessActivities.SLEEP)) {

            long start = session.getStartTime(TimeUnit.MILLISECONDS);
            long end = session.getEndTime(TimeUnit.MILLISECONDS);

            WritableMap map = Arguments.createMap();
            map.putDouble("start", start);
            map.putDouble("end", end);
            map.putString("name", session.getName());
            map.putString("description", session.getDescription());
            map.putString("sourceId", session.getAppPackageName());
            results.pushMap(map);
           }
        }

        return results;
    }

}
