package com.example.frontend.SupportingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public GameListAdapter(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int pos){
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos){
        //change to list.get(pos).getId(); if the games have an id
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //TODO
            //view = inflater.inflate(R.layout.GAMELAYOUTNAME)
        }

        //TODO
        //Handle TextView and display string from your list
        //TextView gameName = (TextView) view.findViewById(R.id.GAMENAMETEXT);
        //gameName.setText(list.get(position));

        //TODO
        //Handle buttons and add onClickListeners
        //Button joinGameButton = (Button) view.findViewById(R.id.GAMEBUTTON);

        //TODO
        //join the game when button is pressed
        /*
        joinGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //join the game
            }
        });
        */

        return view;
    }
}
