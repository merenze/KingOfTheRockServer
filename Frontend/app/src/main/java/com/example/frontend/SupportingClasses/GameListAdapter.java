package com.example.frontend.SupportingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Class for a custom adapter for a list of games.
 *
 * @author Dan Rosenhamer
 */
public class GameListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    /**
     * Constructs a new GameListAdapter from a list and context.
     *
     * @param list    List of String games
     * @param context given context
     */
    public GameListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /**
     * Returns the number of games in the list
     *
     * @return size number
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Returns a game from the list at a given position
     *
     * @param pos position of a game in the list
     * @return game at given position
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /**
     * Returns the id of the game in the list at a given position
     *
     * @param pos position of a game in the list
     * @return id of the game at given position
     */
    @Override
    public long getItemId(int pos) {
        //change to list.get(pos).getId(); if the games have an id
        return 0;
    }

    /**
     * Returns the current View
     *
     * @param position    position of the view to be returned
     * @param convertView view to be returned after inflation
     * @param parent      group of views
     * @return given view after inflation
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
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
