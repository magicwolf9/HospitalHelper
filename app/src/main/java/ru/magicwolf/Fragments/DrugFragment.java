package ru.magicwolf.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import ru.magicwolf.R;

public class DrugFragment extends Fragment implements View.OnClickListener {
    private String drugName, features;
    private String drugTime;
    private static final String TAG = "MyApp";
    private LinearLayout.LayoutParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drug_fragment, null);

        this.params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        v.setLayoutParams(this.params);
        v.setOnClickListener(this);

        TextView tvDrugName = (TextView) v.findViewById(R.id.drugName);
        tvDrugName.setText(drugName);
        Log.i(TAG, "Set drugName");

        TextView tvDrugTime = (TextView) v.findViewById(R.id.textDrugTimeValues);
        tvDrugTime.setText(drugTime);
        Log.i(TAG, "Set drugTime");

        TextView tvFeatures = (TextView) v.findViewById(R.id.textDrugFeatures);
        tvFeatures.setText(features);
        Log.i(TAG, "Set features");


        return v;
    }

    public void setTexts(String drugName, String drugTime, String features){
        this.drugName = drugName;
        this.drugTime = drugTime;
        this.features = features;
    }

    @Override
    public void onClick(View v) {
        int height = v.getHeight();
        if (height == (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics())) {
            //View v2 = inflater.inflate(R.layout.drug_fragment, null);
            this.params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics()));
            v.setLayoutParams(this.params);

        } else if (height == (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics())) {
            //View v2 = inflater.inflate(R.layout.drug_fragment, null);
            this.params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
            v.setLayoutParams(this.params);

        }
    }
}
