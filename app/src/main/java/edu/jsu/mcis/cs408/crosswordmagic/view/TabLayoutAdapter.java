package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabLayoutAdapter extends FragmentStateAdapter {
    public static final int NUM_TABS = 2;

    public TabLayoutAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();


        switch (position) {
            case 0:
                PuzzleFragment puzzleFragment = new PuzzleFragment();
                args.putInt("id", position + 1);
                puzzleFragment.setArguments(args);
                return puzzleFragment;
            case 1:
                ClueFragment clueFragment = new ClueFragment();
                args.putInt("id", position + 1);
                clueFragment.setArguments(args);
                return clueFragment;
            default:
                throw new IllegalArgumentException("Invalid tab position: " + position);
        }


    }
    @Override
    public int getItemCount() { return NUM_TABS; }
}
