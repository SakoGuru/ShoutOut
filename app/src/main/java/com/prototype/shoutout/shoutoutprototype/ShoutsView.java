package com.prototype.shoutout.shoutoutprototype;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

//Display for Shouts
public class ShoutsView extends ArrayAdapter<Shouts>{
    Context mContext;

    //Layout Resource
    int mLayoutResourceId;

    public ShoutsView(Context context, int layoutResourceId){
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    //TODO This is just going to show the Table view of the data, you'll need
    //TODO to convert this to the call for showing the bubble (or erase this if  you do it elsewhere)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Shouts current = getItem(position);

        if(row == null){
            LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
            row = inflate.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(current);
        return row;
    }
}
