package com.android.ctisandbox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ctisandbox.Model.Model;
import com.android.ctisandbox.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    ArrayList<Model> modelArrayList = new ArrayList<>();
    Context context;

    public Adapter(Context context, ArrayList<Model> modelArrayList) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img);
        TextView textView = convertView.findViewById(R.id.tv);
        TextView tableTextView = convertView.findViewById(R.id.table);
        TextView colTextView = convertView.findViewById(R.id.column);
        TextView outputRawTextView = convertView.findViewById(R.id.outputRaw);
        LinearLayout linearLayout = convertView.findViewById(R.id.lin);

        final Model model = modelArrayList.get(position);

        textView.setText(model.getName());
        tableTextView.setText(model.getTableName());
        colTextView.setText(model.getTableColumn());
        outputRawTextView.setText(String.valueOf(model.getOutputRaw()));

        if (!model.getImage().equals("")) {
            Picasso.get().load(model.getImage()).into(imageView);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, model.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
