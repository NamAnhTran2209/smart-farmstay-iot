package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.login_app.img_dashboard.ImageFragment_1;
import com.example.login_app.img_dashboard.ImageFragment_2;
import com.example.login_app.img_dashboard.ImageFragment_3;
import com.example.login_app.img_dashboard.ImageFragment_4;

public class ViewPageHomeAdapter extends FragmentStatePagerAdapter {

    public ViewPageHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ImageFragment_1();
            case 1:
                return new ImageFragment_2();
            case 2:
                return new ImageFragment_3();
            case 3:
                return new ImageFragment_4();
            default:
                return new ImageFragment_1();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
