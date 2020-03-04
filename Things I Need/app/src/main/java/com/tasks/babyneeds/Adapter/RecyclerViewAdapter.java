package com.tasks.babyneeds.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tasks.babyneeds.R;
import com.tasks.babyneeds.data.DatabaseHandler;
import com.tasks.babyneeds.model.Item;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Item> itemList;
    private Context context;
//    intialising some important stuff to get confirm popup
    private LayoutInflater layoutInflater;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public RecyclerViewAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.itemname.setText(MessageFormat.format("Item:{0}", item.getItem()));
//        set text text has to be in string format
        holder.itemquan.setText(MessageFormat.format("Quantity:{0}", String.valueOf(item.getQty())));
        holder.itemcolor.setText(MessageFormat.format("Color:{0}", item.getColor()));
        holder.itemsize.setText(MessageFormat.format("Size:{0}", String.valueOf(item.getSize())));
    }

    @Override
    public int getItemCount() {
        return itemList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemname;
        private TextView itemquan;
        private TextView itemcolor;
        private TextView itemsize;
        private Button editButton;
        private Button deleteButton;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            itemname=itemView.findViewById(R.id.itemname);
            itemquan=itemView.findViewById(R.id.itemquan);
            itemcolor=itemView.findViewById(R.id.itemcolor);
            itemsize=itemView.findViewById(R.id.itemsize);
            editButton=itemView.findViewById(R.id.editButton);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.editButton:
//                    edit item
                    editItem();
                    break;
                case R.id.deleteButton:
//                    delete item
                    delete_item();
                    break;

            }
        }

        private void delete_item() {
            builder=new AlertDialog.Builder(context);
            View viewd=LayoutInflater.from(context).inflate(R.layout.confirmation,null);
            Button yes=viewd.findViewById(R.id.yes);
            Button no=viewd.findViewById(R.id.no);
            builder.setView(viewd);
            alertDialog=builder.create();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db=new DatabaseHandler(context);
                    int position=getAdapterPosition();
                    Item item=itemList.get(position);
                    db.deleteItem(item);
                    itemList.remove(getAdapterPosition());
                    alertDialog.dismiss();
//                    after the item is removed from the database and the item list the recycler view need to be notified that the item is removed
                    notifyItemRemoved(getAdapterPosition());

                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        private void editItem() {

            final Item item=itemList.get(getAdapterPosition());
            final DatabaseHandler db= new DatabaseHandler(context);
            Button saveButton;
            final EditText babyItem;
            final EditText itemqty;
            final EditText itemcol;
            final EditText itemSize;
            TextView title_update;
            builder=new AlertDialog.Builder(context);
            final View view=LayoutInflater.from(context).inflate(R.layout.popup,null);

            babyItem=view.findViewById(R.id.enter_item);
            itemqty=view.findViewById(R.id.qty);
            itemcol=view.findViewById(R.id.color);
            itemSize=view.findViewById(R.id.size);
            title_update=view.findViewById(R.id.title);
            saveButton=view.findViewById(R.id.saveButton);

            saveButton.setText(R.string.update_item);
            title_update.setText(R.string.updating);
            babyItem.setText(item.getItem());
            itemqty.setText( String.valueOf(item.getQty()));
            itemcol.setText(item.getColor());
            itemSize.setText(String.valueOf(item.getSize()));
            builder.setView(view);
            alertDialog=builder.create();// here we are creating the dialog object ,which actually has the methods to show the dialog box
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!babyItem.getText().toString().trim().isEmpty() && !itemqty.getText().toString().isEmpty() && !itemcol.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty()){
                        item.setItem(String.valueOf(babyItem.getText()));
                        item.setSize(Integer.parseInt(String.valueOf(itemSize.getText())));
                        item.setColor(String.valueOf(itemcol.getText()));
                        item.setQty(Integer.parseInt(String.valueOf(itemqty.getText())));
                        db.updateItem(item);
                        notifyItemChanged(getAdapterPosition(),item);//very important step , we need to pass both position as well as object
//                        Snackbar.make(view, "Item Updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        alertDialog.dismiss();
                    }
                    else{
                        Snackbar.make(view, "Fill the Empty Fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                }
            });
            alertDialog.show();
        }
    }
}
