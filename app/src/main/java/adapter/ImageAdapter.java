package adapter;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.androidphotos42.MainActivity;
import com.example.androidphotos42.R;

import java.util.ArrayList;

import Model.Photo;

public class ImageAdapter  extends BaseAdapter {

    private Context context;
    public ArrayList<Photo> allPhotos = MainActivity.driver.getCurrentAlbum().allPhotos;

    public ImageAdapter(Context context, ArrayList<Photo>allPhotos){
        System.out.println("test "+ allPhotos);
        this.context = context;
        this.allPhotos = allPhotos;
    }

    @Override
    public int getCount() {

        return allPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return allPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {

            gridView = new View(context);
            gridView = inflater.inflate(R.layout.content_album, null);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageview);
            Uri uri = Uri.parse(allPhotos.get(position).getPhotoFilePath());
            System.out.println(position);
            imageView.setImageURI(uri);
        } else {
            gridView = (View) convertView;
        }
        return gridView;

    }

}

//package adapter;
//
//        import android.content.Context;
//        import android.net.Uri;
//        import android.os.RemoteException;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.BaseAdapter;
//        import android.widget.ImageView;
//
//        import com.example.androidphotos42.MainActivity;
//        import com.example.androidphotos42.R;
//
//        import java.util.ArrayList;
//
//        import Model.Photo;
//
//public class ImageAdapter  extends BaseAdapter {
//
//    private Context context;
//    public ArrayList<Photo> allPhotos = MainActivity.driver.getCurrentAlbum().allPhotos;
//
//    public ImageAdapter(Context context, ArrayList<Photo>allPhotos){
//        System.out.println("test "+ allPhotos);
//        this.context = context;
//        this.allPhotos = allPhotos;
//    }
//
//    @Override
//    public int getCount() {
//
//        return allPhotos.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return allPhotos.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View gridView;
//
//        Photo photo = (Photo) getItem(position);
//
//        if (convertView == null) {
//
//            //  gridView = new View(context);
//            // gridView = inflater.inflate(R.layout.content_album, null);
//            convertView = (View) inflater.inflate(R.layout.content_album, parent,false);
//
//            //Uri uri = Uri.parse(allPhotos.get(position).getPhotoFilePath());
//
//
//        }
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);
//        imageView.setImageBitmap(photo.getImg());
//        return convertView;
//
//    }
//
//}
