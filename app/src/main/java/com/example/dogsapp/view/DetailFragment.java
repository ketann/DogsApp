package com.example.dogsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;

    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.mTVDogName)
    TextView mTVDogName;

    @BindView(R.id.mTVDogPurpose)
    TextView mTVDogPurpose;

    @BindView(R.id.mTVDogTemperament)
    TextView mTVDogTemperament;

    @BindView(R.id.mTVDogLifeSpan)
    TextView mTVDogLifeSpan;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch();

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed) {
                mTVDogName.setText(dogBreed.dogBreed);
                mTVDogPurpose.setText(dogBreed.bredFor);
                mTVDogTemperament.setText(dogBreed.temperament);
                mTVDogLifeSpan.setText(dogBreed.lifeSpan);
            }
        });
    }
}