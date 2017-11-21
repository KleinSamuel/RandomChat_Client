package com.samuel.klein.randomchat.chat.userlist;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sam on 10.11.17.
 */

public class UserlistAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<>();

    public UserlistAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); i++) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
