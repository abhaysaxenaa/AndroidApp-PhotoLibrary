package com.example.androidphotos42;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Model.Album;
import Model.AlbumList;

public class MainActivity extends AppCompatActivity {

    Button openAlbumButton, renameAlbumButton, deleteAlbumButton;

    public ListView listView;

    public ArrayAdapter adapter;
    public ArrayList<Album> allAlbums = new ArrayList<Album>();
    //AlbumList albumList;
    public static AlbumList driver = new AlbumList();
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("no file");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        openAlbumButton = (Button) findViewById(R.id.openAlbumButton);
        openAlbumButton.setVisibility(View.INVISIBLE);
        renameAlbumButton = (Button) findViewById(R.id.renameAlbumButton);
        renameAlbumButton.setVisibility(View.INVISIBLE);
        deleteAlbumButton = (Button) findViewById(R.id.deleteAlbumButton);
        deleteAlbumButton.setVisibility(View.INVISIBLE);
//
        file = new File("/data/data/com.example.androidphotos42/files/photoGallery1.dat");
        if (!file.exists() || !file.isFile()) {

            try {
                file.createNewFile();
                allAlbums = new ArrayList<Album>();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        Album testAlbum = new Album("Test");
//        driver.addAlbum(testAlbum);
//        allAlbums.add(testAlbum);

        try{
            driver = AlbumList.load();
            populateList();
        }catch(Exception e){
            e.printStackTrace();
        }

            adapter = new ArrayAdapter<>(this, R.layout.content_main, driver.getList());
            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openAlbumButton.setVisibility(View.VISIBLE);
                renameAlbumButton.setVisibility(View.VISIBLE);
                deleteAlbumButton.setVisibility(View.VISIBLE);


                openAlbumButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        try {
                            Album currentAlbum = driver.allAlbums.get(i);
                            driver.setCurrentAlbum(currentAlbum);
                            Intent CurrentAlbum = new Intent(MainActivity.this, AlbumActivity.class);
                            startActivity(CurrentAlbum);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        openAlbumButton.setVisibility(View.INVISIBLE);
                    }
                });



            }
        });

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addAlbum(view);
            }
        });

        FloatingActionButton search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent(MainActivity.this, SearchPhoto.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openAlbum(View view){

    }


    public void addAlbum(View view){
        allAlbums = new ArrayList<Album>();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Enter album name.");
        final EditText input = new EditText(this);
        dialogBuilder.setView(input);

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString().trim();
                Album newAlbum = new Album(albumName);

                if(albumName.isEmpty() || albumName == null) {
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage("Album name cannot be empty").setPositiveButton("OK", null).show();
                    return;
//

                }
                else if(driver.checkAlbumInList(albumName) == true ){
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage( "Album name already exists")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
                else {
                    driver.getList().add(new Album(albumName));
                    //System.out.println(driver);
                    //adapter.add(newAlbum);
                    try{
                        AlbumList.save(driver);
                        populateList();

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });
        dialogBuilder.show();
    }



    public void deleteAlbum(View view){

        adapter = (ArrayAdapter<Album>)listView.getAdapter();
        if (adapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("There are no albums.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int index = listView.getCheckedItemPosition();
        //Album currentAlbum = albumList.getCurrentAlbum();
        System.out.println(index );
        Album currentAlbum = (Album) adapter.getItem(index);

        builder.setMessage("Are you sure you want to delete \"" + currentAlbum.getName() + "\"?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.remove(currentAlbum);
                        driver.getList().remove(currentAlbum);
                        listView.setItemChecked(index, true);

                        try{
                            AlbumList.save(driver);
                            populateList();

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
        renameAlbumButton.setVisibility(View.INVISIBLE);
        deleteAlbumButton.setVisibility(View.INVISIBLE);
        openAlbumButton.setVisibility(View.INVISIBLE);

    }



    public void renameAlbum(View view){

        int index = listView.getCheckedItemPosition();
        Album currentAlbum = (Album) adapter.getItem(index);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Rename");
        dialogBuilder.setMessage("Enter new album name to rename.");
        final EditText input = new EditText(this);
        dialogBuilder.setView(input);

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();

                if(newName.isEmpty() || newName == null) {
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage("Album name cannot be empty").setPositiveButton("OK", null).show();
                    return;
                }
                else if(driver.checkAlbumInList(newName) == true ){
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage( "Album name already exists")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
                else{
                    currentAlbum.setName(newName);
                    driver.getCurrentAlbum().setName(newName);
                    adapter.notifyDataSetChanged();
                    try{
                        AlbumList.save(driver);
                        populateList();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
        renameAlbumButton.setVisibility(View.INVISIBLE);
        deleteAlbumButton.setVisibility(View.INVISIBLE);
        openAlbumButton.setVisibility(View.INVISIBLE);
        dialogBuilder.show();

    }


    private void populateList() {
        allAlbums.clear();

        for(int i = 0; i < driver.getList().size(); i++) {
            allAlbums.add(new Album(driver.getList().get(i).getName()));
        }
    }



    public static AlbumList load() throws IOException, ClassNotFoundException {
        System.out.println("its loading--------------------------------");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/data/data/com.example.androidphotos42/files/photoGallery.dat"));
        AlbumList albumList = (AlbumList) ois.readObject();
        ois.close();
        return albumList;

    }
    public static void save(AlbumList pdApp) throws IOException {

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/data/data/com.example.androidphotos42/files/photoGallery.dat"));
        oos.writeObject(pdApp);
        oos.close();
    }


}