package com.shariar99.remote;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String POWER_ON = "power_on";
    private final String POWER_OFF = "power_off";
    private final String VOLUME_UP = "volume_up";
    private final String VOLUME_DOWN = "volume_down";

    private final String[] irSignals = {POWER_ON, POWER_OFF, VOLUME_UP, VOLUME_DOWN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendIrSignal(View view) {
        String signal = null;
        switch (view.getId()) {
            case R.id.button_power:
                signal = POWER_ON;
                break;
            case R.id.button_volume_up:
                signal = VOLUME_UP;
                break;
            case R.id.button_volume_down:
                signal = VOLUME_DOWN;
                break;
        }
        if (signal != null) {
            sendIr(signal);
        }
    }

    private void sendIr(String signal) {
        try {
            ConsumerIrManager irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
            if (!irManager.hasIrEmitter()) {
                Toast.makeText(this, "This device does not have an IR emitter!", Toast.LENGTH_SHORT).show();
                return;
            }

            int frequency = 38000; // The frequency at which most devices expect IR signals
            int[] pattern;

            switch (signal) {
                case POWER_ON:
                    pattern = new int[]{9000, 4500, 600, 550, 600, 1650};
                    break;
                case POWER_OFF:
                    pattern = new int[]{9000, 4500, 600, 550, 600, 550};
                    break;
                case VOLUME_UP:
                    pattern = new int[]{9000, 4500, 600, 1600, 650, 1600};
                    break;
                case VOLUME_DOWN:
                    pattern = new int[]{9000, 4500, 600, 550, 600, 550};
                    break;
                default:
                    pattern = new int[0];
            }

            irManager.transmit(frequency, pattern);
        } catch (UnsupportedOperationException e) {
            Toast.makeText(this, "IR transmission is not supported on this device!", Toast.LENGTH_SHORT).show();
        }
    }
}