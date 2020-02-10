import android.text.TextUtils;
import android.util.Log;
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
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import static com.google.android.gms.fitness.data.Device.TYPE_WATCH;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;

public class SleepBuilder {

    public void BuildSleepData() {
        Session session = new Session.Builder()
                .setName(sessionName)
                .setIdentifier(identifier)
                .setDescription(description)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .setActivity(FitnessActivities.SLEEP)
                .build();

// Build the request to insert the session.
        SessionInsertRequest request = new SessionInsertRequest.Builder()
                .setSession(session)
                .build();

// Insert the session into Fit platform
        Log.i(TAG, "Inserting the session in the Sessions API");
        Fitness.getSessionsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .insertSession(request)
                .addOnSuccessListener(
                        // At this point, the session has been inserted and can be read.
                        unused -> Log.i(TAG, "Session insert was successful!"))
                .addOnFailureListener(
                        e ->
                                Log.i(TAG, "There was a problem inserting the session: " +
                                        e.getLocalizedMessage()));
    }

}
