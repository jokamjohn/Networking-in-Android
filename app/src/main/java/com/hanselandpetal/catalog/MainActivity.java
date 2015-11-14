package com.hanselandpetal.catalog;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView output;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_do_task) {
			JokamTask task = new JokamTask();
            task.execute("param1","param 2", "param 3");
		}
		return false;
	}

	protected void updateDisplay(String message) {
		output.append(message + "\n");
	}

    public class JokamTask extends AsyncTask<String,String,String>{

        //Before the task
        @Override
        protected void onPreExecute() {
            updateDisplay("Before Task");
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < strings.length; i++)
            {
                //Show progress
                //We only pass in one value
                publishProgress("Working with: " + strings[i]);

                //Fake a delay before the next statement is executed
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task completed";
        }

        //After the task
        @Override
        protected void onPostExecute(String s) {
            updateDisplay(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //We get only one value from publish progress
            updateDisplay(values[0]);
        }
    }

}