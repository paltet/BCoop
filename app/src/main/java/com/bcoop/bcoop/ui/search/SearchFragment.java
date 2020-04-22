package com.bcoop.bcoop.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.R;
import com.bcoop.bcoop.UserSearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView mResultList;
    private ResultListAdapter adapter;
    private Spinner searchSpinner;
    private String habilitat_seleccionada;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        setSpinnerContent(root);
        //final TextView textView = root.findViewById(R.id.textView);
        //searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
           /* @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        ArrayList<UserSearch> users = UserSearch.initUsers();

        mResultList = root.findViewById(R.id.ResultList);


        adapter = new ResultListAdapter(users);
        mResultList.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mResultList.setLayoutManager(layoutManager);

/*
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habilitat_seleccionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        return root;
    }

    private void setSpinnerContent(View root) {

        searchSpinner = root.findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.abilities));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(myAdapter);

        AdapterView.OnItemSelectedListener mListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habilitat_seleccionada = parent.getSelectedItem().toString();
                Toast.makeText(parent.getContext(), "Seleccionada:"+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            } //still never shows up in toast

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "none found = user_store_id.", Toast.LENGTH_SHORT).show(); //still nothing
            }
        };
        searchSpinner.setOnItemSelectedListener(mListener); // Register this spinner for a mListener



    }
}
