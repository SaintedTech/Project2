package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.DownloadListItemBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.DownloadPageBinding;

import java.util.List;

import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private DownloadListItemBinding binding;
    private List<PuzzleListItem> data;

    private DownloadMenuActivity activity;


    public RecyclerViewAdapter(DownloadMenuActivity activity, List<PuzzleListItem> data) {

        super();
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DownloadListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        ViewHolder holder = new ViewHolder(binding.getRoot());
        binding.getRoot().setOnClickListener(activity.getItemClick()); // the click handler
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPuzzleList(data.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        Log.d("Download", String.valueOf(data.size()));
        return data.size();
    }
    public PuzzleListItem getListItem(int position){
        return this.data.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private PuzzleListItem puzzleListItem;
        private TextView textLabel;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public PuzzleListItem getPuzzleListItem() {
            return puzzleListItem;
        }

        public void setPuzzleList(PuzzleListItem item) {
            this.puzzleListItem = item;
        }

        public void bindData() {

            if (textLabel == null) {
                textLabel = (TextView) itemView.findViewById(R.id.textLabel);
            }

            textLabel.setText(puzzleListItem.getId().toString() + ": " +  puzzleListItem.toString());


        }

    }
}
