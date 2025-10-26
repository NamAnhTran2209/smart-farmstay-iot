package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login_app.R;

import java.util.List;

import model.FarmstayModel;

public class HomeFarmAdapter extends ArrayAdapter<FarmstayModel> {
    private Context context;
    private int resourceId;

    public HomeFarmAdapter(Context context, int resourceId, List<FarmstayModel> resorts) {
        super(context, resourceId, resorts);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);
        }

        FarmstayModel farmstayModel = getItem(position);

        ImageView imageView = view.findViewById(R.id.imFarm);
        String imageUrl = farmstayModel.getImageUrl();

        // Sử dụng để tải và hiển thị ảnh từ URL lên ImageView
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);

        TextView nameTextView = view.findViewById(R.id.tvNameFarm);
        nameTextView.setText(farmstayModel.getName());

        return view;
    }
}

