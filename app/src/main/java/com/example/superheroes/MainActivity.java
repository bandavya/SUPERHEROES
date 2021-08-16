package com.example.superheroes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HeroAdapter.OnItemClickListener {
    public static final String E_imageUrl = "imageUrl";
    public static final String E_Name = "Name";
    public static final String E_ID = "ID";

    private DrawerLayout drawer;
    private RecyclerView sRecyclerView;
    private HeroAdapter sAdapter;
    private ArrayList<HeroItem> sList;
    private RequestQueue sRequestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sRecyclerView = findViewById(R.id.RecylerView);
        sRecyclerView.setHasFixedSize(true);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sList = new ArrayList<>();
        sRequestQueue = Volley.newRequestQueue(this);
        parseJSON();



        //Navigation Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_female:
                  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FemaleFragment()).commit();
                break;

            case R.id.nav_male:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MaleFragment()).commit();
                break;

            case R.id.nav_home:
                finish();
                startActivity(getIntent());
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void parseJSON() {
        String URL = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/all.json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject a = jsonArray.getJSONObject(i);
                        String Name = a.getString("name");
                        JSONObject images = a.getJSONObject("images");
                        String imageUrl = images.getString("md");
                        int ID = a.getInt("id");

                        sList.add(new HeroItem(imageUrl, Name, ID));

                    }

                    sAdapter = new HeroAdapter(MainActivity.this, sList);
                    sRecyclerView.setAdapter(sAdapter);
                    sAdapter.setOnItemClickListener(MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        sRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        HeroItem clickedItem = sList.get(position);
        detailIntent.putExtra(E_imageUrl, clickedItem.getImageUrl());
        detailIntent.putExtra(E_Name, clickedItem.getName());
        detailIntent.putExtra(E_ID, clickedItem.getID());
        startActivity(detailIntent);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
