package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.controller.AbstractController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentPuzzleBinding;

public class PuzzleFragment extends Fragment implements AbstractView{


    private CrosswordGridView crosswordGridView;
    AbstractController controller;
    private FragmentEventListener callback;
    private FragmentPuzzleBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPuzzleBinding.inflate(getLayoutInflater(), container, false);
        crosswordGridView = new CrosswordGridView(requireContext(), null);
        binding.getRoot().addView(crosswordGridView);
        return binding.getRoot();

    }




    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* get controller, register Fragment as a View */
        this.controller = ((MainActivity)getContext()).getController();


        controller.addView(this);

    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {


    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public interface FragmentEventListener {
        public void onFragmentButtonClick(String outputText);
    }
}
