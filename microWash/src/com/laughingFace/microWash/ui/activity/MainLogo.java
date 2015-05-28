package com.laughingFace.microWash.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.laughingFace.microWash.R;
import com.laughingFace.microWash.ui.view.WaterRipplesView;

/**
 * Created by zihao on 15-5-27.
 */
public class MainLogo  implements WaterRipplesView.OnCollisionListener{

    private View contentView;
    private TextView dragModelName;
    private String dragingModel;//视觉上正被拖拽着的模式
    private WaterRipplesView checkArea;
    private WaterRipplesView model_standard;
    private WaterRipplesView model_dryoff;
    private WaterRipplesView model_timingwash;
    private WaterRipplesView model_sterilization;
    public MainLogo(View contentView){
        this.contentView = contentView;
        model_standard =  (WaterRipplesView)contentView.findViewById(R.id.model_standard);
        model_dryoff =  (WaterRipplesView)contentView.findViewById(R.id.model_dryoff);
        model_timingwash =  (WaterRipplesView)contentView.findViewById(R.id.model_timingwash);
        model_sterilization =  (WaterRipplesView)contentView.findViewById(R.id.model_sterilization);

        model_standard.setOnCollisionListener(this);
        model_dryoff.setOnCollisionListener(this);
        model_timingwash.setOnCollisionListener(this);
        model_sterilization.setOnCollisionListener(this);

        checkArea = (WaterRipplesView)contentView.findViewById(R.id.checkArea);
        checkArea.setOnCollisionListener(this);
        dragModelName = (TextView)contentView.findViewById(R.id.tv_mode);

    }


    @Override
    public void onLeave(View perpetrators, View wounder) {
        dragModelName.setText(dragingModel);
    }

    @Override
    public void onMove(View perpetrators, View wounder) {

    }

    @Override
    public void onEnter(View perpetrators, View wounder) {
        switch (wounder.getId()){
            case R.id.model_standard:
                Log.i("xixi", "----------- 标准模式 ----------------");
                dragingModel = "标准模式";
                break;
            case R.id.model_dryoff:
                Log.i("xixi", "----------- 烘干模式 ----------------");
                dragingModel = "烘干模式";
                break;
            case R.id.model_timingwash:
                Log.i("xixi", "----------- 定时清洗 ----------------");
                dragingModel = "定时清洗";
                break;
            case R.id.model_sterilization:
                Log.i("xixi", "----------- 杀菌模式 ----------------");
                dragingModel = "杀菌模式";
                break;
        }
        dragModelName.setText(dragingModel);
    }

    @Override
    public void onRealse(View perpetrators, View wounder) {
        if(null != wounder && wounder.getId() == R.id.checkArea){
            Log.i("xixi", "----------- 模式触发 ----------------");
        }
        else {
            dragModelName.setText("");
        }
    }
}
