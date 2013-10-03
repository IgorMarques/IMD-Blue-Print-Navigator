package com.example.imdbpnavigator;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Matrix;
import android.graphics.RectF;
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

        mAttacher.getImageView().setOnClickListener(clickHandler);

    }

    public View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Hello World",
                    Toast.LENGTH_LONG).show();
        }
    };


    public void showMessage(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setMessage(msg)
                .setTitle(title);

        AlertDialog dialog = builder.create();
    }

//    @Override
//    public boolean onTouchListener(MotionEvent event) {
//
//        // calculate inverse matrix
//        Matrix inverse = new Matrix();
//        inverse.invert(inverse);
//        // map touch point from ImageView to image
//        float[] touchPoint = new float[] {event.getRawX(), event.getRawY()};
//        inverse.mapPoints(touchPoint);
//        // touchPoint now contains x and y in image's coordinate system
//
//        //Log.d(TAG, "touch coords="+touchPoint[0] + " - " + touchPoint[1]);
//
//        showMessage("ALERTA!", "touch coords="+touchPoint[0] + " - " + touchPoint[1]);
//
//        return super.onTouchEvent(event);
//    }

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
