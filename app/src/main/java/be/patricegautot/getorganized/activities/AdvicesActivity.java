package be.patricegautot.getorganized.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.patricegautot.getorganized.R;

public class AdvicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
