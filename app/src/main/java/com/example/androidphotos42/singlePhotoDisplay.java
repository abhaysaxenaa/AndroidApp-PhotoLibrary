package com.example.androidphotos42;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.androidphotos42.MainActivity;
import com.example.androidphotos42.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import Model.AlbumList;
import Model.Photo;
import Model.Tag;

public class singlePhotoDisplay extends AppCompatActivity {
    public ImageView imageView;
    public Button tagButton, addTag, deleteTag;
    public ListView listView;
    public TextView filename;
    public EditText tagName, tagValue;

    //Array Lists
    public static ArrayList<Photo> allPhotos = new ArrayList<Photo>();
    public ArrayList<Tag> allTags = new ArrayList<Tag>();
    public Photo photo;

    //Adapter
    public ArrayAdapter<Tag> adapter;
    //private ArrayAdapter<String> tagsAdapter;

    //Indices
    public int currIndex=0;
    public int photoIndex;
    public int previousIndex;
    public int nextIndex = 0;

    public AlbumList driver = MainActivity.driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo_display);

        //MAJOR CHANGE which removed the null pointer exception:
        listView = (ListView) findViewById(R.id.listView);

        adapter = (ArrayAdapter<Tag>) listView.getAdapter();

        imageView = (ImageView) findViewById(R.id.imageView);

        //TBD: Set name at the top.
        filename = (TextView) findViewById(R.id.filename);
        filename.setText(MainActivity.driver.getCurrentAlbum().getPhoto().photoname);

        tagName =(EditText)findViewById(R.id.tagName);
        tagValue =(EditText) findViewById(R.id.tagValue);

        //TBD: Displays first image. (Meant to)
        if (MainActivity.driver.getCurrentAlbum().getPhotos().size() != 0) {
            spinUpDisplay();
        }

        //Not sure if this is the correct way to display first picture.
//        Photo photo = MainActivity.driver.getCurrentAlbum().getPhoto();
//        this.photo = photo;
//        Intent intent = getIntent();
//        int index = intent.getIntExtra("index", -1);
//        Bundle b = intent.getExtras();
//        if(b!=null) {
//            Uri uri = Uri.parse(MainActivity.driver.getCurrentAlbum().getPhotos().get(index).getPhotoFilePath());
//            imageView.setImageURI(uri);
//
//            // displayTags();
//        }
//        else{
//
//        }

    }



    public void nextPhoto(View view) {
        //Populate temporary Photo List.
        populatePhotoList();

        int previousIndex = allPhotos.size()-1;
        if(currIndex + 1 > allPhotos.size()-1) {
            return;
        } else {
            currIndex++;
            Photo photo = allPhotos.get(currIndex);
            if(photo != null) {
                Uri uri = Uri.parse(MainActivity.driver.getCurrentAlbum().getPhotos().get(currIndex).getPhotoFilePath());
                imageView.setImageURI(uri);
                MainActivity.driver.getCurrentAlbum().setCurrentPhoto(photo);
                displayTags();

                //Display tags
//                listView = (ListView) findViewById(R.id.listView);
//                allTags.clear();
//                allTags.addAll(MainActivity.driver.getCurrentAlbum().getPhoto().getTags());
//                tagsAdapter.notifyDataSetChanged();
//                listView.setAdapter(tagsAdapter);


            }
        }
    }



    public void previousPhoto(View view) {
        populatePhotoList();
        if(currIndex-1 < 0) {
            return;
        }
        if(currIndex == -1){
            currIndex = allPhotos.size()-1;
        }
        else {
            currIndex--;
            Photo photo = allPhotos.get(currIndex);
            if(photo != null) {
                Uri uri = Uri.parse(MainActivity.driver.getCurrentAlbum().getPhotos().get(currIndex).getPhotoFilePath());
                imageView.setImageURI(uri);
                MainActivity.driver.getCurrentAlbum().setCurrentPhoto(photo);
                displayTags();

            }
        }
    }



    public void displayTags() {
        photo = MainActivity.driver.getCurrentAlbum().getPhoto();

        if (photo != null) {
            ArrayList<Tag> temp = MainActivity.driver.getCurrentAlbum().getPhoto().getTags();

            //TBD: Set up adapter instead
            adapter = new ArrayAdapter<>(this, R.layout.activity_single_photo_display, temp);
            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }

    }




    public void populatePhotoList(){
        allPhotos.clear();
        for (int i = 0; i < MainActivity.driver.getCurrentAlbum().getPhotos().size(); i++){
            allPhotos.add(MainActivity.driver.getCurrentAlbum().getPhotos().get(i));
        }
    }




    public void spinUpDisplay(){
        Uri uri = Uri.parse(MainActivity.driver.getCurrentAlbum().getPhotos().get(0).getPhotoFilePath());
        imageView.setImageURI(uri);
    }




    public void addTags(View view) {

        adapter = (ArrayAdapter<Tag>) listView.getAdapter();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);


        String tagname = tagName.getText().toString();
        String tagvalue = tagValue.getText().toString();
        Tag tag = (tagname.isEmpty() || tagvalue.isEmpty()) ? null : new Tag(tagname, tagvalue);

        //If Tag already exists.
        if (checkTags(tag)) {
            new AlertDialog.Builder(dialogBuilder.getContext())
                    .setMessage("This tag already exists.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        //Only allows Person and Location
        if (tagname != "Person" || tagname != "Location"){
            new AlertDialog.Builder(dialogBuilder.getContext())
                    .setMessage("Please enter 'Person' or 'Location' for Tag Name.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        //Update Tag List
        adapter.add(tag);
        //adapter.notifyDataSetChanged();
        update();
        dialogBuilder.show();
        listView.setAdapter(adapter);
    }



    public void deleteTags(View view) {
        final int checkedItemPosition = listView.getCheckedItemPosition();
        final Tag checkedTag = adapter.getItem(checkedItemPosition);

        ArrayList<Tag> allTags = MainActivity.driver.getCurrentAlbum().getPhoto().getTags();
        Tag tag = allTags.get(checkedItemPosition);
        MainActivity.driver.getCurrentAlbum().getPhoto().deleteTag(tag.getType(), tag.getValue());

        //Update Tag List
        adapter.remove(tag);
        //adapter.notifyDataSetChanged();
        update();
        //listView.setAdapter(adapter);
    }




    public boolean checkTags(Tag tag){
        for (int i = 0; i < driver.getCurrentAlbum().currPhoto.getTags().size(); i++){
            if (tag.equals(allTags.get(i))){
                return false;
            }
        }
        return true;
    }

    public void update(){
        allTags.clear();
        for(int i = 0; i < MainActivity.driver.getCurrentAlbum().getPhoto().getTags().size(); i++){
            allTags.add(MainActivity.driver.getCurrentAlbum().getPhoto().getTags().get(i));
        }
    }
}