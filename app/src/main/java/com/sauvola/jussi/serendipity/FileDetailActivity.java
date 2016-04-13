package com.sauvola.jussi.serendipity;

import static com.sauvola.jussi.serendipity.ProfileActivity.TAG_FILE_TITLE;
import static com.sauvola.jussi.serendipity.ProfileActivity.TAG_FILE_GPS;
import static com.sauvola.jussi.serendipity.ProfileActivity.TAG_FILE_DESCRIPTION;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FileDetailActivity extends AppCompatActivity {

    Button deleteButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mSettingButton = (Button) findViewById(R.id.setting_button);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.sauvola.jussi.serendipity.PersonalProfileActivity");
                startActivity(intent);
            }
        });

        Intent in = getIntent();

        String file_title = "";
        String file_gps = "";
        String file_description = "";

        deleteButton = (Button) findViewById(R.id.delete_btn);

        saveButton = (Button) findViewById(R.id.save_btn);

        if (null != in) {
            file_title = in.getStringExtra(TAG_FILE_TITLE);
            file_gps = in.getStringExtra(TAG_FILE_GPS);
            file_description = in.getStringExtra(TAG_FILE_DESCRIPTION);
        }

        TextView file_title_text = (TextView) findViewById(R.id.file_title_text);
        file_title_text.setText(file_title);

        TextView file_gps_text = (TextView) findViewById(R.id.file_gps_text);
        file_gps_text.setText(file_gps);

        TextView file_description_text = (TextView) findViewById(R.id.file_description_text);
        file_description_text.setText(file_description);

    }
}
