package com.example.recyclepro.activities.Assessor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.recyclepro.R;
import com.squareup.picasso.Picasso;

public class ViewImagesOfProductFragment extends Fragment {

    private ImageView frontOfDevice, backOfDevice;
    private ImageButton btnBack;
    public static final String TAG= ViewImagesOfProductFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view_images_of_product, container, false);
        frontOfDevice=view.findViewById(R.id.ivFrontOfDevice);
        backOfDevice=view.findViewById(R.id.ivBackOfDevice);
        btnBack=view.findViewById(R.id.btnBack);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String urlFrontOfDevice = bundle.getString("frontOfDevice");
            String urlBackOfDevice = bundle.getString("backOfDevice");

            if (urlFrontOfDevice != null) {
                Picasso.get()
                        .load(urlFrontOfDevice)
                        .into(frontOfDevice);
            }

            if (urlBackOfDevice != null) {
                Picasso.get()
                        .load(urlBackOfDevice)
                        .into(backOfDevice);
            }
        }
        btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }
}