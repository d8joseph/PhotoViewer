package com.example.joseph.fotag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Joseph on 2016-04-03.
 */
public class image{
    Bitmap image;
    Bitmap preview;
    String fileName;
    float rating;
    int id;
    int uid;
    Context c;
    boolean type;

    image(int id,String fileName,int uid,Context c){

        this.id = id;
        this.uid = uid;
        this.fileName = fileName;
        this.c = c;
        this.type = Boolean.TRUE;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            this.image = BitmapFactory.decodeResource(c.getResources(), id,options);
            this.preview = Bitmap.createScaledBitmap(image, 100, 100, true);
        }catch(OutOfMemoryError error){
            error.printStackTrace();


        }
        rating =0;
    }
    image(Bitmap image,String src, int uid){
        this.id = -1;
        this.uid = uid;
        this.image = image;
        this.fileName = src.substring(src.lastIndexOf("/") + 1);
        this.preview = Bitmap.createScaledBitmap(image, 100, 100, true);
        this.rating = 0;

    }
    Bitmap getImage(){return image;}
    Bitmap getPreview(){
       return preview;}
    void setRating(float rating){
        this.rating = rating;
    }
    Boolean getType(){
        return type;
    }

    float getRating(){
        return  rating;
    }
    int getSource(){
        return id;
    }
    int getUid(){
        return uid;
    }
    String getFileName(){
        return fileName;
    }
}