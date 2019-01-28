package be.patricegautot.getorganized.objects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.appdatabase.AppDatabase;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private static final String TAG = TodoAdapter.class.getSimpleName();

    private Context context;
    private List<ToDo> todos;
    private WeeklyTask task;
    private Resources resources;
    private AppDatabase mDB;
    private List<Boolean> expandStates;

    public TodoAdapter(@NonNull Context context, List<ToDo> todos, WeeklyTask task){

        this.context = context;
        this.todos = todos;
        this.task = task;
        this.resources = context.getResources();
        expandStates = new ArrayList<>(Arrays.asList(new Boolean[todos.size()]));
        //Log.e(TAG, "expanded states has size " + expandStates.size());
        Collections.fill(expandStates, Boolean.FALSE);
        this.mDB = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo, parent, false);
        return new TodoViewHolder(context, view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        ToDo todoToBind = todos.get(position);
        holder.bindTodoViewHolder(todoToBind);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder{

        private TextView mTodoTitle;
        private TextView mTodoDesc;
        private TextView mTodoPrio;
        private TextView mTodoDoneButton;
        private Context context;
        private WeeklyTask mTask;
        private Pair<ToDo, Boolean> state;
        private TodoAdapter mAdapter;

        private boolean expanded; //wether description is shown or not

        public TodoViewHolder(Context context, View itemView, TodoAdapter adapter) {
            super(itemView);

            this.mTodoTitle = itemView.findViewById(R.id.todo_title);
            this.mTodoDesc = itemView.findViewById(R.id.todo_description);
            this.mTodoPrio = itemView.findViewById(R.id.todo_priority);
            this.mTodoDoneButton = itemView.findViewById(R.id.todo_done_button);
            this.context = context;
            this.mAdapter = adapter;

            itemView.setOnClickListener(mOnClickListenerExpand);
            //Log.e(TAG, "itemview has listener");
        }

        public void bindTodoViewHolder(ToDo todo) {
            this.mTask = task;

            mTodoTitle.setText(todo.getTitle());
            mTodoDesc.setText(todo.getDescritpion());

            //Log.e(TAG, "updating todo no " + getAdapterPosition() + " name " + todo.getTitle() + " expanded is " + (expandStates.get(getAdapterPosition()) ? "1" : "0"));

            if(expandStates.get(getAdapterPosition()) == false) {
                mTodoDoneButton.setVisibility(View.GONE);
                mTodoDesc.setVisibility(View.GONE);
            } else {
                mTodoDoneButton.setVisibility(View.VISIBLE);
                mTodoDesc.setVisibility(View.VISIBLE);
            }

            mTodoDoneButton.setOnClickListener(mOnClickListenerDone);

            int priority = todo.getPriority();

            GradientDrawable prioCircle = (GradientDrawable) mTodoPrio.getBackground();

            switch (priority){
                case ToDo.PRIORITY_HIGH:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        prioCircle.setColor(ContextCompat.getColor(context, R.color.materialRed));
                    } else {
                        prioCircle.setColor(resources.getColor(R.color.materialRed));
                    }
                    break;
                case ToDo.PRIORITY_MEDIUM:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        prioCircle.setColor(ContextCompat.getColor(context, R.color.materialOrange));
                    } else {
                        prioCircle.setColor(resources.getColor(R.color.materialOrange));
                    }
                    break;
                case ToDo.PRIORITY_LOW:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        prioCircle.setColor(ContextCompat.getColor(context, R.color.materialYellow));
                    } else {
                        prioCircle.setColor(resources.getColor(R.color.materialYellow));
                    }
                    break;
            }

        }

        private View.OnClickListener mOnClickListenerExpand = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandStates.get(getAdapterPosition())){
                    expandStates.set(getAdapterPosition(), false);
                } else {
                    expandStates.set(getAdapterPosition(), true);
                }
                //Log.e(TAG, "click received, expanded now " + (expandStates.get(getAdapterPosition()) ? "1" : "0"));
                notifyItemChanged(getAdapterPosition());
            }
        };

        private View.OnClickListener mOnClickListenerDone = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                mAdapter.remove(pos);
            }
        };

    }

    //OTHER ADAPTER METHODS

    public void remove(final int pos){

        todos.remove(pos);
        task.setTodos(todos);
        notifyItemRemoved(pos);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDB.weeklyTaskDao().updateTask(task);
                //Log.e(TAG, "removed todo at index " + pos);
            }
        }).start();


    }

    public List<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<ToDo> newTodos){todos = newTodos;}
}
