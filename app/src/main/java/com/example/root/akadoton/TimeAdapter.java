package com.example.root.akadoton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.RecyclerItemViewHolder> {

    private List<TimeData> timeData;
    int lastPosition = 0;
    RemoveClickListener listener;
    public boolean isTouched = false;

    public TimeAdapter(ArrayList<TimeData> timeData) {
        this.timeData = timeData;
    }

    @NonNull
    @Override
    public RecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time, parent, false);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
        return holder;
    }

    @Override public void onBindViewHolder(RecyclerItemViewHolder holder, final int position) {
        holder.TimeTextView.setText(timeData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return(null != timeData?timeData.size():0);
    }

    public void notifyData(ArrayList<TimeData> timeData) {
        this.timeData = timeData;
        notifyDataSetChanged();
    }



    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView TimeTextView;
        private final SwitchCompat recyclerSwitch;

        public RecyclerItemViewHolder(View parent) {
            super(parent);
            TimeTextView = parent.findViewById(R.id.text_time);
            recyclerSwitch = parent.findViewById(R.id.switch1);
            //Toast.makeText(itemView.getContext(), "IsChecked", Toast.LENGTH_SHORT).show();
            recyclerSwitch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isTouched = true;
                    return false;
                }
            });
            recyclerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isTouched) {
                        isTouched = false;

                        if (isChecked) {

                            //Toast.makeText(itemView.getContext(), "IsChecked", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(itemView.getContext(), "IsUnChecked", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }

    }

    public interface RemoveClickListener {
        void onRemoveClick(int index);
    }
}
