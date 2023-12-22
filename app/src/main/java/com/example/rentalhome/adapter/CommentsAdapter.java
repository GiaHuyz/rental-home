package com.example.rentalhome.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhome.R;
import com.example.rentalhome.dto.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private String ownerId;

    public CommentsAdapter(List<Comment> commentList, String ownerId) {
        this.commentList = commentList;
        this.ownerId = ownerId;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_list_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        String displayName = comment.getName();

        if (comment.getUserId().equals(ownerId)) {
            displayName += " (Owner)";
        }

        holder.tvName.setText(displayName);
        holder.tvComment.setText(comment.getComment());
        holder.tvTime.setText(comment.getTime());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvComment, tvTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public void updateCommentList(List<Comment> newCommentList) {
        commentList.clear();
        commentList.addAll(newCommentList);
        notifyDataSetChanged();
    }
}
