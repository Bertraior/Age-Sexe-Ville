package com.asv.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asv.R;

/**
 * Created by Bertraior on 25/06/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final String[] nomChat;
    private final String[] pseudoChat;
    private final String[] dateChat;
    private final int[] nbParticipantChat;

    public ChatArrayAdapter(Context context, String[] nomChat, String[] pseudoChat, String[] dateChat, int[] nbParticipantChat) {
        super(context, R.layout.list_chat, nomChat);
        this.context = context;
        this.nomChat = nomChat;
        this.pseudoChat = pseudoChat;
        this.dateChat = dateChat;
        this.nbParticipantChat = nbParticipantChat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_chat, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(nomChat[position]);
        textView.setTypeface(null, Typeface.BOLD);

        TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
        textView2.setText(pseudoChat[position] + " (" + dateChat[position] + ")");
        textView2.setPadding(0, 5, 0, 0);

        TextView textView3 = (TextView) rowView.findViewById(R.id.nbparticipant);
        textView3.setText(nbParticipantChat[position] + "/10");
        textView3.setPadding(0, 0, 20, 0);

        return rowView;
    }
}
