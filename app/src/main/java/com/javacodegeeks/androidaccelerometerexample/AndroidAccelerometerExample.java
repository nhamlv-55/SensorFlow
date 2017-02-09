package com.javacodegeeks.androidaccelerometerexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

public class AndroidAccelerometerExample extends Activity {

    private static String TAG = "BlaBLa";
    private float lastX, lastY, lastZ;
    private float deltaX, deltaY, deltaZ;
    private SensorManager mSensorManager;
//    private Sensor accelerometer, proximity, gyro, rotVec, ori;
    private SensorEventListener mSensorListener;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
    private DataOutputStream dataOutputStream;
    private Socket socket;

    private EditText socketAddress;
    private Boolean socketCreated = false;

    private HashMap<Integer, String> sensorRecord = new HashMap<Integer, String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

//			finally{
//				if (socket != null){
//					try {
//						socket.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				if (dataOutputStream != null){
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				if (dataInputStream != null){
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}

            Log.d(TAG, "On create");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            mSensorListener = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor arg0, int arg1) {
                }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    Sensor sensor = event.sensor;
                    String v="";
                    sensorRecord.put(sensor.getType(), Arrays.toString(event.values));
                    Log.d(TAG, sensorRecord.toString());
                }
            };

            mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_GAME);

            socketAddress = (EditText) findViewById(R.id.socketAddress);
            Button socketButton = (Button) findViewById(R.id.socketButton);
            socketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = socketAddress.getText().toString();
                    Log.d(TAG, address);
                    try {
                        socket = new Socket(address, 8009);
                        socketCreated = true;
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });


        }


    }


    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_GAME);


    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }

}