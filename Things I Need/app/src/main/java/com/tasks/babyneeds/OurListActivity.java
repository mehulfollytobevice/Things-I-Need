package com.tasks.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tasks.babyneeds.Adapter.RecyclerViewAdapter;
import com.tasks.babyneeds.data.DatabaseHandler;
import com.tasks.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class OurListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemarrayList;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ArrayAdapter<String> arrayAdapter;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemqty;
    private EditText itemcolor;
    private EditText itemSize;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_list);
        recyclerView=findViewById(R.id.recyclerView);
        fab=findViewById(R.id.addAnother);
        db=new DatabaseHandler(OurListActivity.this);
        itemarrayList=db.getAllItems();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//since we need to show all the data in form of linear layout(vertically)
        recyclerViewAdapter=new RecyclerViewAdapter(itemarrayList,OurListActivity.this);//intialising the recyclerViewAdapter
        recyclerView.setAdapter(recyclerViewAdapter);
//        here the recyclerView needs to be updated, so it has to be notified each time the dataset is changed
        recyclerViewAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup();
            }
        });
    }

    private void createPopup() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        babyItem=view.findViewById(R.id.enter_item);
        itemqty=view.findViewById(R.id.qty);
        itemcolor=view.findViewById(R.id.color);
        itemSize=view.findViewById(R.id.size);
        saveButton=view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(v);
            }
        });

        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }

    private void saveItem(View view) {

        if(!babyItem.getText().toString().trim().isEmpty() && !itemqty.getText().toString().isEmpty() && !itemcolor.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty()){
            String bitem = String.valueOf(babyItem.getText());
            int quan=Integer.parseInt(String.valueOf(itemqty.getText()));
            String col= String.valueOf(itemcolor.getText());
            int sizeb=Integer.parseInt(String.valueOf(itemSize.getText()));
            Item add=new Item(bitem,quan,col,sizeb);
            db.addItem(add);
            Snackbar.make(view, "Item Added", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            recyclerViewAdapter.notifyItemInserted(add.getId()); we need to get the item index for this to work
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    startActivity(new Intent(OurListActivity.this,OurListActivity.class));
                    finish();
                }
            },500);

        }
        else{
            Snackbar.make(view, "Fill the Empty Fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }
}
