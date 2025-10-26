package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.login_app.onboarding.OnboardingFragment_1;
import com.example.login_app.onboarding.OnboardingFragment_2;
import com.example.login_app.onboarding.OnboardingFragment_3;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardingFragment_1();
            case 1:
                return new OnboardingFragment_2();
            case 2:
                return new OnboardingFragment_3();
            default:
                return new OnboardingFragment_1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
