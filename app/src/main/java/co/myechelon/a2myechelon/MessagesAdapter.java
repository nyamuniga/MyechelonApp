package co.myechelon.a2myechelon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Teacher on 6/15/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.TextsViewHolder>{
    private List<Texts> messages;


    public MessagesAdapter(List<Texts> messages) {
        this.messages = messages;
    }

    @Override
    public TextsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_text,parent,false);
        return  new TextsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextsViewHolder holder, int position) {
      Texts texts = messages.get(position);
        holder.msg.setText(texts.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class TextsViewHolder extends RecyclerView.ViewHolder{
        public TextView msg;
        public TextsViewHolder(View view){

            super(view);
            msg=(TextView) view.findViewById(R.id.txt);

        }


    }
}
