package ru.sigmadigital.taxipro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import ru.sigmadigital.taxipro.R;

public class SettingsView extends ConstraintLayout {

    ConstraintLayout layout;
    TextView title;
    TextView text;

    public SettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        layout = (ConstraintLayout) li.inflate(R.layout.view_settings, this, true);

        title = layout.findViewById(R.id.title);
        text = layout.findViewById(R.id.text);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return text.getText().toString();
    }

    public void  setTextScale(float newScale){

        Log.e("textsizeGetSiseBefore", text.getTextSize()+"");

        text.setTextSize(text.getTextSize()+newScale);

        Log.e("textsizeGetSiseAfter", text.getTextSize()+"");

    }

}
