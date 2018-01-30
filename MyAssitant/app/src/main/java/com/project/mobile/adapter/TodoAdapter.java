package com.project.mobile.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.mobile.helper.RealmHelper;
import com.project.mobile.helper.TodoCommunication;
import com.project.mobile.model.TodoInformation;
import com.project.mobile.myassitant.R;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

/**
 *
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<TodoInformation> todoInformations;
    private TodoCommunication communicator;
    private Realm realm;
    private RealmHelper realmHelper;

    public TodoAdapter(Context context, List<TodoInformation> todoInformations, TodoCommunication communication) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.todoInformations = todoInformations;
        this.communicator = communication;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_todo_list, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view, communicator);
        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
            holder.tvTitle.setText(todoInformations.get(i).getTitle());
            holder.tvTime.setText(todoInformations.get(i).getTime() + " - " + todoInformations.get(i).getDay());
            setColorLabel(todoInformations.get(i).getLabel(), holder.tvLabel);
    }

    @Override
    public int getItemCount() {
        return todoInformations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvTime;
        private CheckBox chbDone;
        private TextView tvLabel;

        private ViewHolder(View itemView, TodoCommunication mCommunicator) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_todo_title);
            tvTime = itemView.findViewById(R.id.tv_todo_message);
            tvLabel = itemView.findViewById(R.id.tv_todo_label);
            itemView.setOnClickListener(this);
            communicator = mCommunicator;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Are you sure to delete this activity?");
                    builder.setNegativeButton("No"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.setPositiveButton("Yes"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(getAdapterPosition());
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == itemView.getId()) {
                communicator.transformTodoInfor(todoInformations.get(getAdapterPosition()));
            }
        }
    }

    private void setColorLabel(int label, TextView tv){
        switch (label){
            case 0:
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_all));
                return;
            case 1:
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_personal));
                return;
            case 2:
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_work));
                return;
            case 3:
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_family));
                return;
            case 4:
                tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_friends));
        }
    }

    private void deleteItem(final int i) {
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        realmHelper.deleteStudentById(todoInformations.get(i).getTag());
        todoInformations.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, todoInformations.size());
    }
}
