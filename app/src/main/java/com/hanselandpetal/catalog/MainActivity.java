package com.hanselandpetal.catalog;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hanselandpetal.catalog.model.Flower;
import com.hanselandpetal.catalog.parsers.FlowerJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {


    private ProgressBar pb;
    List<JokamTask> tasks;
    List<Flower> flowerList;
    private static final String PHOTOS_BASE_URL = "http://services.hanselandpetal.com/photos/";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        //Initializing progressbar
        pb = (ProgressBar) findViewById(R.id.progressBar);
        //Invisible when the app starts
        pb.setVisibility(View.INVISIBLE);

        //Initializing the JokamTasks list
        tasks = new ArrayList<>();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_do_task) {
            if (isOnline())
            {
                requestData("http://services.hanselandpetal.com/secure/flowers.json");
            }
            else {
                Toast.makeText(this,"No network Access",Toast.LENGTH_LONG).show();
            }
		}
		return false;
	}

    /**
     * Executes the AsyncTask
     */
    private void requestData(String uri) {
        JokamTask task = new JokamTask();
        //Parallel processing of the tasks.
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
    }

    /**
     * Appends message to the textview
     */
	protected void updateDisplay() {
        //Using the adapter
        FlowerAdapter adapter = new FlowerAdapter(this,R.layout.item_flower,flowerList);
        setListAdapter(adapter);
	}

    /**
     * Checking for network access
     *
     * @return boolean
     */
    protected boolean isOnline()
    {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Makes a network request
     */
    public class JokamTask extends AsyncTask<String, String, List<Flower>> {

        //Before the task
        @Override
        protected void onPreExecute() {
//            updateDisplay("Before Task");
            //Visible before the task starts
            if (tasks.size() == 0)
            {
                pb.setVisibility(View.VISIBLE);
            }
            //add task to the list
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... params) {

            String results = HttpManager.getData(params[0],"feeduser","feedpassword");
            flowerList = FlowerJsonParser.parseFeed(results);

            if (flowerList != null) {
                for (Flower flower:flowerList) {
                    Uri builtUri = Uri.parse(PHOTOS_BASE_URL)
                            .buildUpon()
                            .appendPath(flower.getPhoto())
                            .build();
                    InputStream imageStream = null;
                    try {
                        URL url = new URL(builtUri.toString());
                        imageStream = (InputStream) url.getContent();
                        Bitmap image = BitmapFactory.decodeStream(imageStream);
                        flower.setBitmap(image);

                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            assert imageStream != null;
                            imageStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i(LOG_TAG,"photo url: " + builtUri.toString());
                }
            }
            return flowerList;

        }

        //After the task
        @Override
        protected void onPostExecute(List<Flower> result) {
            //remove tasks from the list
            tasks.remove(this);
            //Invisible after the task
            if (tasks.size() == 0)
            {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null)
            {
                Toast.makeText(MainActivity.this,"Can`t connect to web service",Toast.LENGTH_LONG)
                        .show();
            }
            //Display list of the data
            updateDisplay();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            //We get only one value from publish progress
//            updateDisplay(values[0]);
        }
    }

}