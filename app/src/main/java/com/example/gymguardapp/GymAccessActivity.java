package com.example.gymguardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class GymAccessActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;

    private TextView sensorDataDisplay;
    private Button reAuthButton;

    private float[] accelerometerData;
    private float[] gyroscopeData;

    private Interpreter tfliteInterpreter;
    private boolean isAuthenticated;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_access);

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (accelerometerSensor == null || gyroscopeSensor == null) {
            System.out.println("Sensors not available on this device");
            // Handle the situation when sensors are not available
        }

        // Initialize UI components
        sensorDataDisplay = findViewById(R.id.sensorDataDisplay);
        reAuthButton = findViewById(R.id.reAuthButton);

        // Initialize shared preferences for authentication status
        sharedPreferences = getSharedPreferences("GymAccessPrefs", Context.MODE_PRIVATE);

        // Load the TensorFlow Lite model from assets
        try {
            tfliteInterpreter = new Interpreter(loadModelFile());
            if (tfliteInterpreter == null) {
                // Handle the situation where model loading failed
                System.out.println("Model loading failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Model file error: " + e.getMessage());
        }


        // Set up a click listener for the Re-authentication button
        reAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performReAuthentication();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listeners when the activity is resumed
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners when the activity is paused
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Handle sensor data updates and display them
        if (sensorEvent.values == null) {
            System.out.println("sensorEvent.values is null");
            // Handle the situation when sensors are not available
        }
        if (sensorEvent.sensor == accelerometerSensor) {
            accelerometerData = sensorEvent.values;
            updateSensorDataDisplay();
        } else if (sensorEvent.sensor == gyroscopeSensor) {
            gyroscopeData = sensorEvent.values;
            updateSensorDataDisplay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Handle accuracy changes if needed
    }

    private void updateSensorDataDisplay() {
        if (accelerometerData != null && gyroscopeData != null) {
            // Update the sensor data display TextView with accelerometer and gyroscope data
            String sensorData = "Accelerometer Data:\nX: " + accelerometerData[0] + "\nY: " + accelerometerData[1] + "\nZ: " + accelerometerData[2];
            sensorData += "\nGyroscope Data:\nX: " + gyroscopeData[0] + "\nY: " + gyroscopeData[1] + "\nZ: " + gyroscopeData[2];
            sensorDataDisplay.setText(sensorData);
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetManager assetManager = getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd("trained_lstm_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void performReAuthentication() {
        // Implement your re-authentication logic using the loaded TensorFlow Lite model
        float[][] sensorData = {accelerometerData, gyroscopeData};
        float result = runInference(sensorData);

        // Define your authentication threshold
        float authenticationThreshold = 0.8f;

        if (result >= authenticationThreshold) {
            // Authentication successful
            isAuthenticated = true;
            sharedPreferences.edit().putBoolean("isAuthenticated", true).apply();
            // Continue with gym access or other actions
        } else {
            // Authentication failed
            isAuthenticated = false;
            sharedPreferences.edit().putBoolean("isAuthenticated", false).apply();
            // Display a message or take appropriate action for failed authentication
        }
    }

    private float runInference(float[][] input) {
        if (tfliteInterpreter == null) {
            // Handle the situation where the interpreter is null
            System.out.println("Model interpreter is null");
            return -1.0f; // Return an error value or handle it accordingly
        }

        float[][] output = new float[1][1];
        tfliteInterpreter.run(input, output);
        return output[0][0];
    }
}