package ru.sigmadigital.taxipro.adapters;

import androidx.annotation.NonNull;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import ru.sigmadigital.taxipro.fragments.training.TrainingFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<TrainingFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<TrainingFragment> recipeList) {
        super(fm);
        this.fragments = recipeList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}




