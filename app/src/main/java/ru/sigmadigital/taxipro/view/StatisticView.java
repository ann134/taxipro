package ru.sigmadigital.taxipro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import ru.sigmadigital.taxipro.R;

public class StatisticView extends ConstraintLayout {

    private TextView title;
    private TextView count;
    private TextView text;
    private ImageView arrow;
    private ProgressBar progressBar;
    private OnClickListener listener;

    public StatisticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        ConstraintLayout layout = (ConstraintLayout) li.inflate(R.layout.view_statistic, this, true);

        this.progressBar = layout.findViewById(R.id.progress_bar);
        this.title = layout.findViewById(R.id.tv_title);
        this.count = layout.findViewById(R.id.tv_count);
        this.text = layout.findViewById(R.id.text);
        this.arrow = layout.findViewById(R.id.imv_arrow);
    }


    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setCount(String count) {
        progressBar.setVisibility(GONE);
        this.count.setText(count);
    }

    public void setText(String text) {
        progressBar.setVisibility(GONE);
        this.text.setVisibility(VISIBLE);
        this.text.setText(text);
    }

    public String getCount() {
        return count.getText().toString();
    }

    public String getTitle() {
        return title.getText().toString();
    }


    public void setVisibleArrow(boolean visible) {
        if (visible) {
            arrow.setVisibility(VISIBLE);
        } else {
            arrow.setVisibility(GONE);
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
    }


    public OnClickListener getListener() {
        return listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTitleSize(float sp) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    public void setCountSize(float sp) {
        count.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }



}
