package com.laughingFace.microWash.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private MainLogo mainLogo;
    private static Activity instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("xixi", "DeviceActivity onCreate...");

        mainLogo = new MainLogo(findViewById(R.id.device_top_container));
        intent = new Intent(this,WorkingActivity.class);
        nextPage = (ImageButton)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        instance = this;
    }


    public static Activity getInstance(){
        return instance;
    }

}
