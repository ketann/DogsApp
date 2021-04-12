package com.example.dogsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>());

    @BindView(R.id.recyclerViewDogList)
    RecyclerView recyclerViewDogList;

    @BindView(R.id.mTVDataNotFound)
    TextView mTVDataNotFound;

    @BindView(R.id.loadingView)
    ProgressBar loadingView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        recyclerViewDogList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDogList.setAdapter(dogsListAdapter);

        refreshLayout.setOnRefreshListener(() -> {
            recyclerViewDogList.setVisibility(View.GONE);
            mTVDataNotFound.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            viewModel.refreshByPassCache();
            refreshLayout.setRefreshing(false);
        });

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.dogs.observe(this, dogs -> {
            if (dogs != null && dogs instanceof List) {
                recyclerViewDogList.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }
        });

        viewModel.dogLoadError.observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) ;
            mTVDataNotFound.setVisibility(isError ? View.VISIBLE : View.GONE);
        });
        viewModel.loading.observe(this, isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) ;
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                mTVDataNotFound.setVisibility(View.GONE);
                recyclerViewDogList.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings: {
                if (isAdded()) {
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}