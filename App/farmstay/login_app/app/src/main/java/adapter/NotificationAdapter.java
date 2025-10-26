package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_app.R;

import java.util.List;

import model.Notification_model;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<Notification_model> listNotify;

    public NotificationAdapter(Context context, List<Notification_model> listNotify) {
        this.context = context;
        this.listNotify = listNotify;
    }

    @Override
    public int getCount() {
        return listNotify.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0)
            return null;
        return listNotify.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_notification, null);

        TextView tvTieude = view.findViewById(R.id.tvTieude);
        TextView tvNoidung = view.findViewById(R.id.tvNoidung);
        ImageView imageView = view.findViewById(R.id.imageIcoin);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvDate = view.findViewById(R.id.tvDate);

        Notification_model notification = listNotify.get(i);
        tvTieude.setText(notification.getTieude());
        tvNoidung.setText(notification.getNoidung());

        tvTime.setText(notification.getTime());
        tvDate.setText(notification.getDate());

        int idImage = notification.getImage();
        switch (idImage){
            case 1:
                imageView.setImageResource(R.drawable.ic_notifications);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_warning);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_fire_2);
                break;
            default:
                break;
        }
        return view;
    }
}
