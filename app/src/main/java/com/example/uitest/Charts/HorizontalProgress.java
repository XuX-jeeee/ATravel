package com.example.uitest.Charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uitest.R;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import com.example.uitest.object.User;

public class HorizontalProgress extends Fragment {
    private static HorizontalProgressView close_eyes;
    private static HorizontalProgressView yawn;
    private static HorizontalProgressView call_up;
    private static HorizontalProgressView smoke;
    private static HorizontalProgressView play_telephone;
    private static HorizontalProgressView other;

    private static TextView close_eyes_times;
    private static TextView yawn_times;
    private static TextView call_up_times;
    private static TextView smoke_times;
    private static TextView play_telephone_times;
    private static TextView other_times;


    private void init(View view){

        yawn= view.findViewById(R.id.yawn_progress);
        close_eyes= view.findViewById(R.id.close_eyes_progress);
        call_up= view.findViewById(R.id.call_up_progress);
        smoke= view.findViewById(R.id.smoke_progress);
        play_telephone= view.findViewById(R.id.play_telephone_progress);
        other= view.findViewById(R.id.other_progress);

        yawn_times=view.findViewById(R.id.yawn_times);
        close_eyes_times= view.findViewById(R.id.close_eyes_times);
        call_up_times= view.findViewById(R.id.call_up_times);
        smoke_times= view.findViewById(R.id.smoke_times);
        play_telephone_times= view.findViewById(R.id.play_telephone_times);
        other_times= view.findViewById(R.id.other_times);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.horizontal_progress_view_ground, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }
}
