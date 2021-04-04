package com.example.marketapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketapp.model.Product;
import com.example.marketapp.model.ShoppingCart;

import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private final List<Product> list;
    private final Activity context;

    public ProductListAdapter(Activity context, List<Product> list) {
        super(context, R.layout.shopping_cart_list_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected Button button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.shopping_cart_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.lblShoppingCartProductName);

            viewHolder.button = (Button) view.findViewById(R.id.btnRemoveProduct);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // removes the product from the shopping cart
                    Product product = (Product) viewHolder.button.getTag();
                    ShoppingCart.removeProduct(product);

                    // removes the item from the layout
                    list.remove(position);
                    ProductListAdapter.this.notifyDataSetChanged();
                }
            });
            view.setTag(viewHolder);
            viewHolder.button.setTag(list.get(position));
        }
        else {
            view = convertView;
            ((ViewHolder) view.getTag()).button.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        return view;
    }
}
