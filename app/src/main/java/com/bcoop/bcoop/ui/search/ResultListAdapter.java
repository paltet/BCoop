package com.bcoop.bcoop.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bcoop.bcoop.HomeActivity;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.ProfileActivity;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.UserSearch;
import com.bcoop.bcoop.ui.profile.ProfileFragment;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder> {


    // Store a member variable for the users
    private ArrayList<UserSearch> users;
    private UserSearch actUser;
    private String habilitat_seleccionada;
    // Pass in the contact array into the constructor
    public ResultListAdapter(ArrayList<UserSearch> users, String habilitat_seleccionada) {
        this.users = users;
        this.habilitat_seleccionada = habilitat_seleccionada;
    }

    @NonNull
    @Override
    public ResultListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ResultListView = inflater.inflate(R.layout.item_row, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ResultListView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ResultListAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        UserSearch user = users.get(position);
        actUser = user;

        // Set item views based on your views and data model
        ImageView profileImageView = holder.profileImageView;
        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(user.getName());
        Button button = holder.xatButton;
        button.setText(R.string.view_profile);
        Picasso.get().load("https://images.pexels.com/photos/248771/pexels-photo-248771.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=346&w=640").into(holder.profileImageView);
        //falta agafar la distancia i el rating

        TextView distance = holder.distTextView;
        distance.setText(user.getDistance());


        System.out.println("AQUESTA ES LA HABILITAAAAAAAAAAAAATTTTTTTTTTTTTTT"+ habilitat_seleccionada);
        TextView rating = holder.ratingTextView;
        //agafem el rating de la habiliat del user en qüestió
        int estrellesHabilitat;
        Map<String, HabilitatDetall> habilitatsDetallProveidor = user.getHabilitats();
        for (Map.Entry<String, HabilitatDetall> entry : habilitatsDetallProveidor.entrySet()) {
            if (entry.getKey().equals(habilitat_seleccionada)) {
                HabilitatDetall habilitatDetall = entry.getValue();
                rating.setText(String.valueOf(habilitatDetall.getValoracio()));
            }
        }

        //per amagar o no la expandableList
        boolean isExpanded = users.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView profileImageView;
        public Button xatButton;
        public TextView ratingTextView;
        public TextView distTextView;

        ConstraintLayout expandableLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.Name);

            profileImageView = (ImageView) itemView.findViewById(R.id.circleImage);
            xatButton = (Button) itemView.findViewById(R.id.buttonXat);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
            distTextView = (TextView) itemView.findViewById(R.id.distTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserSearch user = users.get(getAdapterPosition());
                    user.setExpanded(!user.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            xatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("", "aqui");

                    Intent intent = new Intent((FragmentActivity)v.getContext(), ProfileActivity.class);
                    intent.putExtra("email", actUser.getMail());
                    v.getContext().startActivity(intent);


                }
            });

        }
    }


}