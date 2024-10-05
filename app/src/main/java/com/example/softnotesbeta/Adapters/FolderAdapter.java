package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.FolderCLickListener;
import com.example.softnotesbeta.OnTaskClickListener;
import com.example.softnotesbeta.R;

public class FolderAdapter extends PagedListAdapter<Folder, FolderAdapter.FolderViewHolder> {

    private Context context;
    private FolderCLickListener listener;

    public FolderAdapter(Context context, FolderCLickListener listener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item_layout, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = getItem(position);

        if (folder != null) {
            holder.titleTv.setText(folder.getName());

            listener.onFolderClicked(folder.getId(), folder.getName());
        }
    }

    private static DiffUtil.ItemCallback<Folder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Folder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Folder oldItem, @NonNull Folder newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Folder oldItem, @NonNull Folder newItem) {
            return oldItem.equals(newItem);
        }
    };

    class FolderViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView titleTv;
        private AppCompatTextView dateTv;
        private AppCompatTextView detailsTv;


        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = (AppCompatTextView) itemView.findViewById(R.id.folder_name);
            dateTv = (AppCompatTextView) itemView.findViewById(R.id.tv_date);
            detailsTv = (AppCompatTextView) itemView.findViewById(R.id.folder_details);
        }
    }
}
