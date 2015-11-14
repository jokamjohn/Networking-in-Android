package com.hanselandpetal.catalog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hanselandpetal.catalog.model.Flower;
import com.hanselandpetal.catalog.parsers.FlowerJsonParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	TextView output;
    private ProgressBar pb;
    List<JokamTask> tasks;

    List<Flower> flowerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());

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
        if (flowerList != null){
            for (Flower flower:flowerList) {
                output.append(flower.getName() + "\n");
            }
        }
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
    public class JokamTask extends AsyncTask<String,String,String>{

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
        protected String doInBackground(String... params) {

            return HttpManager.getData(params[0],"feeduser","feedpassword");

        }

        //After the task
        @Override
        protected void onPostExecute(String result) {
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

            //Passing the raw XML from doInBackground to the XML parser
            flowerList = FlowerJsonParser.parseFeed(result);
            updateDisplay();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            //We get only one value from publish progress
//            updateDisplay(values[0]);
        }
    }

}