package com.example.topreddit.ui.adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topreddit.R;
import com.example.topreddit.domain.pojo.PostData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostData> postDatas;
    private OnImageClickListener onImageClickListener;
    private OnPostClickListener onPostClickListener;
    private static final String HTTP_FORMAT = "http";

    public PostAdapter() {
        postDatas = new ArrayList<>();
    }

    public void setPostDatas(List<PostData> postDatas) {
        this.postDatas = postDatas;
        notifyDataSetChanged();
    }

    public List<PostData> getPostDatas() {
        return postDatas;
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostData postData = postDatas.get(position);
        String thumnailImage = postData.getThumbnailImage();
        if (thumnailImage != null && thumnailImage.contains(HTTP_FORMAT)) {
            Picasso.get().load(postData.getThumbnailImage()).into(holder.imageViewThumbnail);
        } else {
            Picasso.get().load(R.drawable.redditicon).into(holder.imageViewThumbnail);
        }
        holder.textViewAuthor.setText(postData.getAuthor());
        holder.textViewTitle.setText(postData.getTitle());
        holder.textViewTime.setText(DateUtils.getRelativeTimeSpanString(postData.getCreatedUtc()).toString());
        holder.textViewNumberOfComments.setText(Integer.toString(postData.getNumComments()));
    }

    @Override
    public int getItemCount() {
        return postDatas.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewImage)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewAuthor)
        TextView textViewAuthor;
        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewTime)
        TextView textViewTime;
        @BindView(R.id.textViewNumberOfComments)
        TextView textViewNumberOfComments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onImageClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPostClickListener!= null) {
                        onPostClickListener.onPostClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
