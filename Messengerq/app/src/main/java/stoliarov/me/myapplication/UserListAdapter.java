package stoliarov.me.myapplication;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VladS on 1/29/2016.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private final List<String> usersList = new ArrayList<>();

    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.UserViewHolder holder, int position) {
        holder.setUser(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setItems(List<String> usersList) {
        this.usersList.clear();
        this.usersList.addAll(usersList);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView userId;

        @Override
        public void onClick(View v) {
            try {
                Messengerq.getMessengerq().send("cat@jazz.io", "asdad");
                System.out.println("clicked");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public UserViewHolder(View userView) {
            super(userView);
            userId = (TextView) userView.findViewById(R.id.user_id);
        }

        void setUser(String user) {
            userId.setText(user);
        }
    }
}
