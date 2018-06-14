package com.food.ecom.product;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.food.ecom.R;
import com.food.ecom.fragments.ProductListFragment;
import com.food.ecom.fragments.ViewPagerActivity;
import com.food.ecom.model.Product;
import com.food.ecom.notification.NotificationCountSetClass;
import com.food.ecom.options.CartListActivity;
import com.food.ecom.startup.MainActivity;
import com.food.ecom.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class ItemDetailsActivity extends AppCompatActivity {
    //int imagePosition;
    Product oProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);
TextView mtvName=(TextView)findViewById(R.id.ProductName);
        TextView mtvPrice=(TextView)findViewById(R.id.ProductPrice);
        TextView mtvProductDelivery=(TextView)findViewById(R.id.ProductDelivery);
        TextView mtvProductDescription=(TextView)findViewById(R.id.tvDetail);

        //Getting image uri from previous screen
        if (getIntent() != null) {
            oProduct =(Product)getIntent().getSerializableExtra(ProductListFragment.ProductObj);
            //imagePosition = getIntent().getIntExtra(ProductListFragment.STRING_IMAGE_URI,0);
        }
        Uri uri = Uri.parse(oProduct.getImage());
        mtvName.setText(oProduct.getName());
        mtvPrice.setText(oProduct.getPriceString());
        mtvProductDelivery.setText("None");
        mtvProductDescription.setText(oProduct.getDescription());
        mImageView.setImageURI(uri);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ItemDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);

            }
        });

        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oProduct.setQty((double) 1);
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(oProduct.getImage());
                imageUrlUtils.addCartListProduct(oProduct);
                Toast.makeText(ItemDetailsActivity.this,"Item added to cart.",Toast.LENGTH_SHORT).show();
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
            }
        });

        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));
            }
        });

    }
}
