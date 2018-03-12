package com.example.andrewclark.stockquotes_clark;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private String searchQuery;



    TextView aSymbol;
    TextView aName;
    TextView aPrice;
    TextView aTime;
    TextView aChange;
    TextView aRange;



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("aSymbol", aSymbol.getText().toString());
        savedInstanceState.putString("aName", aName.getText().toString());
        savedInstanceState.putString("aPrice", aPrice.getText().toString());
        savedInstanceState.putString("aTime", aTime.getText().toString());
        savedInstanceState.putString("aChange", aChange.getText().toString());
        savedInstanceState.putString("aRange", aRange.getText().toString());

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        aSymbol.setText(savedInstanceState.getString("aSymbol"));
        aName.setText(savedInstanceState.getString("aName"));
        aPrice.setText(savedInstanceState.getString("aPrice"));
        aTime.setText(savedInstanceState.getString("aTime"));
        aChange.setText(savedInstanceState.getString("aChange"));
        aRange.setText(savedInstanceState.getString("aRange"));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSymbol = (TextView) findViewById(R.id.aSymbol);
        aName = (TextView) findViewById(R.id.aName);
        aPrice = (TextView) findViewById(R.id.aPrice);
        aTime = (TextView) findViewById(R.id.aTime);
        aChange = (TextView) findViewById(R.id.aChange);
        aRange = (TextView) findViewById(R.id.aRange);


        Button searchButton = (Button) findViewById(R.id.getQuoteBut);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText searchBox = (EditText) findViewById(R.id.searchText);

                searchQuery = searchBox.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

                if(validateInput(searchQuery)) {
                    startLoadDisplay();
                    asyncTask process = new asyncTask();
                    process.execute();
                } else {
                    Toast.makeText(MainActivity.this, "Error in retrieving stock symbol", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private class asyncTask extends AsyncTask<String, String, String> {
        Stock objStock;



        @Override
        protected String doInBackground(String... params) {

            objStock = new Stock(searchQuery);
            try {
                objStock.load();
            } catch(Exception e) {
                Log.i("bs", e.getMessage());
            }
            Log.i("async", "started");
            return "All Done!";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            if(objStock.getName() != null) {
                aSymbol.setText(objStock.getSymbol());
                aName.setText(objStock.getName());
                aPrice.setText(objStock.getLastTradePrice());
                aTime.setText(objStock.getLastTradeTime());
                aChange.setText(objStock.getChange());
                aRange.setText(objStock.getRange());

            } else {
                aSymbol.setText("...");
                aName.setText("...");
                aPrice.setText("...");
                aTime.setText("...");
                aChange.setText("...");
                aRange.setText("...");
                Toast.makeText(MainActivity.this, "Error in retrieving stock symbol", Toast.LENGTH_SHORT).show();

            }
            endLoadDisplay();

        }

    }

    public boolean validateInput(String input) {
        boolean valid = false;
        if ((input.matches("^[a-zA-Z]+$")) && input != "" && input != null) {
            valid = true;
        }
        return valid;
    }

    public void startLoadDisplay() {
        Button searchButton = (Button) findViewById(R.id.getQuoteBut);
        searchButton.setText("LOADING...");

        int buttonColor = Color.parseColor("#303540");

        int textColor = Color.parseColor("#ffffff");
        searchButton.setBackgroundColor(buttonColor);
        searchButton.setTextColor(textColor);
        searchButton.setEnabled(false);
    }

    public void endLoadDisplay() {
        Button searchButton = (Button) findViewById(R.id.getQuoteBut);
        searchButton.setText("SEARCH");
        int buttonColor = Color.parseColor("#D6D7D7");
        int textColor = Color.parseColor("#212121");
        searchButton.setBackgroundColor(buttonColor);
        searchButton.setTextColor(textColor);
        searchButton.setEnabled(true);
    }



}
