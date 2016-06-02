package de.sicher.sichersmarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.objects.StartMenuItem;

/**
 * Created by sicher on 03.05.2016.
 */
public class GridAdapter extends ArrayAdapter<StartMenuItem> {

    private final Context context;
    private final ArrayList<StartMenuItem> values;

    public GridAdapter(Context context, ArrayList<StartMenuItem> values) {
        super(context, R.layout.start_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridLayout = inflater.inflate(R.layout.start_item, parent, false);
        ImageView icon = (ImageView) gridLayout.findViewById(R.id.icon);
        StartMenuItem current = values.get(position);
        icon.setImageResource(current.getImageResourceID());
        return gridLayout;
    }
}
