package com.example.ims2019;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {

        super(fm);
    }



    @Override
    public Fragment getItem(int i) {


        switch (i){
            case 0:
                ComposeFragment composeFragment = new ComposeFragment();
                return composeFragment;

            case 1:
                E_DepartmentsFragment e_departmentsFragment = new E_DepartmentsFragment();
                return e_departmentsFragment;
            default:
                return null;
        }

    }


    @Override
    public int getCount() {

        return 2;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Compose";

            case 1:
                return "Departments";
            default:
                return null;
        }
    }
}
