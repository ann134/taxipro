package ru.sigmadigital.taxipro.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;

public class ButtonOrderView extends ConstraintLayout {

    String label;

    ConstraintLayout layout;
    FrameLayout imageContainer;
    ImageView image;
    TextView title;


    public ButtonOrderView(Context context, String label, String name, int src, int bgColor) {
        super(context);
        this.label = label;

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);

        layout = (ConstraintLayout) li.inflate(R.layout.view_button_order, this, true);
        imageContainer = layout.findViewById(R.id.image_container);
        image = layout.findViewById(R.id.image);
        title = layout.findViewById(R.id.title);

        imageContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setImageSize();
            }
        });


        setTitle(name);
        setImage(src);
        setBackgroundStates(bgColor);

    }

    private void setTitle(String title) {
        this.title.setText(title);
    }

    private void setImage(int src) {
        image.setImageDrawable(App.getAppContext().getDrawable(src));

        int padding = (int) (10 * getResources().getDisplayMetrics().density);
        image.setPadding(padding, padding, padding, padding);
    }

    private void setBackgroundStates(int bgColor) {
        Drawable drawableNormal = ResourcesCompat.getDrawable(getResources(), R.drawable.bt_oval_yellow, null);
        drawableNormal = DrawableCompat.wrap(drawableNormal);
        DrawableCompat.setTint(drawableNormal, getResources().getColor(bgColor));


        Drawable drawablePressed = drawableNormal.getConstantState().newDrawable();
        drawablePressed = DrawableCompat.wrap(drawablePressed);

        String hex = getResources().getString(bgColor);
        int alfa = (int) (255 * 0.75);
        String hexAlfa = Integer.toHexString(alfa);
        String hex2 = hex.substring(0, 1) + hexAlfa + hex.substring(3);

        DrawableCompat.setTint(drawablePressed, Color.parseColor(hex2));


        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[]{}, drawableNormal);

        image.setBackground(listDrawable);
    }

    private void setImageSize() {
        int side;
        int diffHor = 0;
        int diffVer = 0;

        if (imageContainer.getWidth() > imageContainer.getHeight()) {
            side = imageContainer.getHeight();
            diffHor = imageContainer.getWidth() - imageContainer.getHeight();
        } else {
            side = imageContainer.getWidth();
            diffVer = imageContainer.getHeight() - imageContainer.getWidth();
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(side, side);
        params.setMargins(diffHor / 2, diffVer / 2, diffHor / 2, diffVer / 2);
        image.setLayoutParams(params);
    }

    public String getLabel() {
        return label;
    }
}
