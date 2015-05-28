package com.laughingFace.microWash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.ui.view.WaterRipplesView;

public class DeviceActivity extends BaseActivity{
    private ImageButton nextPage;
    private Intent intent;
    MainLogo mainLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainLogo = new MainLogo(findViewById(R.id.main_logo));
        intent = new Intent(this,WorkingActivity.class);
        nextPage = (ImageButton)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }


}
