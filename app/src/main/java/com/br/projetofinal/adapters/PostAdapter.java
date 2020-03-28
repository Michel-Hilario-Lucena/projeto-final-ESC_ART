package com.br.projetofinal.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.br.projetofinal.R;
import com.br.projetofinal.models.Post;
import com.br.projetofinal.utils.MySystem;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> posts=new ArrayList<>();
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final int idBackground =
                position % 2 == 0 ? R.drawable.post_background_bottom : R.drawable.post_background_top,
                range1 = 126, range2 = 127;
        final int color = Color.rgb((int) (
                        Math.random() * range1 + range2),
                (int) (Math.random() * range1 + range2),
                (int) (Math.random() * range1 + range2)
        );
        final Context context = holder.getItemView().getContext();
        final Drawable drawable = ContextCompat.getDrawable(context, idBackground);

        final String referenceImage = posts.get(position).getImageReference();
        if (drawable != null) drawable.setTint(color);

        if (referenceImage != null && !referenceImage.isEmpty())
            MySystem.getImageIn(referenceImage, bitmap -> holder.getImageView().setImageBitmap(bitmap));
        else holder.getImageView().getLayoutParams().height = 0;
        holder.getItemView().setBackground(drawable);
        holder.getTitle().setText(posts.get(position).getTitle());
        holder.getText().setText(posts.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return posts;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private TextView title;
        private TextView text;
        private final View itemView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_post_title);
            this.text = itemView.findViewById(R.id.item_post_text);
            this.imageView = itemView.findViewById(R.id.imageView2);
            this.imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final float value = 25 * Resources.getSystem().getDisplayMetrics().density;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), value);
                }
            });
            ImageView imageProfile = itemView.findViewById(R.id.imageView);
            imageProfile.setClipToOutline(true);
            this.imageView.setClipToOutline(true);
            this.itemView = itemView;
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getTitle() {
            return title;
        }

        public TextView getText() {
            return text;
        }

        public void setText(TextView text) {
            this.text = text;
        }

        View getItemView() {
            return itemView;
        }

    }
}
