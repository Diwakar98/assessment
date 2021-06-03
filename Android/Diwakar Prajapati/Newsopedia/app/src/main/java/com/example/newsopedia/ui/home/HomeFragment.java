package com.example.newsopedia.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newsopedia.Adapter;
import com.example.newsopedia.ApiClient;
import com.example.newsopedia.Model.Articles;
import com.example.newsopedia.Model.Category;
import com.example.newsopedia.Model.Headlines;
import com.example.newsopedia.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    final String API_KEY = "58a952499a7341b3955c30e0798bbb42";
    RecyclerView recyclerView;
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        retrieveJSON(getCountry(),API_KEY);
        root.findViewById(R.id.button).setOnClickListener(View -> getImageUsingKeyword());

        return root;
    }

    void getImageUsingKeyword(){
        EditText editText = root.findViewById(R.id.editText);
        String s = editText.getText().toString().trim();
        editText.setText("");
        if(!s.equals("")){
            retrieveJSON1(s,API_KEY);
        }
    }

    public void retrieveJSON1(String query, String apiKey){
        Call<Category> call = ApiClient.getInstance().getApi().getCategory(query,apiKey);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                try {
                    if(response.isSuccessful() && response.body().getArticles()!=null){
                        articles.clear();
                        articles=response.body().getArticles();
                        adapter = new Adapter(getContext(), articles);
                        recyclerView.setAdapter(adapter);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });
    }

    public void retrieveJSON(String country, String apiKey){
        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                try {
                    if(response.isSuccessful() && response.body().getArticles()!=null){
                        articles.clear();
                        articles=response.body().getArticles();
                        adapter = new Adapter(getContext(), articles);
                        recyclerView.setAdapter(adapter);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {

            }
        });
    }

    public String getCountry(){
        return Locale.getDefault().getCountry().toLowerCase();
    }
}