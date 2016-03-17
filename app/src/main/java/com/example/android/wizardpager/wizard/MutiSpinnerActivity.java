package com.example.android.wizardpager.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.wizardpager.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.util.ViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by salman on 15/03/16.
 */
public class MutiSpinnerActivity extends AppCompatActivity {

    View rootView;
    ArrayList<String> arrayList;
    ListView listView;
    CustomArrayAdapter customArrayAdapter;
    MaterialEditText materialEditText;
    Button button;
    JSONObject jsonObject;
    ArrayList<MultiSpinnerValues> tempDataList = new ArrayList<MultiSpinnerValues>();


    private final TextWatcher testWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {


            String text = editable.toString().trim();

            ArrayList<MultiSpinnerValues> finalData  = new ArrayList<MultiSpinnerValues>();
            for (MultiSpinnerValues data : tempDataList){
                if(data.value.contains(text) && data.isChecked){
                    finalData.add(data);
                }
            }

            if(text.isEmpty()){
                finalData = (ArrayList<MultiSpinnerValues>) tempDataList.clone();
            }

            if(finalData.size() == 0){
                button.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            customArrayAdapter.clear();
            customArrayAdapter.addAll(finalData);
            customArrayAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.item_multi_spinner, null);
        setContentView(rootView);

        Intent intent = getIntent();

        if(intent != null){

            arrayList = intent.getExtras().getStringArrayList("list");
            try {
                jsonObject = new JSONObject(intent.getExtras().getString("json"));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        try {
            initData();
        } catch (Exception e){

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  addData(materialEditText, stepName, materialEditText.getText().toString());
                arrayList.add(materialEditText.getText().toString());
               // customArrayAdapter.addAll(( ArrayList<String> )arrayList.clone());

                customArrayAdapter.clear();
                MultiSpinnerValues multiSpinnerValue = new MultiSpinnerValues();
                multiSpinnerValue.value = materialEditText.getText().toString();
                multiSpinnerValue.isChecked = true;

                tempDataList.add(multiSpinnerValue);

                customArrayAdapter.addAll(tempDataList);
                customArrayAdapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                materialEditText.setText("");



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multi_spinner, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_yes:

                Intent intent=new Intent();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", arrayList);

                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDatainEditText(MaterialEditText editText) throws Exception {
        editText.setHint("Enter data...");
      //  editText.setId(ViewUtil.generateViewId());
        editText.addTextChangedListener(testWatcher);
    }

    private void initData() throws  Exception{



        for(String s : arrayList){
            MultiSpinnerValues multiSpinnerValue = new MultiSpinnerValues();
            multiSpinnerValue.value = s;
            multiSpinnerValue.isChecked = true;
            tempDataList.add(multiSpinnerValue);
        }


        listView = (ListView) findViewById(R.id.list);
        customArrayAdapter = new CustomArrayAdapter(getApplicationContext(), R.layout.item_multi_spinner);
        customArrayAdapter.addAll((ArrayList<MultiSpinnerValues>)tempDataList.clone());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MultiSpinnerValues multiSpinnerValue = (MultiSpinnerValues) parent.getItemAtPosition(position);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    multiSpinnerValue.isChecked = false;
                    arrayList.remove(multiSpinnerValue.value);
                //    customArrayAdapter.remove(multiSpinnerValue);
                //    customArrayAdapter.add(multiSpinnerValue);

                } else {
                    checkBox.setChecked(true);
                    multiSpinnerValue.isChecked = true;
                    arrayList.add(multiSpinnerValue.value);
                }

            }
        });

        listView.setAdapter(customArrayAdapter);
        materialEditText = (MaterialEditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.addInList);
        addDatainEditText(materialEditText );


    }

    public class CustomArrayAdapter extends ArrayAdapter<MultiSpinnerValues>{

        private LayoutInflater mInflater;
        private CheckBox checkBox;
        private TextView materialEditText;
        MultiSpinnerValues multiSpinnerValue;

        public CustomArrayAdapter(Context context, int resource) {
            super(context, resource);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_muti_spinner, parent, false);
            }

            multiSpinnerValue = getItem(position);
            checkBox = ViewHolder.get(convertView, R.id.checkBox);

            materialEditText = ViewHolder.get(convertView, R.id.material_text);

            materialEditText.setText(multiSpinnerValue.value);

            checkBox.setTag(R.id.checkBox, multiSpinnerValue);
            if(multiSpinnerValue.isChecked == true){
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MultiSpinnerValues mValues = (MultiSpinnerValues)buttonView.getTag(R.id.checkBox);
                    if(isChecked){
                        buttonView.setChecked(true);
                        mValues.isChecked = true;
                        arrayList.add(mValues.value);

                    } else {
                        buttonView.setChecked(false);
                        mValues.isChecked = false;
                        arrayList.remove(mValues.value);
                    }
                }
            });

            return convertView;

        }
    }

    public static class ViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }

}
