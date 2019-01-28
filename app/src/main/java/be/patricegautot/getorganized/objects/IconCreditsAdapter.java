package be.patricegautot.getorganized.objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import be.patricegautot.getorganized.R;

public class IconCreditsAdapter extends RecyclerView.Adapter<IconCreditsAdapter.IconCreditsViewHolder> {

    int[] textResId = {R.string.string_credit_icon_1, R.string.string_credit_icon_2, R.string.string_credit_icon_3,
            R.string.string_credit_icon_4, R.string.string_credit_icon_5, R.string.string_credit_icon_6, R.string.string_credit_icon_7,
            R.string.string_credit_icon_8, R.string.string_credit_icon_9, R.string.string_credit_icon_10, R.string.string_credit_icon_11,
            R.string.string_credit_icon_12, R.string.string_credit_icon_13, R.string.string_credit_icon_14, R.string.string_credit_icon_15,
            R.string.string_credit_icon_16, R.string.string_credit_icon_17};

    int[] iconResId = {R.drawable.ic_sport, R.drawable.ic_work, R.drawable.ic_wake_up, R.drawable.ic_sleep, R.drawable.ic_app,
                       R.drawable.ic_credits, R.drawable.ic_notebook, R.drawable.ic_notification, R.drawable.ic_check_button,
                       R.drawable.ic_advice, R.drawable.ic_help, R.drawable.ic_about_app, R.drawable.ic_todo_list,
                       R.drawable.play_pause_button, R.drawable.sport_advice, R.drawable.work_advice, R.drawable.wake_up_advice };

    private Context context;

    public IconCreditsAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public IconCreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_credit_item, parent, false);

        return new IconCreditsAdapter.IconCreditsViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconCreditsViewHolder holder, int position) {
        holder.bindIconCreditViewHolder(textResId[position], iconResId[position]);

    }

    @Override
    public int getItemCount() {
        return 17;
    }

    public class IconCreditsViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private int textId;
        private int drawableId;
        private TextView creditText;
        private ImageView iconImage;

        public IconCreditsViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            creditText = itemView.findViewById(R.id.icon_credit_tv);
            iconImage = itemView.findViewById(R.id.icon_image_iv);
        }

        public void bindIconCreditViewHolder(int textId, int drawableId){
            this.textId = textId;
            this.drawableId = drawableId;

            iconImage.setBackgroundResource(drawableId);
            creditText.setText(textId);

            creditText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
