package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView chatsRecyclerView;
    private EditText msgInput;
    private FloatingActionButton sendMsgBtn;


    private ArrayList<ChatsModel> chatsModelArrayList;
    private ChatAdapter chatAdapter;

    private final String BOT_KEY ="bot";
    private final String USER_KEY ="user";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgInput = findViewById(R.id.chat_input_field);
        sendMsgBtn = findViewById(R.id.send_fab);
        chatsRecyclerView = findViewById(R.id.chats_recycler_view);

        chatsModelArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsModelArrayList, this);

        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatsRecyclerView.setAdapter(chatAdapter);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String message = msgInput.getText().toString().trim();
                if (!message.isEmpty())
                {
                    getResponse(message);
                    msgInput.setText("");
                }
                else
                {
                    msgInput.setError("Type Message");
                    msgInput.requestFocus();
                }
            }
        });
    }

    private void getResponse(String message)
    {
        chatsModelArrayList.add(new ChatsModel(message, USER_KEY));
        chatAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=163716&key=JxjNFFjF45MxL6K4&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MessageModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response)
            {
                if(response.isSuccessful())
                {
                    MessageModel messageModel = response.body();
                    chatsModelArrayList.add(new ChatsModel(messageModel.getCnt(), BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t)
            {
                chatsModelArrayList.add(new ChatsModel("Please revert your question", BOT_KEY));
                chatAdapter.notifyDataSetChanged();

            }
        });
    }
}