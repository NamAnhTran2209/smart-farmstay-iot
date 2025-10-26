package com.example.login_app.home_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.login_app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import adapter.HomeFarmAdapter;
import adapter.ViewPageHomeAdapter;
import api.FarmModel;
import api.RetrofitInterface;
import api.RetrofitServer;
import me.relex.circleindicator.CircleIndicator;
import model.FarmstayModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    SharedPreferences sharedPreferences;

    private TextView tvUsername;
    private ViewPager viewPager;
    private ViewPageHomeAdapter viewPageHomeAdapter;
    private CircleIndicator circleIndicator;
    private ListView lvHomeFarm;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        viewPager = view.findViewById(R.id.viewPager_home);
        tvUsername = view.findViewById(R.id.tvUsernameHome);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        lvHomeFarm = view.findViewById(R.id.lvHomeFarm);

        viewPageHomeAdapter = new ViewPageHomeAdapter(getParentFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageHomeAdapter);
        circleIndicator.setViewPager(viewPager);

        // Tự động chuyển fragment sau mỗi 2 giây
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (viewPager != null) {
                    viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % viewPageHomeAdapter.getCount(), true);
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo", MODE_PRIVATE);
        tvUsername.setText(sharedPreferences.getString("usernameUser",""));

        //set hien thi farmstay
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        List<FarmstayModel> farmstayList = new ArrayList<>(); // tạo một danh sách ArrayList để chứa các Farm

        Call<FarmModel> call = retrofitInterface.getFarm(token);
        call.enqueue(new Callback<FarmModel>() {
            @Override
            public void onResponse(Call<FarmModel> call, Response<FarmModel> response) {
                Log.e("codeFarm", response.code()+"");
                if(response.code() == 200){
                    FarmModel result = response.body();
                    JsonArray data = result.getData();

                    for (int i = 0; i < data.size(); i++) {
                        JsonObject farm = (JsonObject) data.get(i);
                        Log.e("Farm " + i, farm.toString());

                        String id = farm.get("uuid").getAsString();
                        String name = farm.get("name").getAsString();
                        JsonArray images = (JsonArray) farm.get("images");
                        String imageUrl = null;
                        for (int j = 0; j < images.size(); j++) {
                            imageUrl = images.get(j).getAsString();

                        }
                        //thong tin chi tiet farm
                        String description = farm.get("description").getAsString();

                        JsonObject addressFull = (JsonObject) farm.get("address");
                        String address = addressFull.get("specific_address").getAsString();

                        String price = farm.get("rent_cost_per_day").getAsString();

//                        Log.e("images", String.valueOf(images));
//                        Log.e("uuid " + i, id);
//                        Log.e("name " + i, name);
//                        Log.e("description " + i, description);
//                        Log.e("imageItem",imageUrl);

                        // Thêm từng Farm vào danh sách
                        FarmstayModel farmstayModel = new FarmstayModel(name,description, address, price, imageUrl);
                        farmstayList.add(farmstayModel);
                    }

                    // Tạo một adapter mới và gán nó vào ListView
                    HomeFarmAdapter homeFarmAdapter = new HomeFarmAdapter(getActivity(), R.layout.layout_home_farm, farmstayList);
                    lvHomeFarm.setAdapter(homeFarmAdapter);
                    homeFarmAdapter.notifyDataSetChanged();

//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("emailUser", email);
//                    editor.putString("usernameUser", username);
//                    editor.commit();kl
                }
            }

            @Override
            public void onFailure(Call<FarmModel> call, Throwable t) {

            }
        });

        lvHomeFarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Lấy ra thông tin Farm tương ứng với item được click
                FarmstayModel farm = farmstayList.get(i);

                // Tạo một Intent để chuyển sang Activity mới để hiển thị thông tin chi tiết của Farm
                Intent intent = new Intent(getActivity(), Farm_1Activity.class);

                // Đưa thông tin của Farm vào trong Intent để truyền sang Activity mới
                intent.putExtra("name", farm.getName());
                intent.putExtra("imageUrl", farm.getImageUrl());
                intent.putExtra("description", farm.getDescription());
                intent.putExtra("address", farm.getAddress());
                intent.putExtra("price", farm.getPrice());

                // Chuyển sang Activity mới để hiển thị thông tin chi tiết của Farm
                startActivity(intent);
            }
        });

        return view;
    }
}