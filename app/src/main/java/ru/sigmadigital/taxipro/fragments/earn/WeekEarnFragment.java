package ru.sigmadigital.taxipro.fragments.earn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.WeekDaysAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogChoose;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.util.DateFormatter;
import ru.sigmadigital.taxipro.view.Chart;
import ru.sigmadigital.taxipro.view.StatisticView;
import ru.sigmadigital.taxipro.view.TextWithIconView;


public class WeekEarnFragment extends BaseNavigatorFragment implements View.OnClickListener, DialogChoose.OnAcceptClickListener, WeekDaysAdapter.OnWeekDayClickListener {

    private List<Driver.WeekEarn> weekEarns;
    private Driver.WeekEarn currentWeek;

    private TextView errorText;
    private ProgressBar progressBar;
    private NestedScrollView body;

    private Chart chart;
    private RecyclerView recyclerView;

    private LinearLayout daysNumbers;
    private LinearLayout daysOfWeek;
    private StatisticView incomeOfWeek;
    private StatisticView ridesCount;
    private StatisticView onlineMinutes;


    public static WeekEarnFragment newInstance() {
        return new WeekEarnFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week_earn, container, false);
        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.progress_bar);
        body = v.findViewById(R.id.body);
        daysNumbers = v.findViewById(R.id.ll_days_numbers);
        daysOfWeek = v.findViewById(R.id.ll_days_of_week);
        incomeOfWeek = v.findViewById(R.id.sv_income_of_week);
        ridesCount = v.findViewById(R.id.sv_rides_count);
        onlineMinutes = v.findViewById(R.id.sv_online_minutes);
        recyclerView = v.findViewById(R.id.rv_income_of_day);
        TextWithIconView operations = v.findViewById(R.id.operations);
        ImageView chooseWeek = v.findViewById(R.id.imv_choose_week);

        EarnActionBarFragment.setActionBarTitle("ДОХОДЫ ЗА НЕДЕЛЮ");
        EarnActionBarFragment.setBackButtonVisible(true);

        incomeOfWeek.setTitle("НЕДЕЛЬНЫЙ ДОХОД");
        incomeOfWeek.setVisibleArrow(false);
        ridesCount.setTitle("ПОЕЗДОК");
        ridesCount.setTitleSize(14f);
        ridesCount.setCountSize(20f);
        ridesCount.setVisibleArrow(false);
        onlineMinutes.setTitle("МИНУТ В СЕТИ");
        onlineMinutes.setTitleSize(14f);
        onlineMinutes.setCountSize(20f);
        onlineMinutes.setVisibleArrow(false);
        operations.setTitle("Операции");
        operations.setArrow();
        operations.setOnClickListener(this);
        chooseWeek.setOnClickListener(this);

        chart = new Chart((BarChart) v.findViewById(R.id.chart), App.getAppContext(), true);

        recyclerView = v.findViewById(R.id.rv_income_of_day);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));

        sendRequest();

        return v;
    }


    private void sendRequest() {
        Sender.getInstance().send("driver.weekEarn", new Driver.WeekEarnFilter(0, 999).toJson(), this.getClass().getSimpleName());

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onWeeksReceived(responce);
                }
            }
        });
    }

    private <T> void onWeeksReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                weekEarns = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof Driver.WeekEarn) {
                        weekEarns.add((Driver.WeekEarn) item);
                    }
                }

                currentWeek = weekEarns.get(weekEarns.size() - 1);
                showCurrentWeek();

                progressBar.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:" + error.getMessage());
                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }

    private void showCurrentWeek() {

        incomeOfWeek.setCount(currentWeek.getTotal() + "\u20BD");
        ridesCount.setCount(currentWeek.getOrderCount() + "");
        onlineMinutes.setCount(currentWeek.getTimeOnline() + "");

        chart.setData(currentWeek.getWeekDays());

        Date date = DateFormatter.getDateFromStringUTCTimeZone(currentWeek.getStart());
        if (date != null) {
            initWeek(date);
        }

        initDaysAdapter(currentWeek.getWeekDays());
    }


    private void initWeek(Date date) {
        Calendar calendar = getCalendarDayFromDate(date);
        for (int i = 0; i < 7; i++) {
            TextView child1 = (TextView) daysNumbers.getChildAt(i);
            child1.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

            TextView child2 = (TextView) daysOfWeek.getChildAt(i);
            child2.setText(getDayOfWeekFromDate(calendar));
            if (getDayOfWeekFromDate(calendar).equals("сб") || getDayOfWeekFromDate(calendar).equals("вс")) {
                child2.setTextColor(getResources().getColor(R.color.areaRed));
            }

            calendar.add(Calendar.DATE, 1);
        }
    }

    private Calendar getCalendarDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private String getDayOfWeekFromDate(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return "пн";
            case Calendar.TUESDAY:
                return "вт";
            case Calendar.WEDNESDAY:
                return "ср";
            case Calendar.THURSDAY:
                return "чт";
            case Calendar.FRIDAY:
                return "пт";
            case Calendar.SATURDAY:
                return "сб";
            case Calendar.SUNDAY:
                return "вс";
            default:
                return "xз";
        }
    }

    private void initDaysAdapter(List<Driver.WeekDay> weekDays) {
        WeekDaysAdapter adapter = new WeekDaysAdapter(this, weekDays);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_choose_week:

                DialogChoose dialog = DialogChoose.newInstance(this, "Неделя");
                List<String> weeksString = new ArrayList<>();
                for (Driver.WeekEarn weekEarn : weekEarns) {

                    Date startDate = DateFormatter.getDateFromStringUTCTimeZone(weekEarn.getStart());
                    if (startDate != null) {
                        Date endDate = DateFormatter.datePlusDays(startDate, 6);

                        String startDateString = DateFormatter.getStringDdMmmmEromDate(startDate);
                        String endDateString = DateFormatter.getStringDdMmmmEromDate(endDate);

                        weeksString.add(startDateString + " - " + endDateString);
                    }
                }

                dialog.setDataList(weeksString);
                dialog.show(getFragmentManager(), "dialog");
                break;
            case R.id.operations:
                replaceCurrentFragmentWith(getFragmentManager(), getParentContainer(this.getView()), WeekOperationsFragment.newInstance(currentWeek.getStart()), true);
                break;
        }
    }

    @Override
    public void onWeekDayClick(Driver.WeekDay day) {
        replaceCurrentFragmentWith(getFragmentManager(), getParentContainer(this.getView()), DayEarnFragment.newInstance(day), true);
    }

    @Override
    public void OnAcceptClick(String startDaySelected) {
        String selected = startDaySelected.substring(0, startDaySelected.indexOf('-'));
        selected = selected.trim();

        for (Driver.WeekEarn weekEarn : weekEarns) {

            Date weekStart = DateFormatter.getDateFromStringUTCTimeZone(weekEarn.getStart());
            if (weekStart != null) {
                String weekStartString = DateFormatter.getStringDdMmmmEromDate(weekStart);

                if (weekStartString.equals(selected)) {
                    currentWeek = weekEarn;
                }
            }
        }

        chart.setData(currentWeek.getWeekDays());
        showCurrentWeek();
    }

}
