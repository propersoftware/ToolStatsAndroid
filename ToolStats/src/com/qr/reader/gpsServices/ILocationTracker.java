package com.qr.reader.gpsServices;

import android.location.Location;

public interface ILocationTracker {
    public interface ILocationUpdateListener{
        public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime);
    }

    public void start();
    public void start(ILocationUpdateListener update);

    public void stop();

    public boolean hasLocation();

    public boolean hasPossiblyStaleLocation();

    public Location getLocation();

    public Location getPossiblyStaleLocation();

}