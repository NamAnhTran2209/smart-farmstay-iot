package com.example.login_app.farmstay_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.login_app.R;
import com.example.login_app.home_page.DashboardActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import adapter.ViewPagerAdapter_3;
import api.LoginResult;
import api.RentedFarm;
import api.RetrofitInterface;
import api.RetrofitServer;
import api.SensorResuilt;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.SensorData;
import model.SocketIO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmstayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FarmstayFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvMessage;

    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FarmstayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmstayFragment newInstance(String param1, String param2) {
        FarmstayFragment fragment = new FarmstayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmstay, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager_farmstay);
        tvMessage = view.findViewById(R.id.tvMessage);

        tvMessage.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);

        ViewPagerAdapter_3 viewPagerAdapter_3 = new ViewPagerAdapter_3(getActivity(), getParentFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter_3);
        tabLayout.setupWithViewPager(viewPager);

        getData();

        return view;
    }

    private void getData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        Call<RentedFarm> callFarm = retrofitInterface.RentedFarm(token);
        callFarm.enqueue(new Callback<RentedFarm>() {
            @Override
            public void onResponse(Call<RentedFarm> call, Response<RentedFarm> response) {
                RentedFarm result = response.body();
                if(result != null){
                    JsonObject data = result.getData();
                    //test
                    Log.e("dataFarmTest", String.valueOf(data));

                    String nameFarm = data.get("name").getAsString();

                    JsonObject addressFull = (JsonObject) data.get("address");
                    String address = addressFull.get("specific_address").getAsString();

                    tvMessage.setVisibility(View.INVISIBLE);
                    viewPager.setVisibility(View.VISIBLE);

                } else {
                    tvMessage.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RentedFarm> call, Throwable t) {

            }
        });
    }

}