package cau.injiyong.slight;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


public class MessageAdapter extends ArrayAdapter<TextItem> {
    Context mContext;
    public MessageAdapter(Context context, int resource, List<TextItem> objects) {
        super(context, resource, objects);
        mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_text_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.colorImage);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.dateTextView);

        TextItem message = getItem(position);

        Drawable roundDrawable = mContext.getResources().getDrawable(R.drawable.shape);
        roundDrawable.setColorFilter(Integer.parseInt(message.getColor()), PorterDuff.Mode.SRC_ATOP);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            photoImageView.setBackgroundDrawable(roundDrawable);
        } else {
            photoImageView.setBackground(roundDrawable);
        }
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());

        authorTextView.setText(message.getDate());

        return convertView;
    }
}