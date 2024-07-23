package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatme.ModelPesan;
import com.example.chatme.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ModelPesan> modelPesanArrayList;
    private Context context;

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;

    public ChatAdapter(ArrayList<ModelPesan> modelPesanArrayList, Context context) {
        this.modelPesanArrayList = modelPesanArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user, parent, false);
            return new UserViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModelPesan modelPesan = modelPesanArrayList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_USER) {
            ((UserViewHolder) holder).userChat.setText(modelPesan.getPesan());
        } else {
            ((BotViewHolder) holder).botChat.setText(modelPesan.getPesan());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (modelPesanArrayList.get(position).getPengirim().equals("user")) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_BOT;
        }
    }

    @Override
    public int getItemCount() {
        return modelPesanArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userChat;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userChat = itemView.findViewById(R.id.chatUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botChat;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botChat = itemView.findViewById(R.id.chatBot);
        }
    }
}
