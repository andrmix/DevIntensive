package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Profile;
import com.softdesign.devintensive.data.storage.models.ProfileDTO;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageHelper;
import com.softdesign.devintensive.utils.operations.LoadMeFromDb;
import com.softdesign.devintensive.utils.operations.LoadUsersFromDb;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;

    private MenuItem mSearchItem;
    private String mQuery;

    private Handler mHandler;

    private ImageView mAvatar;
    private NavigationView mNavigationView;
    private TextView mUserName, mUserEmail;

    private MenuItem mItemTeam;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        showProgress();
        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHandler = new Handler();

        Log.e("onCreate UserListA", "runOperation");
        runOperation(new LoadUsersFromDb());

        setupToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsersFromDb(List<User> userList) {
        if (userList.size() == 0) {
            showSnackbar("Список пользователей не может быть загружен");
            Log.e("ERR_FLAG", "ERROR loadUsersFromDb");
            hideProgress();
        } else {
            Log.e("loadUsersFromDb", "to showUsers");
            showUsers(userList);
        }
    }

    private void setupDrawer(final Profile user) {
        //при нажатии на элемент списка в Drawer'е выводится Snackbar с текстом элемента
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile_menu:
                        doGotoMainActivity(user);
                    case R.id.exit_menu:
                        System.exit(0);
                }

                //закрытие Drawer'а
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mItemTeam = mNavigationView.getMenu().findItem(R.id.team_menu);
        mItemTeam.setChecked(true);

        //скругление аватара в Drawer'е
        mAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.avatar);

        Picasso.with(this).load(user.getAvatar())
                .transform(new ImageHelper())
                .into(mAvatar);

        mUserName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        mUserEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_txt);

        mUserName.setText(user.getFullName());
        mUserEmail.setText(user.getEmail());
    }

    private void doGotoMainActivity(Profile user) {
        ProfileDTO profileDTO = new ProfileDTO(user);
        Intent intent = new Intent(UserListActivity.this, MainActivity.class);
        intent.putExtra(ConstantManager.INTENT_MAIN_KEY, profileDTO);
        startActivity(intent);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        mSearchItem = menu.findItem(R.id.menu_search);

        android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint("Введите имя пользователя");
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showUserByQuery(newText);
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void showUsers(List<User> users) {
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });

        mRecyclerView.swapAdapter(mUsersAdapter, false);

        Log.e("showUsers", "to hideProgress");
        hideProgress();
    }

    private void showUserByQuery(String query) {
        mQuery = query;

        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };

        mHandler.removeCallbacks(searchUsers);

        if (mQuery.isEmpty() || mQuery == "") {
            mHandler.postDelayed(searchUsers, ConstantManager.NO_SEARCH_DELAY);
        } else {
            mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
        }
    }

    public void onOperationFinished(final LoadUsersFromDb.Result result) {
        Log.e("onOperationFinished", "LoadUsersFromDb - in");
        if (result.isSuccessful()) {
            Log.e("onOperationFinished", "LoadUsersFromDb - isSuccessful");
            loadUsersFromDb(result.getOutput());
            runOperation(new LoadMeFromDb(mDataManager.getPreferencesManager().getUserId()));
        } else {
            Log.e("LoadChronos","ErrLoadChronos");
        }
    }

    public void onOperationFinished(final LoadMeFromDb.Result result) {
        Log.e("onOperationFinished", "LoadMeFromDb - in");
        if (result.isSuccessful()) {
            Log.e("onOperationFinished", "LoadMeFromDb - isSuccessful");
            setupDrawer(result.getOutput());
        } else {
            Log.e("LoadChronos","ErrLoadChronos");
        }
    }

}
