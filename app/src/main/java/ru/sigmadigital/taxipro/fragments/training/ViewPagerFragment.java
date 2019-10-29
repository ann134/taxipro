package ru.sigmadigital.taxipro.fragments.training;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.adapters.ViewPagerAdapter;
import ru.sigmadigital.taxipro.fragments.BaseFragment;
import ru.sigmadigital.taxipro.fragments.main.MapFragment;


public class ViewPagerFragment extends BaseFragment implements TrainingFragment.OnStartClickListener, View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout indicatorContainer;
    private TextView button;


    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_view_pager, null);
        viewPager = view.findViewById(R.id.view_pager);
        indicatorContainer = view.findViewById(R.id.indicator_container);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);

        init();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                updateIndicators();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        return view;
    }


    private void init(){
        List<TrainingFragment> fragments = new ArrayList<>();
        fragments.add(TrainingFragment.newInstance(1,this, 0, "", ""));
        fragments.add(TrainingFragment.newInstance(0,this, R.drawable.intro_2, getString(R.string.training_tit2), getString(R.string.training_text2)));
        fragments.add(TrainingFragment.newInstance(0,this, R.drawable.intro_3, getString(R.string.training_tit3), getString(R.string.training_text3)));
        fragments.add(TrainingFragment.newInstance(0,this, R.drawable.intro_4, getString(R.string.training_tit4), getString(R.string.training_text4)));
        fragments.add(TrainingFragment.newInstance(4,this, 0,  "",""));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        updateIndicators();
    }


    private void updateIndicators(){
        for (int n = 0; n < indicatorContainer.getChildCount(); n++) {
            indicatorContainer.getChildAt(n).setBackgroundResource(R.drawable.dot_gray);
        }
        button.setVisibility(View.VISIBLE);

        indicatorContainer.getChildAt(viewPager.getCurrentItem()).setBackgroundResource(R.drawable.dot_yellow);
        if (viewPager.getAdapter() != null && viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1){
            button.setVisibility(View.GONE);
        }
    }



    @Override
    public void onStartClick() {
        skip();

    }

    @Override
    public void onClick(View v) {
        skip();

    }

    public void skip(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        //loadFragment(MapFragment.newInstance(), " gj", true);
    }



    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }
}
