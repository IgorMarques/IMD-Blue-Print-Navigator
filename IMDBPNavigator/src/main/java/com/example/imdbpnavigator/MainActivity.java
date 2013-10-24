package com.example.imdbpnavigator;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import photoview.*;


public class MainActivity extends Activity {

    boolean isOpen = false;
    String item = null;

    int[] myImageList = new int[]{R.drawable.pic_3, R.drawable.pic_4};

    float scale=0;
    float centerX=0;
    float centerY =0;

    RectF rect;

    PhotoViewAttacher mAttacher;

    public void openListViewItem(){
        Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

        int i = 0;

       ImageView imgView;
       imgView = (ImageView) findViewById(R.id.imageView);

        imgView.setImageResource(myImageList[i]);

        mAttacher = new PhotoViewAttacher(imgView);

        mAttacher.setMaxScale(10);
        mAttacher.setMidScale(5);

        int centerX = (imgView.getLeft() + imgView.getRight())/2;
        int centerY = (imgView.getTop() + imgView.getBottom())/2;

        mAttacher.zoomTo(4,  mAttacher.getDisplayRect().centerX(),  mAttacher.getDisplayRect().centerY());

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

                ImageView myImageView = mAttacher.getImageView();

                myImageView.setDrawingCacheEnabled(true);

                myImageView.buildDrawingCache();

                myImageView.setImageBitmap(createImage(myImageView, x, y));

                Toast.makeText(getBaseContext(), "Tap " + x + " " + y, Toast.LENGTH_SHORT).show();


            }
        });
        isOpen = true;

    }


    public Bitmap createImage(ImageView myImageView, float touchX, float touchY){
        Bitmap workingBitmap =  ((BitmapDrawable)myImageView.getDrawable()).getBitmap();
        Bitmap image = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);


        float radius = touchX*100000;

       // touchX=touchX- image.getWidth() / 2;
        //touchY=touchY- image.getHeight() / 2;

        canvas.drawCircle(touchX, touchY, radius, paint);
        System.out.println("Drew a circle at "+touchX+" " + touchY+" with a radius of "+radius+".");
        return image;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        ListView listView1 = (ListView) findViewById(R.id.listView1);

        Blueprint[] items = {
                new Blueprint(1, "Primeiro pavimento", "-"),
                new Blueprint(2, "Segundo pavimento", "-"),
        };

        ArrayAdapter<Blueprint> adapter = new ArrayAdapter<Blueprint>(this,
                android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);

        if (savedInstanceState  == null){

            listView1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                item = ((TextView)view).getText().toString();

                 openListViewItem();
                }
            });
        }
        else{

            item = savedInstanceState.getString("openedItem");
            scale= savedInstanceState.getFloat("scale");
            centerX = savedInstanceState.getFloat("x");
            centerY = savedInstanceState.getFloat("y");


            if(item != null){

                openListViewItem();

                if(scale!=0){
                    mAttacher.zoomTo(scale, centerX, centerY);
                    mAttacher.update();
                }

            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        scale = mAttacher.getScale();
        rect = mAttacher.getDisplayRect();

        outState.putString("openedItem", item);
        outState.putFloat("scale", scale);
        outState.putFloat("x", rect.centerX());
        outState.putFloat("y", rect.centerY());

        super.onSaveInstanceState(outState);
    }

    public void onBackPressed(){

        if (isOpen){
            finish();
            startActivity(getIntent());
        }
        else{
            isOpen= false;
            item = null;
            scale=0;
            centerX=0;
            centerY=0;
            super.onBackPressed();
        }

    }
}
