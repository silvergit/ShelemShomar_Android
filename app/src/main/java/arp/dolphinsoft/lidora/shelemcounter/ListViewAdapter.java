package arp.dolphinsoft.lidora.shelemcounter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ali on 12/28/15.
 */
public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txt_lst_first_group_name;
    TextView txt_lst_round_read;
    TextView txt_lst_second_group_name;
    TextView txt_lst_arrow;

    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.lay_column_row, null);

            txt_lst_first_group_name =(TextView) convertView.findViewById(R.id.lst_first_group);
            txt_lst_round_read =(TextView) convertView.findViewById(R.id.lst_round_read);
            txt_lst_second_group_name =(TextView) convertView.findViewById(R.id.lst_second_group);
            txt_lst_arrow =(TextView) convertView.findViewById(R.id.lst_arrow);
        }

        HashMap<String, String> map=list.get(position);
        txt_lst_first_group_name.setText(map.get(Constants.FIRST_COLUMN));
        txt_lst_round_read.setText(map.get(Constants.SECOND_COLUMN));
        txt_lst_arrow.setText(map.get(Constants.SECOND_ARROW_COLUMN));
        txt_lst_second_group_name.setText(map.get(Constants.THIRD_COLUMN));

        return convertView;
    }

}