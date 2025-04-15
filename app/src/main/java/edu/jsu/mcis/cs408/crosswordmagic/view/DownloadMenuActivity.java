package edu.jsu.mcis.cs408.crosswordmagic.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.ActivityWelcomeBinding;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.DownloadPageBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;
import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadMenuActivity extends AppCompatActivity implements AbstractView {

    private static String TAG = "DownloadMenuActivity";
    private DownloadPageBinding binding;
    private CrosswordMagicController controller =  new CrosswordMagicController();
    private CrosswordMagicModel model;

    private Integer puzzleid = -1;
    private PuzzleListItem[] puzzles;
    private ArrayList<PuzzleListItem> puzzleListItemsArrayList = new ArrayList<>();
    private final PuzzleListItemClickHandler itemClick = new PuzzleListItemClickHandler();
    private JSONObject data;
    private RecyclerViewAdapter adapter;

    private ArrayList<Puzzle> puzzleArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Log.e("FATAL", "Uncaught exception in thread: " + t.getName(), e);
        });

        adapter = new RecyclerViewAdapter(this, this.puzzleListItemsArrayList);
        /*
        binding.items.setHasFixedSize(true);
        binding.items.setLayoutManager(new LinearLayoutManager(this));
        binding.items.setAdapter(adapter);
        */
         

        model = new CrosswordMagicModel(this, 1);

        controller.addModel(model);
        controller.addView(this);

        super.onCreate(savedInstanceState);
        binding = DownloadPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //updateRecyclerView();

        Context passing = this;


        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add passing
                Intent i = new Intent(passing, MainActivity.class);
                i.putExtra("puzzleid", 1);
                startActivity(i);

            }
        });


      controller.getPuzzlesFromWeb();


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

        //add call to function to grab from webservices


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, this.puzzleListItemsArrayList);
        binding.items.setHasFixedSize(true);
        binding.items.setLayoutManager(new LinearLayoutManager(this));
        binding.items.setAdapter(adapter);


    }
    private void parseData()  {
        try {
            PuzzleListItem temp = new PuzzleListItem(1, data.get("name").toString());
            puzzleListItemsArrayList.add(temp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



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
        if(name.equals(CrosswordMagicController.PUZZLE_GET_REQUEST)){{
            if(value instanceof JSONObject){
                this.data = (JSONObject) value;
                parseData();
                //updateRecyclerView();
                adapter.notifyDataSetChanged();
            }
        }

        }

    }




}
