package com.bcoop.bcoop.ui.prize;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bcoop.bcoop.HomeActivity;
import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.R;

public class PrizeFragment extends Fragment {

    private PrizeViewModel prizeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        prizeViewModel =
                ViewModelProviders.of(this).get(PrizeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_prize, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_prize);
        prizeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void showAlertDialog(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Buy gift");
            alert.setMessage("Do you want to buy this item?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "@string/success", Toast.LENGTH_SHORT).show();
            }
        });
            alert.setNegativeButton("@string/abort", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "@string/abort", Toast.LENGTH_SHORT).show();
                }
            });
        alert.create().show();
    }
}