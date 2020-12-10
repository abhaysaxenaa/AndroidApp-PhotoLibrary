package com.example.androidphotos42;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import Model.*;
import adapter.ImageAdapter;

public class AlbumActivity extends AppCompatActivity {

    private Button deletePhotoButton, movePhotoButton, openPhotoButton;
    private FloatingActionButton addPhoto;

    public  ArrayList<Photo> allPhotos = new ArrayList<Photo>();
    public GridView gridView;
    public ImageAdapter imageAdapter;
    public final int REQUEST_CODE = 1;
    public AlbumList albumList= MainActivity.driver;
    public Album album;
    static AlbumList driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        gridView = (GridView) findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this, albumList.getCurrentAlbum().allPhotos);
        gridView.setAdapter(imageAdapter);

        deletePhotoButton = (Button) findViewById(R.id.deletePhotoButton);
        movePhotoButton = (Button) findViewById(R.id.movePhotoButton);
        openPhotoButton = (Button) findViewById(R.id.openPhotoButton);

        populate();
        //  deletePhotoButton.setVisibility(View.INVISIBLE);
        //  movePhotoButton.setVisibility(View.INVISIBLE);
        // openPhotoButton.setVisibility(View.INVISIBLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  deletePhotoButton.setVisibility(View.VISIBLE);
                //  movePhotoButton.setVisibility(View.VISIBLE);
                // openPhotoButton.setVisibility(View.VISIBLE);

                deletePhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePhoto(position);
                    }
                });

                movePhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePhoto(position);
                    }
                });

                openPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPhoto(position);
                    }
                });
            }
        });

        FloatingActionButton add = findViewById(R.id.addPhoto);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
            public void addPhoto() {
                System.out.println("it is selectingggg-------------------------------");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to add this photo?");
        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(requestCode == REQUEST_CODE ){

                            Uri photoPath = null;

//            String photoPath = null;
//            System.out.println("here");
                            if(data != null){
                                photoPath = data.getData();
                            }
                            String photoPathString = photoPath.toString();
                            MainActivity.driver.getCurrentAlbum().addPhoto(photoPathString);

                            try{
                                AlbumList.save(MainActivity.driver);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            gridView =(GridView) findViewById(R.id.gridView);
                            populate();
                            imageAdapter.allPhotos = MainActivity.driver.getCurrentAlbum().allPhotos;
                            imageAdapter.notifyDataSetChanged();

                            //MAJOR CHANGE Fixes the different photos being displayed on the GridView:
                            gridView.setAdapter(imageAdapter);

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

    }


    public void deletePhoto(int index){
        imageAdapter = (ImageAdapter)gridView.getAdapter();
        if (imageAdapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("There are no photos to delete.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // int index = gridView.getCheckedItemPosition();
        Photo currentPhoto = (Photo) imageAdapter.getItem(index);
        dialogBuilder.setMessage("Are you sure you want to delete?");
        dialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.driver.getCurrentAlbum().remove(index);
                        allPhotos.remove(currentPhoto);
                        gridView.setItemChecked(index, true);

                        try{

                            AlbumList.save(MainActivity.driver);

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
        dialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        gridView =(GridView) findViewById(R.id.gridView);
        populate();
        imageAdapter.notifyDataSetChanged();
        gridView.setAdapter(imageAdapter);
        dialogBuilder.show();
        // deletePhotoButton.setVisibility(View.INVISIBLE);

    }


    public void movePhoto(int index){
        imageAdapter = (ImageAdapter)gridView.getAdapter();
        if (imageAdapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("There are no photos.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // int index = gridView.getCheckedItemPosition();
        Photo currentPhoto = (Photo) imageAdapter.getItem(index);
        dialogBuilder.setMessage("Enter the Name of the Album to move?");
        final EditText input = new EditText(this);
        dialogBuilder.setView(input);

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        dialogBuilder.setPositiveButton("Move", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String albumNameToMove = input.getText().toString().trim();
                boolean albumCheck = false;

                if(albumNameToMove.isEmpty() || albumNameToMove == null) {
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage("Album name cannot be empty").setPositiveButton("OK", null).show();
                    return;
                }
                int index =0;
                for(int i = 0; i < MainActivity.driver.allAlbums.size(); i++){
                    Album album = MainActivity.driver.allAlbums.get(i);
                    if(album.getName().equals(albumNameToMove)){
                        index = i;
                        albumCheck = !albumCheck;
                    }
                }
                if(albumCheck){
                    MainActivity.driver.allAlbums.get(index).addPhoto((Photo) gridView.getSelectedItem());
                    MainActivity.driver.getCurrentAlbum().remove(gridView.getCheckedItemPosition());
                }
                else{
                    new AlertDialog.Builder(dialogBuilder.getContext()).setMessage("Album name is invalid").setPositiveButton("OK", null).show();
                    return;
                }

            }
        });
        gridView =(GridView) findViewById(R.id.gridView);
        populate();
        imageAdapter.notifyDataSetChanged();
        gridView.setAdapter(imageAdapter);
        dialogBuilder.show();

    }

    public void openPhoto(int index){
        if (imageAdapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("There are no photos to open.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }
        Photo photo = MainActivity.driver.getCurrentAlbum().getPhotos().get(index);
        MainActivity.driver.getCurrentAlbum().setCurrentPhoto(photo);
        Intent intent = new Intent(this, singlePhotoDisplay.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }



    public void populate(){
        allPhotos.clear();
        allPhotos.addAll(MainActivity.driver.getCurrentAlbum().getPhotos());
    }
}