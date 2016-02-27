package stoliarov.me.myapplication;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserListActivity extends AppCompatActivity{
    private Messengerq messengerq = Messengerq.getMessengerq();
    private static final int ITEMS_LOADER_ID = 1;
    private final UserListAdapter userListAdapter = new UserListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    Messengerq.getMessengerq().send("qweqweqwe@safeboard", "test msg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> usersList = getIntent().getStringArrayListExtra("users");

//        ArrayList<String> usersList = null;
//        try {
//            usersList = messengerq.usersList();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        LinearLayout lView = new LinearLayout(this);
//        TextView myText= new TextView(this);
//        lView.addView(myText);
//        for (int i=0; i<usersList.size();i++){
//            myText.append(usersList.get(i));
//            myText.append("\n");
//        }
//        setContentView(lView);

        RecyclerView listView = (RecyclerView) findViewById(R.id.user_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(userListAdapter);
        userListAdapter.setItems(usersList);
    }
}
