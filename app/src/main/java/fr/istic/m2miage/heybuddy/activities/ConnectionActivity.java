package fr.istic.m2miage.heybuddy.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fr.istic.m2miage.heybuddy.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConnectionActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnConnection;
    private Button btnSubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection);
    }

    private void initComponent() {
        this.txtUsername = (EditText)this.findViewById(R.id.txt_id);
    }
}
