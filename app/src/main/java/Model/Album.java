package Model;

import android.widget.ArrayAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 42L;


    public String name;
    public ArrayList<Photo> allPhotos;
    public int currPhotoIdx;
    public Photo currPhoto = null;


    public Album(String name) {
        this.name = name;
        allPhotos = new ArrayList<Photo>();
    }


    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return this.name;
    }

    public ArrayList<Photo> getPhotos(){

        return allPhotos;
    }

    public void addPhoto(Photo photo) {

        allPhotos.add(photo);
    }

    public void addPhoto(String path ){
        Photo photo = new Photo(path);
        allPhotos.add(photo);

    }

    public void remove(int index) {
        allPhotos.remove(index);

    }

    public Photo getPhoto() {

        return allPhotos.get(currPhotoIdx);
    }

    public void setCurrentPhoto(Photo currentPhoto) {

        this.currPhoto = currentPhoto;
    }


    public String toString() {
        if(name == null) {
            return "There are no albums";
        }
        return name;
    }



    public boolean equals(Album other) {

        return this.name.equals(other.name);
    }





}