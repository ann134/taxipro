package ru.sigmadigital.taxipro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;

public class TextWithIconView  extends ConstraintLayout {

    ConstraintLayout layout;
    ImageView icon;
    TextView title;
    TextView iconCount;
    ImageView arrow;

    public TextWithIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        layout = (ConstraintLayout) li.inflate(R.layout.view_text_with_icon, this, true);

        icon = layout.findViewById(R.id.icon);
        title = layout.findViewById(R.id.title);
        iconCount = layout.findViewById(R.id.icon_count);
        arrow = layout.findViewById(R.id.arrow);
    }

    public void setIcon(int drawable) {
        this.icon.setVisibility(VISIBLE);
        this.icon.setImageDrawable(App.getAppContext().getDrawable(drawable));
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setIconCount(String text) {
        this.iconCount.setVisibility(VISIBLE);
        this.iconCount.setText(text);
    }

    public void setArrow() {
        this.arrow.setVisibility(VISIBLE);
    }

    public float getTextSise(){
        return title.getTextSize();

    }
}
