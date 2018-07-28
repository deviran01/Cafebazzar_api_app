package com.example.ali_sarkhosh.cafebazzar.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.R;
import com.example.ali_sarkhosh.cafebazzar.activitys.ActivitySecond;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;

import java.util.ArrayList;

public class AdapterRVList extends ArrayAdapter<DataItem> {

    public AdapterRVList(ArrayList<DataItem> array){
        super(G.context , R.layout.recyclerview_row , array);

    }



    public static class ViewHolder{

        private TextView textView;
        private LinearLayout linearLayout;
        public ViewHolder(View view){

              textView = view.findViewById(R.id.row_tv_title);
              linearLayout = view.findViewById(R.id.rv_layout);


        }


        public void fill(ArrayAdapter<DataItem> adapter , final DataItem item  , int position){
            textView.setTypeface(G.tf);
            textView.setText(item.getName());

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(G.context , ActivitySecond.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("name" , item.getName());
                    intent.putExtra("city" , item.getCity());
                    intent.putExtra("country" , item.getCountry());
                    intent.putExtra("address",item.getAddress());
                    intent.putExtra("crossStreet" , item.getCrossStreet());
                    intent.putExtra("distance" , item.getDistance());
                    G.context.startActivity(intent);
                }
            });


        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        G.COUNTER = position;


        DataItem item = getItem(position);

        if(convertView == null){
            convertView = G.inflater.inflate(R.layout.recyclerview_row , parent , false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.fill(this , item, position);
        convertView.startAnimation(G.animationLst);
        return convertView;
    }
}
