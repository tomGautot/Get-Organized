package be.patricegautot.getorganized.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.objects.IconCreditsAdapter;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rv = findViewById(R.id.icon_credit_list);
        rv.setAdapter(new IconCreditsAdapter(this));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
