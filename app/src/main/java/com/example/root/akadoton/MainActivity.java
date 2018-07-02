package com.example.root.akadoton;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.root.akadoton.application.AkadatonApplication;
import com.example.root.akadoton.network.UpdateDataBody;
import com.example.root.akadoton.network.UpdateDataResponse;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.regex.*;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    static Boolean isTouched = false;

    private TimeAdapter adapter;
    private RecyclerView recyclerView;
    private TextView sunStatusText;
    private TextView windowStatusText;
    private SwitchCompat windowSwitch;
    private SwitchCompat lightSwitch;

    AppCompatImageView sunStatus;
    AppCompatImageView windowStatus;

    @NonNull AppCompatButton setTimeBtn;

    String time = "";
    ArrayList<TimeData> timeData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.time_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TimeAdapter(timeData);
        recyclerView.setAdapter(adapter);

        windowSwitch = findViewById(R.id.switch_window);
        lightSwitch = findViewById(R.id.switch_light);

        windowSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        lightSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        windowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        setBlindsOn();
                    }
                    else {
                        setBlindsOff();
                    }
                }

            }
        });

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        setLightsOn();
                    }
                    else {
                        setLightOff();
                    }
                }
            }
        });

        setTimeBtn = findViewById(R.id.button_settime);
        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker(v);
            }
        });

        sunStatus = findViewById(R.id.sun_status);
        sunStatusText = findViewById(R.id.sun_status_text);
        windowStatus = findViewById(R.id.window_status);
        windowStatusText = findViewById(R.id.window_status_text);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean i = true;
                while(i) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf.format(c.getTime());

                    if (timeData.size()>(-1)) {
                        for (int z = 0; z < timeData.size(); z++) {
                            if ((timeData.get(z).getTime().equals(currentTime))) {
                                importantDecision();
                            }
                        }
                    }



                    ((AkadatonApplication) getApplication()).provideUpdateDataService()
                            .update(new UpdateDataBody("h", "q"))
                            .enqueue(new Callback<UpdateDataResponse>() {
                                @Override
                                public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                                    if (response.isSuccessful()) {
                                        UpdateDataResponse body = response.body();
                                        if (body.getLightsOut()!=null){
                                            switch (body.getLightsOut()) {
                                                case "1":
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Picasso.get().load(R.drawable.sun).resize(90, 90).into(sunStatus);
                                                            sunStatusText.setText("солнечно");
                                                        }
                                                    });
                                                    break;
                                                case "0":
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Picasso.get().load(R.drawable.clouds).resize(90, 90).into(sunStatus);
                                                            sunStatusText.setText("облачно");
                                                        }
                                                    });
                                                default:
                                                    Log.e("Backend Sucks", "guy on a backend is faggot");
                                                    break;
                                            }
                                            switch (body.getWindows()) {
                                                case "0":
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Picasso.get().load(R.drawable.closed).resize(90, 90).into(windowStatus);
                                                            windowStatusText.setText("закрыто");
                                                        }
                                                    });
                                                    break;
                                                case "1":
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Picasso.get().load(R.drawable.opened).resize(90, 90).into(windowStatus);
                                                            windowStatusText.setText("открыто");
                                                        }
                                                    });
                                                    break;
                                                default:
                                                    Log.e("Backend Sucks", "guy on a backend is faggot");
                                                    break; }
                                            }else {importantDecision();}

                                        }

                                    }




                                @Override
                                public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                                }
                            });

                }

            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void startPicker(View v) {
        new SingleDateAndTimePickerDialog.Builder(this)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .displayDays(false)
                .displayMonth(false)
                .displayYears(false)
                .bottomSheet()
                .minutesStep(1)
                .title("Выбрать время")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        String txt= date.toString();

                        String re1=".*?";
                        String re2="((?:(?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):(?:[0-5][0-9])(?::[0-5][0-9])?(?:\\s?(?:am|AM|pm|PM))?)";

                        Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        Matcher m = p.matcher(txt);
                        if (m.find())
                        {
                            String time1 = m.group(1);
                            Log.e("Test: ", time1);
                            TimeData time = new TimeData();
                            time.setTime(time1);
                            timeData.add(time);
                            adapter.notifyData(timeData);

                        }
                    }
                }).display();
    }

    public void importantDecision() {
        ((AkadatonApplication) getApplication()).provideUpdateDataService()
                .update(new UpdateDataBody("q", "h"))
                .enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                        if (response.isSuccessful()) {
                            UpdateDataResponse body = response.body();
                            switch (body.getWindows()) {
                                case "1":
                                    setBlindsOff();
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "0":
                                    setBlindsOn();
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    Log.e("Backend Sucks", "guy on a backend is faggot");
                                    break;
                            }

                        }


                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                    }
                });
    }

    public void setBlindsOff() {
        Log.e("Window", "OFF");
        ((AkadatonApplication) getApplication()).provideUpdateDataService()
                .update(new UpdateDataBody("q", "0"))
                .enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                    }
                });
    }

    public void setBlindsOn() {
        Log.e("Window", "ON");
        ((AkadatonApplication) getApplication()).provideUpdateDataService()
                .update(new UpdateDataBody("q", "1"))
                .enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                        if (response.isSuccessful()) {
                            UpdateDataResponse body = response.body();
                        }

                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                    }
                });
    }

    public void setLightsOn() {
        Log.e("Lights", "ON");
        ((AkadatonApplication) getApplication()).provideUpdateDataService()
                .update(new UpdateDataBody("1", "q"))
                .enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {
                        if (response.isSuccessful()) {
                            UpdateDataResponse body = response.body();
                        }

                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                    }
                });
    }

    public void setLightOff() {
        Log.e("Lights", "OFF");
        ((AkadatonApplication) getApplication()).provideUpdateDataService()
                .update(new UpdateDataBody("0", "q"))
                .enqueue(new Callback<UpdateDataResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDataResponse> call, Response<UpdateDataResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<UpdateDataResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_window:

                break;
            case R.id.switch_light:
                break;


        }
    }
}
