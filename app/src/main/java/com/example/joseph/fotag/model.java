package com.example.joseph.fotag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joseph on 2016-04-02.
 */
public class model extends Observable {

    private float filter;
    boolean showImages;
    private Boolean def;
    ProgressDialog dialog;
    static Integer SerialId;
    ArrayList<Integer> ImageCollection;
    ArrayList<Integer> ImageIds;
    ArrayMap<Integer,image> ImageMap;
    private Context mContext;

    model(Context c){
        super();
        showImages = Boolean.FALSE;
        mContext = c;
        SerialId =0;
        ImageIds = new ArrayList<>();
        ImageMap = new ArrayMap<>();
        ImageCollection = new ArrayList<>();
        filter = 0;
        def = Boolean.FALSE;
    }

    void clear(){
        SerialId = 0;
        def = Boolean.FALSE;
        ImageIds = null;
        ImageCollection= null;
        ImageMap= null;
        ImageIds = new ArrayList<>();
        ImageCollection = new ArrayList<>();
        ImageMap = new ArrayMap<>();
        notifyObservers();

    }

    void addDefaults(){
        if(!def) {
            for (Integer i : mThumbIds
                    ) {
                addImage(i, "Sample" + i);
            }
            def = Boolean.TRUE;
            notifyObservers();
        }
    }

    void show(Boolean V){
        showImages = V;
    }

    void setFilter(float filter){
        this.filter = filter;
        notifyObservers();
    }

    ArrayList<Integer> getImages() {
        ImageIds.clear();
        ImageIds = null;
        ImageIds = new ArrayList<>();

            for (Integer i :
                    ImageCollection) {
                Log.d("Getting Images", "Checking ID : " + ImageMap.get(i).getFileName()+" With Rating "+ImageMap.get(i).getRating()+ " against filter :"+filter);
                if (ImageMap.get(i).getRating() >= filter) {
                    Log.d("AddingImages","Adding to imList ID : "+ImageMap.get(i).getFileName()+" With Rating "+ImageMap.get(i).getRating());
                    ImageIds.add(i);
                }
            }
        return ImageIds;

    }

    float getRating(int SerialId) {
        return ImageMap.get(SerialId).getRating();
    }

    void setRating(int SerialId,float rating){
        ImageMap.get(SerialId).setRating(rating);
        notifyObservers();
    }

    String getName(int SerialId){
        return ImageMap.get(SerialId).getFileName();
    }

    int getSource(int SerialId){
        return ImageMap.get(SerialId).getSource();
    }

    void getImage(int SerialId, ImageView im){

        //bmp = BitmapFactory.decodeResource(mContext.getResources(),ImageMap.get(SerialId).getSource() );
      //  Bitmap bitmap = ImageMap.get(SerialId).getImage();
//        Log.d("GETTING IMGE","Height = "+bitmap.getHeight()+" Width = "+bitmap.getWidth());
        im.setImageBitmap(ImageMap.get(SerialId).getImage());

    }

    void getPreview(int SerialId,ImageView im){
        im.setImageBitmap(ImageMap.get(SerialId).getPreview());
    }

    void addImage(Integer id,String name){
        image temp = new image(id,name,SerialId,mContext);
        ImageCollection.add(SerialId);
        ImageMap.put(SerialId, temp);
        SerialId++;
    }

    void addImageFromUri(String uri){
        Bitmap bmp= null;
        if(uri.substring(0, 4).equalsIgnoreCase("http")||(uri.substring(0,4).equalsIgnoreCase("www."))||(uri.substring(0,3).equalsIgnoreCase("ftp"))){
            try {
                bmp = new GetImageFromUrl().execute(uri).get(10, TimeUnit.SECONDS);
                if(bmp==null){

                    return;
                }
                else{
                    Toast.makeText(mContext, "Image Added!", Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ex.printStackTrace();
                Toast.makeText(mContext, "Download Timed Out!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else{
            try {
                Uri contentUri = Uri.parse("file://" + uri);
                bmp = BitmapLoader(contentUri);
            }catch(Exception ex){ ex.printStackTrace(); return;}

        }

        Log.d("ADDING","Height = "+bmp.getHeight()+" Width = "+bmp.getWidth());
        image temp = new image(bmp,uri,SerialId);
        ImageMap.put(SerialId,temp);
        ImageCollection.add(SerialId);
        SerialId++;

        notifyObservers();

    }

    @Override
    public void addObserver(Observer observer) {
        Log.d("MVC", "Model: Observer added");
        super.addObserver(observer);
        Log.d("MVC", "OB COUNT =" + super.countObservers());
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    @Override
    public void notifyObservers() {

        Log.d("MVC", "Model: Observers notified "+super.countObservers());
        setChanged();
        super.notifyObservers();
        clearChanged();
    }

    @Override
    protected void setChanged() {
        super.setChanged();
    }

    @Override
    protected void clearChanged() {
        super.clearChanged();
    }

    public Bitmap BitmapLoader(Uri uri)  throws FileNotFoundException, IOException {
        int THUMBNAIL_SIZE = 1000;
        InputStream input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig= Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        DisplayMetrics met = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(met);
        //TODO: Scaled fullscreen
        //bitmap =  Bitmap.createScaledBitmap(bitmap,800 ,600, true);
        input.close();
        return bitmap;
    }


    private Integer[] mThumbIds = {
            R.drawable.s1,
           R.drawable.s2,R.drawable.s3,
            R.drawable.s4,R.drawable.s5,
            R.drawable.s6,R.drawable.s7,
            R.drawable.s8, R.drawable.s9,
            R.drawable.s10

    };

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        @Override protected Bitmap doInBackground(String... urls) {
            Bitmap map = null; for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
           dialog.setMessage("Downloading Image");
           dialog.show();
        }
        @Override
        protected void onPostExecute(Bitmap result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
           if(result==null){
               Toast.makeText(mContext, "Download Failed!", Toast.LENGTH_LONG).show();
           }
            else{
                  // result = Bitmap.createScaledBitmap(result, 800, 600, true);

           }

        }
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds=Boolean.TRUE;
            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, options); stream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
           // Log.d("BITMAP","Height = "+bitmap.getHeight()+" Width = "+bitmap.getWidth());
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                { stream = httpConnection.getInputStream();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return stream;
        }
    }

}
