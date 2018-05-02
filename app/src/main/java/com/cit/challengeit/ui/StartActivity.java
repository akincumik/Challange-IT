package com.cit.challengeit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cit.challengeit.R;
import com.cit.challengeit.network.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.tv_start_code)
    TextView mTvCode;
    @BindView(R.id.rv_start_joiners)
    RecyclerView mRvJoiners;

    private String mRoomCode;

    public static void start(Context context) {
        Intent intent = new Intent(context, StartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        mRoomCode = "ABC123";
        mTvCode.setText(mRoomCode);

        mRvJoiners.setHasFixedSize(true);
        mRvJoiners.setLayoutManager(new LinearLayoutManager(this));
        mRvJoiners.setAdapter(newUserAdapter());
    }

    protected RecyclerView.Adapter newUserAdapter() {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference()
                .child("rooms").child(mRoomCode).child("users");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User user) {
                holder.tvName.setText(user.name);
            }

            @Override
            public void onDataChanged() {
//                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    @OnClick(R.id.btn_start_start)
       public void onStartClicked() {}

    public static class UserHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_user_name)
        TextView tvName;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
