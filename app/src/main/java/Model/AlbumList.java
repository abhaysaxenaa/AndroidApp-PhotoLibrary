package Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumList implements Serializable {
    private  static final long serialVersionUID = 42L;

    public  ArrayList<Album> allAlbums;
    public Album present;
    public int currUserIdx = -1;

    public AlbumList() {

        allAlbums = new ArrayList<Album>();
    }

    public  ArrayList<Album> getList(){
        return allAlbums;
    }

    public  boolean checkAlbumInList(String album) {
        for (int i = 0; i < allAlbums.size(); i++) {
            if (album.equals(allAlbums.get(i).getName())) {
                return true;
            }
        }
        return false;
    }
    public  void addAlbum(Album album) {
        allAlbums.add(album);
    }

    public void deleteAlbum(int idx) {
        allAlbums.remove(idx);
    }

    public Album getAlbum(Album album) {
        for(int i = 0; i < allAlbums.size(); i++) {
            if(allAlbums.get(i).equals(album)) {
                return allAlbums.get(i);
            }
        }
        return null;
    }

    public Album getCurrentAlbum() {
        return present;
        // return allAlbums.get(currUserIdx);
    }
    public void setCurrentAlbum(Album present) {
//        for(int i = 0; i < allAlbums.size(); i++) {
//
//            if(allAlbums.get(i).getName().equals(present.getName())) {
//
//                this.currUserIdx = i;
//            }
//        }

        this.present = present;
    }

    public String toString() {
        if(allAlbums == null) {
            return "There are no albums";
        }
        String result = "";
        for(int i = 0; i < allAlbums.size(); i++) {
            result = result + allAlbums.get(i);
        }
        return result;

    }
    public static void save(AlbumList albumList) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/data/data/com.example.androidphotos42/files/photoGallery1.dat"));
        oos.writeObject(albumList);
        oos.close();
    }
    public static AlbumList load() throws IOException, ClassNotFoundException {
        System.out.println("its loading--------------------------------");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/data/data/com.example.androidphotos42/files/photoGallery1.dat"));
        AlbumList albumList = (AlbumList) ois.readObject();
        ois.close();
        return albumList;

    }


}
