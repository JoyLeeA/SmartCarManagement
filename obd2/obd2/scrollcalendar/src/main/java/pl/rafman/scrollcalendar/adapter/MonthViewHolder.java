package pl.rafman.scrollcalendar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.rafman.scrollcalendar.R;
import pl.rafman.scrollcalendar.contract.ClickCallback;
import pl.rafman.scrollcalendar.data.CalendarDay;
import pl.rafman.scrollcalendar.data.CalendarMonth;
import pl.rafman.scrollcalendar.style.DayResProvider;
import pl.rafman.scrollcalendar.style.MonthResProvider;

/**
 * Created by rafal.manka on 10/09/2017
 */
class MonthViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private final TextView title;
    private MonthResProvider monthResProvider;

    private final WeekHolder[] weeks = new WeekHolder[7];
    private boolean textAllCaps;
    private Context context;


    private MonthViewHolder(@NonNull View rootView, @NonNull ClickCallback calendarCallback, @NonNull MonthResProvider monthResProvider, @NonNull DayResProvider dayResProvider) {
        super(rootView);
        this.monthResProvider = monthResProvider;
        LinearLayout monthContainer = rootView.findViewById(R.id.monthContainer);
        title = rootView.findViewById(R.id.title);
        setupTitleAppearance(monthResProvider);
        context = rootView.getContext();

        for (int i = 0; i < weeks.length; i++) {
            WeekHolder holder = new WeekHolder(calendarCallback, dayResProvider);
            weeks[i] = holder;
            monthContainer.addView(holder.layout(monthContainer));
        }
    }

    private void setupTitleAppearance(@NonNull MonthResProvider resProvider) {
        if (title == null) {
            return;
        }
        title.setTextColor(resProvider.getTextColor());
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resProvider.gatTextSize());
        title.setGravity(resProvider.getGravity());
        textAllCaps = resProvider.getTextAllCaps();
    }

    MonthViewHolder(View rootView) {
        super(rootView);
        title = null;
    }

    static MonthViewHolder create(@NonNull ViewGroup parent, @NonNull ClickCallback calendarCallback, @NonNull MonthResProvider resProvider, @NonNull DayResProvider dayResProvider) {
        return new MonthViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.scrollcalendar_month, parent, false),
                calendarCallback, resProvider, dayResProvider);
    }

    void bind(CalendarMonth month) {
        if (title != null) {
            String txt = month.getMonthNameWithYear(context);
            title.setText(txt);
        }
        for (int i = 0; i <= weeks.length - 1; i++) {
            weeks[i].display(i, month, filterWeekDays(i, month));
        }
    }


    CalendarDay[] filterWeekDays(int weekOfMonth, CalendarMonth calendarMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.YEAR, calendarMonth.getYear());
        calendar.set(Calendar.MONTH, calendarMonth.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<CalendarDay> days = new ArrayList<>();
        for (CalendarDay calendarDay : calendarMonth.getDays()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendarDay.getDay());
            if (calendar.get(Calendar.WEEK_OF_MONTH) == weekOfMonth) {
                days.add(calendarDay);
            }
        }
        return days.toArray(new CalendarDay[days.size()]);
    }

}
