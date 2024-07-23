package com.example.chatme;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ChatBotAPI {
    @GET
    Call<BotModel> getPesan(@Url String sql);
}
