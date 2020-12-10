package com.example.androidphotos42;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import Model.Album;
import Model.Photo;
import Model.Tag;
import adapter.ImageAdapter;

public class SearchPhoto extends AppCompatActivity {

    private Button searchTag, addTag;
    private EditText tagName, tagValue;
    private GridView gridView;
    private TextView tagsEntered;

    private ArrayList<Photo> searchPhotos = new ArrayList<Photo>();

    private ArrayList<Tag> tagSearch = new ArrayList<Tag>();

    //private ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        searchTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchPhotoTags(tagSearch);
            }
        });

        addTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addPhotoTags();
            }
        });

        searchTag = (Button) findViewById(R.id.searchButton);
        addTag = (Button) findViewById(R.id.addButton);
        tagName = (EditText) findViewById(R.id.tagName);
        tagValue = (EditText) findViewById(R.id.tagValue);
        tagsEntered = (TextView) findViewById(R.id.tagsEntered);
        addTag.setVisibility(View.VISIBLE);
        searchTag.setVisibility(View.VISIBLE);

        gridView = (GridView) findViewById(R.id.gridView);
        //imageAdapter = new ImageAdapter(this, searchPhotos);
        //gridView.setAdapter(imageAdapter);
    }

    private void searchPhotoTags(ArrayList<Tag> tagSearch){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        if (tagSearch.size() == 0){
            new AlertDialog.Builder(dialogBuilder.getContext())
                    .setMessage("Tag List is empty.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        //HashSet to avoid duplicating Photos.
        HashSet<Photo> photoSet = new HashSet<Photo>();

        for (Album album : MainActivity.driver.getList()) {
            for (Photo photo : album.getPhotos()) {
                for (int i = 0; i < photo.getTags().size(); i++){
                    for (int j = 0; j < tagSearch.size(); j++){
                        if (photo.getTags().get(i).getValue().toLowerCase().contains(tagSearch.get(j).value.toLowerCase()));
                        photoSet.add(photo);
                    }
                }
            }
        }
        searchPhotos.addAll(photoSet);

        gridView = (GridView) findViewById(R.id.gridView);
        //imageAdapter.notifyDataSetChanged();
        //gridView.setAdapter(imageAdapter);
    }


    private void addPhotoTags() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        String tagname = tagName.getText().toString();
        String tagvalue = tagValue.getText().toString();

        //Only allows Person and Location
        if (tagname != "Person" || tagname != "Location"){
            new AlertDialog.Builder(dialogBuilder.getContext())
                    .setMessage("Please enter either 'Person' or 'Location' for Tag Name.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        } else if (tagname.isEmpty() || tagvalue.isEmpty()){
            new AlertDialog.Builder(dialogBuilder.getContext())
                    .setMessage("Please enter a valid Tag.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        tagsEntered.setText("Tag Type: " + tagname + ", Tag Value: " + tagvalue);
        tagValue.setText("");
        tagName.setText("");
        Tag newTag = new Tag(tagname, tagvalue);
        tagSearch.add(newTag);
    }
}