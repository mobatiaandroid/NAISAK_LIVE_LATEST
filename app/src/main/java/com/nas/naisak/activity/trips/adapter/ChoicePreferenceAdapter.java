package com.nas.naisak.activity.trips.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.activity.trips.model.ChoicePreferenceModel;

import java.util.ArrayList;


public class ChoicePreferenceAdapter extends RecyclerView.Adapter<ChoicePreferenceAdapter.MyViewHolder> {
    Context context;
    ArrayList<ChoicePreferenceModel> choiceList;
    OnItemSelectedListener listener;

    public ChoicePreferenceAdapter(Context mContext, ArrayList<ChoicePreferenceModel> choiceList, OnItemSelectedListener listener) {
        this.context = mContext;
        this.choiceList = choiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_choice_preference_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChoicePreferenceModel model = choiceList.get(position);
        holder.choiceButton.setText(model.getChoiceName());
        if(choiceList.get(position).getSelected())
        {
           holder.choiceButton.setBackgroundResource(R.drawable.rectangle_navy_blue_button);
           holder.choiceButton.setTextColor(context.getResources().getColor(R.color.white));
        }
        else {
            holder.choiceButton.setBackgroundResource(R.drawable.button_blue_outline);
            holder.choiceButton.setTextColor(context.getResources().getColor(R.color.split_bg));
        }

//        holder.choiceButton.setBackgroundResource(R.drawable.rectangle_navy_blue_button);
//        int backgroundColor = model.getSelected() ? R.drawable.rectangle_navy_blue_button : R.drawable.button_blue_outline;
//        holder.choiceButton.setBackgroundResource(backgroundColor);
//        int fontColor = model.getSelected() ? R.color.white : R.color.split_bg;
//        holder.choiceButton.setTextColor(context.getResources().getColor(fontColor));
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String choice);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button choiceButton;

        public MyViewHolder(View view) {
            super(view);
            choiceButton = view.findViewById(R.id.choiceButton);
            choiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        for (int i = 0; i < choiceList.size(); i++) {
                            if (i == position) {
                                choiceList.get(i).setSelected(true);
                                if (listener != null) {
                                    listener.onItemSelected(choiceList.get(i).getChoiceName());
                                }
                            } else {
                                choiceList.get(i).setSelected(false);
                            }
                        }
                        notifyDataSetChanged();
                    }
                }

            });
        }
    }
}
