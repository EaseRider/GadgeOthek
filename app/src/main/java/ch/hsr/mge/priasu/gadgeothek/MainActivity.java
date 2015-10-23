package ch.hsr.mge.priasu.gadgeothek;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReservierungFragment.ReservationListener {

    private LoanFragment loanFragment = null;
    private ReservierungFragment reservFragment = null;
    private BiblioFragment biblioFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load LoanFragment at beginnung.
        loanFragment = new LoanFragment();//(LoanFragment)getSupportFragmentManager().findFragmentById(R.id.loanFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainFrame, loanFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        //create Other Fragments also at beginning
        reservFragment = new ReservierungFragment();
        biblioFragment = new BiblioFragment();

    }

    private Fragment recreateFragment(Fragment f)
    {
        try {
            Fragment.SavedState savedState = getSupportFragmentManager().saveFragmentInstanceState(f);

            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        }
        catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // No need for Options-Menu
        //getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Disable +-Buton in all Views, Enable, where needed
        if (id == R.id.nav_ausleihe) {
            transaction.replace(R.id.mainFrame, loanFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_reservierung) {
            transaction.replace(R.id.mainFrame, reservFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_biblio) {
            transaction.replace(R.id.mainFrame, biblioFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_options) {

        } else if (id == R.id.nav_logout){
            LibraryService.logout(new Callback<Boolean>() {
                                      @Override
                                      public void onCompletion(Boolean input) {
                                          if (input) {
                                              Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                              startActivity(intent);
                                          } else {

                                          }
                                      }

                                      @Override
                                      public void onError(String message) {
                                      // TODO Show Error.. (Server unrechable etc.)

                                      }
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddReservation() {
        
    }

    @Override
    public void onEditReservation(Reservation reservation) {

    }

    public void onEditReservationClose() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, reservFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
