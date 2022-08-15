package com.example.instagram.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.adapter.PostAdapter;
import com.example.instagram.databinding.ActivityFeedBinding;
import com.example.instagram.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<PostModel> postArrayList;
    private ActivityFeedBinding binding;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postArrayList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        System.out.println("GET DATA");
        firebaseFirestore.collection("Post").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            if (value != null) {
                List<DocumentSnapshot> snaps = value.getDocuments();
                for (DocumentSnapshot doc : snaps) {
                    Map<String, Object> map = doc.getData();
                    if (map != null) {
                        //date
                        String comment = (String) map.get("comment");
                        String downloadUrl = (String) map.get("downloadUrl");
                        String email = (String) map.get("email");
                        PostModel post = new PostModel(email, comment, downloadUrl);
                        postArrayList.add(post);
                    }
                }
            }

            postAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.sign_out) {
            auth.signOut();
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}