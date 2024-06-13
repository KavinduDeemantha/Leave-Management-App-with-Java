package com.s22010068.leave_management_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

public class LightSensorManager implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private LightSensorCallback callback;
    private Context context;

    public interface LightSensorCallback {
        void onLightSensorChanged(float lux);
    }

    public LightSensorManager(Context context, LightSensorCallback callback) {
        this.context = context;
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    public void start() {
        if (sensorManager != null && lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            final float lux = event.values[0];
            new Handler(Looper.getMainLooper()).post(() -> callback.onLightSensorChanged(lux));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
