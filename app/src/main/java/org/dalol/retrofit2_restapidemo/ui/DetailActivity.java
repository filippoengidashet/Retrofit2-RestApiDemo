/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package org.dalol.retrofit2_restapidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dalol.retrofit2_restapidemo.R;
import org.dalol.retrofit2_restapidemo.model.pojo.Flower;
import org.dalol.retrofit2_restapidemo.model.helper.Constants;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public class DetailActivity extends AppCompatActivity {

    private ImageView mPhoto;
    private TextView mName, mId, mCategory, mInstruction, mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        Flower flower = (Flower) intent.getSerializableExtra(Constants.REFERENCE.FLOWER);

        configViews();

        mId.setText(String.format("%d", flower.getProductId()));
        mName.setText(flower.getName());
        mCategory.setText(flower.getCategory());
        mInstruction.setText(flower.getInstructions());
        mPrice.setText(String.format("$%.2f", flower.getPrice()));

        if (flower.isFromDatabase()) {
            mPhoto.setImageBitmap(flower.getPicture());
        } else {
            Picasso.with(getApplicationContext()).load(Constants.HTTP.BASE_URL + "/photos/" + flower.getPhoto()).into(mPhoto);
        }
    }

    private void configViews() {
        mPhoto = (ImageView) findViewById(R.id.flowerPhoto);
        mName = (TextView) findViewById(R.id.flowerName);
        mId = (TextView) findViewById(R.id.flowerId);
        mCategory = (TextView) findViewById(R.id.flowerCategory);
        mInstruction = (TextView) findViewById(R.id.flowerInstruction);
        mPrice = (TextView) findViewById(R.id.flowerPrice);

    }
}
