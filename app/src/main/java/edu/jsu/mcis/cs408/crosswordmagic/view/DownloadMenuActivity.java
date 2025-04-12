package edu.jsu.mcis.cs408.crosswordmagic.view;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.ActivityWelcomeBinding;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.DownloadPageBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class DownloadMenuActivity extends AppCompatActivity implements AbstractView {

    private static String TAG = "DownloadMenuActivity";
    private DownloadPageBinding binding;
    private CrosswordMagicController controller =  new CrosswordMagicController();

    private Integer puzzleid = -1;
    private PuzzleListItem[] puzzles;
    private final PuzzleListItemClickHandler itemClick = new PuzzleListItemClickHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        controller.addView(this);

        super.onCreate(savedInstanceState);
        binding = DownloadPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        updateRecyclerView();


        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add passing
                int y =1;
            }
        });



    }
    private class  PuzzleListItemClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = binding.items.getChildLayoutPosition(v);
            RecyclerViewAdapter adapter = (RecyclerViewAdapter)binding.items.getAdapter();
            if (adapter != null) {
                PuzzleListItem itemSelected = adapter.getListItem(position);


                if (puzzleid.equals(itemSelected.getId())) {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                else{
                    v.setBackgroundColor(Color.GRAY);
                    puzzleid = itemSelected.getId();
                }





            }
        }
    }
    public PuzzleListItemClickHandler getItemClick() { return itemClick; }
    public void addNewPuzzle() {

       //add puzzle to database, for later use
        updateRecyclerView();

    }
    private void updateRecyclerView() {

        //add call to function to grab from database
        //controller.getPuzzleList();
        ArrayList<PuzzleListItem> temp = new ArrayList<>();
        temp.add(new PuzzleListItem(1, "Default"));
      //  List<PuzzleListItem> puzzlesTemp = new ArrayList<>(Arrays.asList(this.puzzles));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, temp);
        binding.items.setHasFixedSize(true);
        binding.items.setLayoutManager(new LinearLayoutManager(this));
        binding.items.setAdapter(adapter);


    }



    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

        String name = evt.getPropertyName();
        Object value = evt.getNewValue();

        if (name.equals(CrosswordMagicController.PUZZLE_LIST_PROPERTY)) {

            if (value instanceof PuzzleListItem[]) {

                PuzzleListItem[] puzzles = (PuzzleListItem[])value;
                this.puzzles = puzzles;



            }

        }

    }




}
