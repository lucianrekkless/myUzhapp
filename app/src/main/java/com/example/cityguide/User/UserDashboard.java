package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cityguide.Common.Transport.TransportActivityMain;
import com.example.cityguide.HelperClasses.Adapters.FeaturedAdapter;
import com.example.cityguide.HelperClasses.Adapters.dbAdapter;
import com.example.cityguide.Common.LoginSignup.RetailerStartUpScreen;
import com.example.cityguide.HelperClasses.Adapters.fsAdapter;
import com.example.cityguide.HelperClasses.Models.Place;
import com.example.cityguide.HelperClasses.Models.dbPlace;
import com.example.cityguide.HelperClasses.Models.fsPlace;
import com.example.cityguide.HelperClasses.SessionManager;
import com.example.cityguide.HelperClasses.Adapters.CategoriesAdapter;
import com.example.cityguide.HelperClasses.Models.Category;
import com.example.cityguide.HelperClasses.Adapters.MostViewedAdapter;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables


    dbAdapter  residdbAdapter, entertdbAdapter;
    FeaturedAdapter featuredAdapter;

    fsAdapter foodAdapter;

    private GradientDrawable gradient1, gradient2, gradient3, gradient4, gradient5;
    ImageView menuIcon;
    List<Place> lstPlace;
    ScrollView scrollViewMain;
    RelativeLayout card1, card1x, food_and_drink, card2, card2x, residence, card4, card4x, entertainment;


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference placesRef = firebaseFirestore.collection("Places");


    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);


        //Hooks

        card1 = findViewById(R.id.card1);
        card1x = findViewById(R.id.card1x);
        food_and_drink = findViewById(R.id.food_drink);


        card2 = findViewById(R.id.card2);
        card2x = findViewById(R.id.card2x);
        residence = findViewById(R.id.residence);


        card4 = findViewById(R.id.card4);
        card4x = findViewById(R.id.card4x);
        entertainment = findViewById(R.id.entertainment);

        menuIcon = findViewById(R.id.menu_icon);

        scrollViewMain = findViewById(R.id.main_scroll);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();


        // Recycler Function Calls
        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();


        foodRecycler();
//        residenceRecycler();
//        entertainmentRecycler();

    }

    @Override
    protected void onStart() {
        super.onStart();

        featuredAdapter.startListening();

        foodAdapter.startListening();
//        residdbAdapter.startListening();
//        entertdbAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        featuredAdapter.stopListening();

        foodAdapter.stopListening();
//        residdbAdapter.stopListening();
//        entertdbAdapter.stopListening();
    }


    private void featuredRecycler() {

        RecyclerView featuredRV = (RecyclerView) findViewById(R.id.featured_recycler);
        featuredRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        Query query = placesRef.whereEqualTo("name", "Hodynka");

        FirestoreRecyclerOptions<fsPlace> options =
                new FirestoreRecyclerOptions.Builder<fsPlace>()
                        .setQuery(query, fsPlace.class)
                        .build();

        featuredAdapter = new FeaturedAdapter(this, options);
        featuredRV.setAdapter(featuredAdapter);
        featuredRV.setHasFixedSize(true);

    }


    private void foodRecycler() {

        RecyclerView foodRV = (RecyclerView) findViewById(R.id.food_and_drink_recycler);
        foodRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        Query query = placesRef.whereGreaterThan("category", "residence");

        FirestoreRecyclerOptions<fsPlace> options =
                new FirestoreRecyclerOptions.Builder<fsPlace>()
                        .setQuery(query, fsPlace.class)
                        .build();

        foodAdapter = new fsAdapter(this, options);
        foodRV.setAdapter(foodAdapter);



//        RecyclerView foodRV = (RecyclerView) findViewById(R.id.food_and_drink_recycler);
//        foodRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        FirebaseRecyclerOptions<dbPlace> options =
//                new FirebaseRecyclerOptions.Builder<dbPlace>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places").child("Food and Drink"), dbPlace.class)
//                        .build();
//
//        foodAdapter = new fsAdapter(this, options);
//        foodRV.setAdapter(foodAdapter);
    }


    private void residenceRecycler() {

        RecyclerView residRV = (RecyclerView) findViewById(R.id.residence_recycler);
        residRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<dbPlace> options =
                new FirebaseRecyclerOptions.Builder<dbPlace>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places").child("Residence"), dbPlace.class)
                        .build();

        residdbAdapter = new dbAdapter(this, options);
        residRV.setAdapter(residdbAdapter);

    }

    private void entertainmentRecycler() {

        RecyclerView entertRV = (RecyclerView) findViewById(R.id.entertainment_recycler);
        entertRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<dbPlace> options =
                new FirebaseRecyclerOptions.Builder<dbPlace>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places").child("Entertainment"), dbPlace.class)
                        .build();

        entertdbAdapter = new dbAdapter(this, options);
        entertRV.setAdapter(entertdbAdapter);
    }


    //Navigation drawer Functions
    private void navigationDrawer() {
        //Navigation drawer


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        menuIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
        }


        if (item.getItemId() == R.id.nav_logout) {
            SessionManager logout = new SessionManager(UserDashboard.this, SessionManager.SESSION_USERSLOGIN);
            logout.logoutUserFromSession();
            FirebaseAuth.getInstance().signOut();
//            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//            am.clearApplicationUserData();


            Toast.makeText(UserDashboard.this, "Logout Success ", Toast.LENGTH_SHORT).show();


        }

        return true;

    }




    private void mostViewedRecycler() {

        lstPlace = new ArrayList<>();
        lstPlace.add(new Place("0", "Chicken Hut", R.drawable.chicken_hut_photo, "Yummy fast foods. In uzhhorod city there is no KFC or McDonald... So this this the only fast food chain shop to rely on", "<a href=\"https://goo.gl/maps/zUaAC1J9KnFrCXtbA\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("1", "Hodynka", R.drawable.hodynka_photo, "Amazing selection of beers, good place to chill out and with great service. Also you will have a great view on River and city centre. Prices reasonable, service very good", "<a href=\"https://g.page/Godynka?share\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("2", "Uzhhorod Castle", R.drawable.uzh_castle_photo, "Landmark stone castle housing multiple museums with collections of instruments, clothing & artwork", "<a href=\"https://goo.gl/maps/Y6Q7Vqh8Z1TS1ctZ8\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("3", "Chicken Hut", R.drawable.chicken_hut_photo, "Yummy fast foods. In uzhhorod city there is no KFC or McDonald... So this this the only fast food chain shop to rely on", "<a href=\"https://goo.gl/maps/zUaAC1J9KnFrCXtbA\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("4", "Hodynka", R.drawable.hodynka_photo, "Amazing selection of beers, good place to chill out and with great service. Also you will have a great view on River and city centre. Prices reasonable, service very good", "<a href=\"https://g.page/Godynka?share\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("5", "Uzhhorod Castle", R.drawable.uzh_castle_photo, "Landmark stone castle housing multiple museums with collections of instruments, clothing & artwork", "<a href=\"https://goo.gl/maps/Y6Q7Vqh8Z1TS1ctZ8\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("6", "Chicken Hut", R.drawable.chicken_hut_photo, "Yummy fast foods. In uzhhorod city there is no KFC or McDonald... So this this the only fast food chain shop to rely on", "<a href=\"https://goo.gl/maps/zUaAC1J9KnFrCXtbA\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("7", "Hodynka", R.drawable.hodynka_photo, "Amazing selection of beers, good place to chill out and with great service. Also you will have a great view on River and city centre. Prices reasonable, service very good", "<a href=\"https://g.page/Godynka?share\">Show on Google maps</a>", "0"));
        lstPlace.add(new Place("8", "Uzhhorod Castle", R.drawable.uzh_castle_photo, "Landmark stone castle housing multiple museums with collections of instruments, clothing & artwork", "<a href=\"https://goo.gl/maps/Y6Q7Vqh8Z1TS1ctZ8\">Show on Google maps</a>", "0"));

        RecyclerView mvRV = (RecyclerView) findViewById(R.id.most_viewed_recycler);
        MostViewedAdapter adapter = new MostViewedAdapter(this, lstPlace);
        mvRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mvRV.setAdapter(adapter);
        mvRV.setHasFixedSize(true);


    }

    private void categoriesRecycler() {

        //All Gradients
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xffd4cbe5, 0xfffff});
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff7adccf, 0xfffff});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xfff7c59f, 0xfffff});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xffb8d7f5, 0xfffff});
        gradient5 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xffF1E9CE, 0xfffff});


        ArrayList<Category> Categories = new ArrayList<>();
        Categories.add(new Category(R.drawable.school_image, "Education", gradient1));
        Categories.add(new Category(R.drawable.hospital_image, "HOSPITAL", gradient2));
        Categories.add(new Category(R.drawable.restaurant_image, "Restaurant", gradient3));
        Categories.add(new Category(R.drawable.shopping_image, "Shopping", gradient4));
        Categories.add(new Category(R.drawable.transport_image, "Transport", gradient5));


        RecyclerView categoriesRecycler = (RecyclerView) findViewById(R.id.categories_recycler);

        categoriesRecycler.setHasFixedSize(true);
        CategoriesAdapter adapter = new CategoriesAdapter(Categories);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);

    }

    public void callRetailerScreens(View view) {


        Intent intent = new Intent(getApplicationContext(), RetailerStartUpScreen.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.retailer_startup), "transition_retailer");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(intent, options.toBundle());
    }


    public void callFoodAndDrink(View view) {

        scrollViewMain.setVisibility(View.GONE);

        card1.setBackgroundColor(ContextCompat.getColor(this, R.color.card1));
        card2.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        card4.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        food_and_drink.setVisibility(View.VISIBLE);
        residence.setVisibility(View.GONE);
        entertainment.setVisibility(View.GONE);
        card1x.setVisibility(View.VISIBLE);
        card2x.setVisibility(View.INVISIBLE);
        card4x.setVisibility(View.INVISIBLE);
        card1x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card1x.setVisibility(View.INVISIBLE);
                card1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
                food_and_drink.setVisibility(View.GONE);
                scrollViewMain.setVisibility(View.VISIBLE);
            }
        });

    }


    public void callResidence(View view) {

        scrollViewMain.setVisibility(View.GONE);

        card1.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        card2.setBackgroundColor(ContextCompat.getColor(this, R.color.card2));
        card4.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        food_and_drink.setVisibility(View.GONE);
        residence.setVisibility(View.VISIBLE);
        entertainment.setVisibility(View.GONE);
        card1x.setVisibility(View.INVISIBLE);
        card2x.setVisibility(View.VISIBLE);
        card4x.setVisibility(View.INVISIBLE);
        card2x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card2x.setVisibility(View.INVISIBLE);
                card2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
                residence.setVisibility(View.GONE);
                scrollViewMain.setVisibility(View.VISIBLE);

            }
        });


    }

    public void callTransport(View view) {

        Intent intent = new Intent(getApplicationContext(), TransportActivityMain.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair(findViewById(R.id.card3), "transition_dashboard_transport");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(intent, options.toBundle());

    }


    public void callEntertainment(View view) {

        scrollViewMain.setVisibility(View.GONE);

        card1.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        card2.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        card4.setBackgroundColor(ContextCompat.getColor(this, R.color.card4));
        food_and_drink.setVisibility(View.GONE);
        residence.setVisibility(View.GONE);
        entertainment.setVisibility(View.VISIBLE);
        card1x.setVisibility(View.INVISIBLE);
        card2x.setVisibility(View.INVISIBLE);
        card4x.setVisibility(View.VISIBLE);
        card4x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card4x.setVisibility(View.INVISIBLE);
                card4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
                entertainment.setVisibility(View.GONE);
                scrollViewMain.setVisibility(View.VISIBLE);

            }
        });

    }
}