package tw.org.iii.myservice1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PlayService extends Service {
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
