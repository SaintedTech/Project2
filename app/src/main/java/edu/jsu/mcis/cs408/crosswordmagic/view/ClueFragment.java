package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.controller.AbstractController;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentClueBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.Word;

public class ClueFragment extends Fragment implements AbstractView {


    CrosswordMagicController controller;

    private FragmentClueBinding binding;

    private String cluesAcross;
    private String cluesDown;


    public void draw(){

        //find container
        TextView aContainer = binding.aContainer;
        TextView dContainer = binding.dContainer;



        //aContainer.setTextSize(16f);
        //dContainer.setTextSize(16f);

        //get words from model
        controller.getWords();
        aContainer.setText(cluesAcross);
        dContainer.setText(cluesDown);
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentClueBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();



    }



    @Override

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* get controller, register Fragment as a View */
        this.controller = ((MainActivity)getContext()).getController();
        controller.addView(this);
        draw();


    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        Object value = evt.getNewValue();

        if (name.equals(CrosswordMagicController.CLUE_WORDS_PROPERTY)) {

           if(value instanceof String[]){

               //parse to string array, get values
               cluesAcross = ((String[]) value)[0];
               cluesDown = ((String[]) value)[1];



           }


        }

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

}
