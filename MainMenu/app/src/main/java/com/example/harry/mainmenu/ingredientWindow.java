package com.example.harry.mainmenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ingredientWindow extends AppCompatActivity implements View.OnClickListener {

    private EditText name;
    private EditText amount;
    private Spinner unit;
    private Button sub;
    private ArrayAdapter<String> adapterUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        LinearLayout layout = findViewById(R.id.mainLayout);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (int) (height * .8);
        params.width = (int) (width * .8);
        layout.setLayoutParams(params);

        name = (EditText) findViewById(R.id.ingredientName);
        sub = (Button) findViewById(R.id.submit);
        amount = (EditText) findViewById(R.id.ingredientAmount);
        unit = (Spinner) findViewById(R.id.unitDropDown);

        units();

        sub.setOnClickListener(this);

        if(getIntent().hasExtra("ingredientName")){
            name.setText(getIntent().getStringExtra("ingredientName"));
        }
        if(getIntent().hasExtra("amount")){
            amount.setText(getIntent().getStringExtra("amount"));
        }
        if(getIntent().hasExtra("unit")){
            String text = getIntent().getStringExtra("unit");

            for(int i=0; i < adapterUnit.getCount(); i++) {
                if(text.equals(adapterUnit.getItem(i).toString())){
                    unit.setSelection(i);
                    break;
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                Intent i = new Intent();
                i.putExtra("name", name.getText().toString());
                i.putExtra("amount", amount.getText().toString());
                i.putExtra("unit", unit.getSelectedItem().toString());
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }

    private void units() {
        String[] items = new String[]{"", "teaspoon", "tablespoon", "fluid ounce", "cup", "pint", "millilitre",
                "litre", "pound", "ounce", "milligram", "gram", "kilograms"};

        adapterUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        unit.setAdapter(adapterUnit);
    };
}
