package com.github.pires.obd.reader.Record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.github.pires.obd.reader.R;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.contract.State;

public class RecordActivity extends AppCompatActivity implements RecordContract.View,View.OnClickListener,GestureDetector.OnGestureListener{


    private RecordPresenter presenter;
    private ScrollCalendar scrollCalendar;
    private ImageView back,moveToday;
    private TextView date;
    private ImageView point[] = new ImageView[2];
    private ViewFlipper viewFlipper;
    private GestureDetector gestureDetector;

    private final int SWIPE_MIN_DISTANCE = 120;
    private final int SWIPE_THRESHOLD_VELOCITY = 200;
    private TextView cool,vol,cool_state,vol_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        scrollCalendar = findViewById(R.id.scrollCalendar);

        back = findViewById(R.id.record_back);
        moveToday = findViewById(R.id.record_move_today);

        date = findViewById(R.id.record_date);

        cool = findViewById(R.id.record_cool_tem);
        vol = findViewById(R.id.record_voltage);

        cool_state = findViewById(R.id.record_cool_state);
        vol_state = findViewById(R.id.record_voltage_state);

        point[0] = findViewById(R.id.record_point1);
        point[1] = findViewById(R.id.record_point2);

        viewFlipper = findViewById(R.id.record_viewflipper);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        gestureDetector = new GestureDetector(this,this);

        back.setOnClickListener(this);
        moveToday.setOnClickListener(this);

        presenter = new RecordPresenter();
        presenter.attachView(this);
        presenter.initialize(this);



        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {
                presenter.onCalendarItemClick(getApplicationContext(),year, month, day);
            }
        });

        scrollCalendar.setDateWatcher(new DateWatcher() {
            @State
            @Override
            public int getStateForDate(int year, int month, int day) {
                return presenter.doGetStateForDate(year, month, day);
            }
        });

        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                return false;
            }

            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                return presenter.doShouldAddPreviousMonth(firstDisplayedYear, firstDisplayedMonth);
            }
        });

    }


    @Override
    public void refreshCalendar() {
        scrollCalendar.refresh();
        scrollCalendar.getRecyclerview().scrollToPosition(scrollCalendar.getRecyclerview().getAdapter().getItemCount()-1);
    }

    @Override
    public void setDate(String date) {
        this.date.setText(date);
    }

    @Override
    public void setCoolTem(String data) {

        this.cool.setText(data+"C");

        if(Integer.parseInt(data) > 90){
            cool_state.setText("점검 필요");
        }
        else if(Integer.parseInt(data) == 0){
            cool_state.setText("이력 없음");
        }
        else{
            cool_state.setText("정상 입니다.");
        }

    }

    @Override
    public void setVoltage(String voltage) {

        this.vol.setText(voltage+"V");

        if(Double.parseDouble(voltage) == 0.0){
            vol_state.setText("이력 없음");
        }
        else if(Double.parseDouble(voltage) < 11.5){
            vol_state.setText("점검 필요");
        }
        else{
            vol_state.setText("정상 입니다.");
        }

    }


    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.record_back :
                presenter.finish(this);
                break;

            case R.id.record_move_today :
                presenter.moveToday(this);
                break;

        }
    }


    @Override
    public void setPreviousHolderView(int index) {

        for(int i=0;i<point.length;i++)
            point[i].setBackground(getResources().getDrawable(R.drawable.gray_fill_round));

        point[index+1].setBackground(getResources().getDrawable(R.drawable.white_fill_round));

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
        viewFlipper.showPrevious();
    }

    @Override
    public void setNextHolderView(int index) {
        for(int i=0;i<point.length;i++)
            point[i].setBackground(getResources().getDrawable(R.drawable.gray_fill_round));

        point[index-1].setBackground(getResources().getDrawable(R.drawable.white_fill_round));

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
        viewFlipper.showNext();
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            presenter.setNextHolder(this);

        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            presenter.setPreviousHolder(this);


        return false;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
}
