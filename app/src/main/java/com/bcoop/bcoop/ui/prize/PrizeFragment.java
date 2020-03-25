package com.bcoop.bcoop.ui.prize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bcoop.bcoop.R;

public class PrizeFragment extends Fragment {

    private PrizeViewModel prizeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        prizeViewModel =
                ViewModelProviders.of(this).get(PrizeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_prize, container, false);
        final TextView textView = root.findViewById(R.id.text_prize);
        prizeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
