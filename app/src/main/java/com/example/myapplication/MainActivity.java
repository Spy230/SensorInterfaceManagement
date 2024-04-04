package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private LinearLayout buttons;
    private ImageView Buttonclear, Buttonpalette;
    private boolean buttonsVisible = false;
    private ArtView art;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime = 0;
    private static final int SHAKE_THRESHOLD = 1000; // Порог встряхивания в миллисекундах

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = findViewById(R.id.buttons);
        Buttonclear = findViewById(R.id.Buttonclear);
        Buttonpalette = findViewById(R.id.Buttonpalette);
        art = findViewById(R.id.art);

        // Инициализация датчика акселерометра
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Buttonclear.setOnClickListener(listener);
        Buttonpalette.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.Buttonclear) {
                AlertDialog.Builder broonDialog = new AlertDialog.Builder(MainActivity.this);
                broonDialog.setTitle("Очистка рисунка");
                broonDialog.setMessage("Очистить область рисования?");
                broonDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        art.clear();
                        dialog.dismiss();
                    }
                });
                broonDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                broonDialog.show();
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastShakeTime) > SHAKE_THRESHOLD) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                if (acceleration > 2) {
                    toggleButtonsVisibility();
                    lastShakeTime = currentTime;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void toggleButtonsVisibility() {
        if (buttonsVisible) {
            buttons.setVisibility(View.INVISIBLE);
            buttonsVisible = false;
        } else {
            buttons.setVisibility(View.VISIBLE);
            buttonsVisible = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}