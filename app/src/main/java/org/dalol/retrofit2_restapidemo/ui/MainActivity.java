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

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.dalol.retrofit2_restapidemo.R;
import org.dalol.retrofit2_restapidemo.controller.RestManager;
import org.dalol.retrofit2_restapidemo.model.Flower;
import org.dalol.retrofit2_restapidemo.model.adapter.FlowerAdapter;
import org.dalol.retrofit2_restapidemo.model.helper.Constants;
import org.dalol.retrofit2_restapidemo.model.helper.FlowerDatabase;
import org.dalol.retrofit2_restapidemo.model.helper.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public class MainActivity extends AppCompatActivity implements FlowerAdapter.FlowerClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RestManager mManager;
    private FlowerAdapter mFlowerAdapter;
    private FlowerDatabase mDatabase;
    private Button mReload;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configViews();

        mManager = new RestManager();
        mDatabase = new FlowerDatabase(this);

        loadFlowerFeed();

        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlowerFeed();
            }
        });
    }

    private void loadFlowerFeed() {

        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Loading Flower Data...");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.show();

        mFlowerAdapter.reset();
        if (getNetworkAvailablility()) {
            getFeed();
        } else {
            getFeedFromDatabase();
        }
    }

    private void getFeedFromDatabase() {
        List<Flower> flowerList = mDatabase.getFlowers();

        for (int i = 0; i < flowerList.size(); i++) {
            Flower flower = flowerList.get(i);
            mFlowerAdapter.addFlower(flower);
            Log.d(TAG, flower.getName() + "||" + flower.getInstructions());
        }

        mDialog.dismiss();
    }

    private void configViews() {
        mReload = (Button) findViewById(R.id.reload);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        mFlowerAdapter = new FlowerAdapter(this);

        mRecyclerView.setAdapter(mFlowerAdapter);
    }

    @Override
    public void onClick(int position) {
        Flower selectedFlower = mFlowerAdapter.getSelectedFlower(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constants.REFERENCE.FLOWER, selectedFlower);
        startActivity(intent);
    }

    public void getFeed() {

        Call<List<Flower>> listCall = mManager.getFlowerService().getAllFlowers();
        listCall.enqueue(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Response<List<Flower>> response) {

                if (response.isSuccess()) {
                    List<Flower> flowerList = response.body();

                    for (int i = 0; i < flowerList.size(); i++) {
                        Flower flower = flowerList.get(i);

                        SaveIntoDatabase task = new SaveIntoDatabase();
                        task.execute(flower);

                        mFlowerAdapter.addFlower(flower);
                    }
                } else {
                    int sc = response.code();
                    switch (sc) {

                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean getNetworkAvailablility() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }

    public class SaveIntoDatabase extends AsyncTask<Flower, Flower, Boolean> {


        private final String TAG = SaveIntoDatabase.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Flower... params) {

            Flower flower = params[0];

            try {
                InputStream stream = new URL("http://services.hanselandpetal.com/photos/" + flower.getPhoto()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                flower.setPicture(bitmap);
                publishProgress(flower);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Flower... values) {
            super.onProgressUpdate(values);
            mDatabase.addFlower(values[0]);

            Log.d(TAG, "Values Got " + values[0].getName());
        }

    }
}
