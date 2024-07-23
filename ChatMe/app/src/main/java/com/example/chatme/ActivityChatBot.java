package com.example.chatme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.ChatAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityChatBot extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText userChat;
    private Button btnKirim;
    private TextView user;

    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ModelPesan> arrayList;
    private ChatAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        recyclerView = findViewById(R.id.recycViem);
        userChat = findViewById(R.id.txt_pesan);
        btnKirim = findViewById(R.id.btn_kirim);
        user = findViewById(R.id.txtId);

        SharedPreferences prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");

        user.setText(username);

        arrayList = new ArrayList<>();
        adapter = new ChatAdapter(arrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userChat.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityChatBot.this, "Tuliskan Pesan", Toast.LENGTH_SHORT).show();
                    return;
                }

                getResponse(userChat.getText().toString());
                userChat.setText("");
            }
        });
    }

    private void getResponse(String newPesan) {
        arrayList.add(new ModelPesan(newPesan, USER_KEY));
        adapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=182725&key=TZ64NXSQGzcP3Occ&uid=[uid]&msg=" + newPesan;
        String base_url = "http://api.brainshop.ai/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChatBotAPI chatBotAPI = retrofit.create(ChatBotAPI.class);
        Call<BotModel> call = chatBotAPI.getPesan(url);

        call.enqueue(new Callback<BotModel>() {
            @Override
            public void onResponse(Call<BotModel> call, Response<BotModel> response) {
                if (response.isSuccessful()) {
                    BotModel model = response.body();
                    arrayList.add(new ModelPesan(model.getPesanBot(), BOT_KEY));
                    adapter.notifyDataSetChanged();
                } else {
                    arrayList.add(new ModelPesan("Response error", BOT_KEY));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BotModel> call, Throwable t) {
                arrayList.add(new ModelPesan("Masukan Pesan Kembali", BOT_KEY));
                adapter.notifyDataSetChanged();
            }
        });
    }

}
