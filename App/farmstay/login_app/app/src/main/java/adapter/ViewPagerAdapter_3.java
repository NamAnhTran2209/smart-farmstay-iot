package adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.login_app.farmstay_page.FarmFragment;
import com.example.login_app.farmstay_page.HouseFragment;
import com.example.login_app.R;

public class ViewPagerAdapter_3 extends FragmentStatePagerAdapter {

    private Context mContext;

    public ViewPagerAdapter_3(@NonNull Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HouseFragment();
            case 1:
                return new FarmFragment();
            default:
                return new HouseFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch(position){
            case 0:
                title = mContext.getString(R.string.home1);
                break;
            case 1:
                title = mContext.getString(R.string.farm);
                break;
        }
        return title;
    }
}
