package com.jbkindelberger.testproject;

import java.util.ArrayList;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<Contact> {



    int groupid;

    ArrayList<Contact> records;

    Context context;



    public CustomAdapter(Context context, int vg, int id, ArrayList<Contact>
            records) {

        super(context, vg, id, records);

        this.context = context;

        groupid = vg;

        this.records = records;

    }



    public View getView(int position, View convertView, ViewGroup parent) {



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(groupid, parent, false);

        TextView textName = (TextView) itemView.findViewById(R.id.contact_name);

        textName.setText(records.get(position).getcName());

        TextView textDate = (TextView) itemView.findViewById(R.id.contact_date);
        textDate.setText(records.get(position).getcDate());


        return itemView;

    }

}