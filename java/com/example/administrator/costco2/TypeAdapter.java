package com.example.administrator.costco2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by double on 9/8/16.
 */
public class TypeAdapter extends ArrayAdapter<TypeItem> {
    private int resourceId;

    public TypeAdapter(Context context, int textViewResourceId,
                       List<TypeItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TypeItem item = getItem(position);
        TextView typeName = (TextView) view.findViewById(R.id.type_item_name);
        typeName.setText(item.getName());

        return view;
    }
}
