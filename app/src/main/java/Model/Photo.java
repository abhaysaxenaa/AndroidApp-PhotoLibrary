package Model;

import android.media.Image;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

    private static final long serialVersionUID = 42L;

    public String fileName;
    private String caption;
    public File photoLocation;
    public String photoFilePath;
    public String photoname;
    public ArrayList<String> tag_person;
    public ArrayList<String> tag_location;
    // private SerializableImage image;
    private ArrayList<Tag> tags = new ArrayList<>();





    public Photo(String photoFilePath){
        this.photoFilePath = photoFilePath;
        tag_person = new ArrayList<>();
        tag_location = new ArrayList<>();
    }



    public void setName(String name)
    {
        this.photoname = name;
    }



    public void setImg(File photoLocation) {

        this.photoLocation = photoLocation;
    }



    public String getName() {

        return photoname;
    }

    public String getPhotoFilePath(){

        return photoFilePath;
    }

    public String getFileName(){

        return fileName;
    }



//    public Image getImage() {
//        return image.getImage();
//    }


    public ArrayList<Tag> getTags(){
        if(this.tags == null) this.tags = new ArrayList<>();
        return this.tags;
    }



    public void addNewTag(String name, String value) {
        if(this.tags == null) this.tags = new ArrayList<>();
        this.tags.add(new Tag(name, value));
    }


    public void deleteTag(String name, String value) {
        for(int i = 0; i < tags.size(); i++) {
            Tag curr = tags.get(i);
            if(curr.name.toLowerCase().equals(name.toLowerCase()) && curr.value.toLowerCase().equals(value.toLowerCase())) {
                tags.remove(i);
            }
        }
    }





    public File getImg() {

        return this.photoLocation;
    }



    public String toString() {
        if(photoname == null) {
            return "There are no photos";
        }

        return photoname;
    }

}

