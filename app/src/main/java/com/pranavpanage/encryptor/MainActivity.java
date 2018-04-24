package com.pranavpanage.encryptor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    String SharedText;
    Boolean encodeMode =false,decodeMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Handle Incoming intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                //textview.setText( intent.getStringExtra(Intent.EXTRA_TEXT));

                //Copy data into String to make it accesible to fragment
                SharedText = intent.getStringExtra(intent.EXTRA_TEXT);

                //alert dialogue to choose between Encoding and decoding
                CharSequence colors[] = new CharSequence[] {"Encode", "Decode"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select action for Shared Data");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which==0){ //Endoed mode selected for Shared Text
                            encodeMode = true;

                            mViewPager.setCurrentItem(0);
                        }

                        if(which == 1){ //Decode Mode
                            decodeMode =true;

                            mViewPager.setCurrentItem(1);
                        }

                    }
                });
                builder.show();


                // Toast.makeText(this,"Shared String" + intent.getStringExtra(intent.EXTRA_TEXT),Toast.LENGTH_LONG).show();
            }
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);








    }

    public String getSharedData(){
        return SharedText;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter= new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new EncryptFragment(),"Encode");
        adapter.addFragment(new DecryptFragment(),"Decode");


        viewPager.setAdapter(adapter);
    }




}
