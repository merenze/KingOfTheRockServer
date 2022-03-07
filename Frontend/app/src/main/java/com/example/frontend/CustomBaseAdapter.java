package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> usernames;
    ArrayList<String> accountTypes;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context context, ArrayList usernames, ArrayList accountTypes){
        this.context = context;
        this.usernames = usernames;
        this.accountTypes = accountTypes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, viewGroup, false);
        TextView usernameTextView = (TextView) view.findViewById(R.id.customlist_username_textview);
        TextView accountTypeTextView = (TextView) view.findViewById(R.id.customlist_accounttype_textview);
        usernameTextView.setText(usernames.get(i));
        accountTypeTextView.setText(accountTypes.get(i));
        return view;
    }
}
