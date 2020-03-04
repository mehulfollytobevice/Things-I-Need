package com.tasks.babyneeds;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tasks.babyneeds.data.DatabaseHandler;
import com.tasks.babyneeds.model.Item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemqty;
    private EditText itemcolor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler=new DatabaseHandler(MainActivity.this);
        bypassMainActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
//        some earlier versions of android do not support the toolbar , this makes sures they do
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
//                snack bar is another kind of toast message
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });



    }

    private void bypassMainActivity() {
        if(databaseHandler.getCount()>0){
            startActivity(new Intent(MainActivity.this,OurListActivity.class));
//            we want to kill all activities before this
            finish();
        }
    }

    private void createPopupDialog() {

        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        babyItem=view.findViewById(R.id.enter_item);
        itemqty=view.findViewById(R.id.qty);
        itemcolor=view.findViewById(R.id.color);
        itemSize=view.findViewById(R.id.size);
//        Very important , the save button should be initialised and the onClick listener should be specified here only since this is where the popup is created
        saveButton=view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(v);
//                Item show=databaseHandler.getItem(4);
//                Log.d("ItemAdded","ADD:"+show.getId()+" "+show.getItem()+" "+show.getColor());
            }
        });

        builder.setView(view);
        alertDialog=builder.create();// here we are creating the dialog object ,which actually has the methods to show the dialog box
        alertDialog.show();

    }

    private void saveItem(View view) {

//        ToDo: Save the item and add it to the database
        if(!babyItem.getText().toString().trim().isEmpty() && !itemqty.getText().toString().isEmpty() && !itemcolor.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty()){
            String bitem = String.valueOf(babyItem.getText());
            int quan=Integer.parseInt(String.valueOf(itemqty.getText()));
            String col= String.valueOf(itemcolor.getText());
            int sizeb=Integer.parseInt(String.valueOf(itemSize.getText()));
            Item add=new Item(bitem,quan,col,sizeb);
            databaseHandler.addItem(add);
            Snackbar.make(view, "Item Added", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else{
            Snackbar.make(view, "Fill the Empty Fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
//        ToDo:Create a RecyclerView that can then show all the items in the database

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                this is where the type the code
                alertDialog.dismiss();
                startActivity(new Intent(MainActivity.this,OurListActivity.class));
                finish();

            }
        },1200);// to dalay the dismissal

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
