package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.DAOFactory;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.PuzzleDAO;
import edu.jsu.mcis.cs408.crosswordmagic.view.CrosswordGridView;

public class CrosswordMagicModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;

    private Puzzle puzzle;

    public CrosswordMagicModel(Context context) {

        DAOFactory daoFactory = new DAOFactory(context);
        PuzzleDAO puzzleDAO = daoFactory.getPuzzleDAO();

        this.puzzle = puzzleDAO.find(DEFAULT_PUZZLE_ID);


    }

    public void getTestProperty() {

        String wordCount = (this.puzzle != null ? String.valueOf(puzzle.getSize()) : "none");
        firePropertyChange(CrosswordMagicController.TEST_PROPERTY, null, wordCount);

    }
    public void getGridDimensions(){
        Integer[] dims = new Integer[2];
        dims[0] = puzzle.getHeight();
        dims[1] = puzzle.getWidth();
        firePropertyChange(CrosswordMagicController.GRID_DIMENSION_PROPERTY, null, dims);

    }
    public void getGridLetters(){

        firePropertyChange(CrosswordMagicController.GRID_LETTERS_PROPERTY, null, puzzle.getLetters());


    }
    public void getGridNumbers(){
        firePropertyChange(CrosswordMagicController.GRID_NUMBERS_PROPERTY, null, puzzle.getNumbers());
    }
    public void getClues(){
        String[] clues = {puzzle.getCluesAcross(), puzzle.getCluesDown()};
        firePropertyChange(CrosswordMagicController.CLUE_WORDS_PROPERTY, null, clues);
        
    }
    public void setGuess(String[] parcel){

        Character[][] copy = puzzle.getLetters();

        int number = Integer.parseInt(parcel[0]);
        if(puzzle.checkGuess(number, parcel[1]) != null){

             getGridLetters();
            firePropertyChange(CrosswordMagicController.GUESS_WORD_PROPERTY, null, true);

        }else{
            firePropertyChange(CrosswordMagicController.GUESS_WORD_PROPERTY, null, false);
        }





    }

}