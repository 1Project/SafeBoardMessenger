package stoliarov.me.myapplication;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity{
    private final Executor mWorker = Executors.newSingleThreadExecutor();
    private Messengerq messengerq = Messengerq.getMessengerq();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Snackbar.make(view, messengerq.usersList().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void login(View view) {
        try {
            messengerq.login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userslist(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        ArrayList<String> usersList = null;
        try {
            usersList = messengerq.usersList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putStringArrayListExtra("users", usersList);

        startActivity(intent);
    }

    public void disconnect(View view) {
        try {
            messengerq.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {
        try {
            messengerq.send("poo@safeboard", "test message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
