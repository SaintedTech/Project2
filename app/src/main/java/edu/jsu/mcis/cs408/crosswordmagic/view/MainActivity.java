package edu.jsu.mcis.cs408.crosswordmagic.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.controller.AbstractController;
import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.ActivityMainBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;

public class MainActivity extends AppCompatActivity implements AbstractView {


    private final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private CrosswordMagicController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /* Create Controller and Model */

        controller = new CrosswordMagicController();

        int puzzleid = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            puzzleid = extras.getInt("puzzleid");
        }

        CrosswordMagicModel model = new CrosswordMagicModel(this, new Integer(puzzleid));

        /* Register View(s) and Model(s) with Controller */

        controller.addModel(model);
        controller.addView(this);

        /* Get Test Property (tests MVC framework) */

        controller.getTestProperty(CrosswordMagicController.TEST_PROPERTY);


    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        String name = evt.getPropertyName();
        String value = evt.getNewValue().toString();

        if (name.equals(CrosswordMagicController.TEST_PROPERTY)) {

            String placeholder = getString(R.string.placeholder_text) + value;

            //binding.output.setText(placeholder);

        }

    }

    public CrosswordMagicController getController(){
        return this.controller;
    }

}