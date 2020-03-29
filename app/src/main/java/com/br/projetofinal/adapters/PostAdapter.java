package com.br.projetofinal.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.Base64;
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
    private final List<Post> posts = new ArrayList<>();

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_post, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final int idBackground =
                position % 2 == 0 ? R.drawable.post_background_bottom : R.drawable.post_background_top,
                range1 = 106, range2 = 146;
        final Context context = holder.itemView.getContext();
        Drawable drawable = ContextCompat.getDrawable(context, idBackground);
        double[] rgb = {
                Math.random() * range1 + range2,
                Math.random() * range1 + range2,
                Math.random() * range1 + range2
        };
        final String referenceImage = posts.get(position).getImageReference();
        final byte[] bytesId = Base64.decode(posts.get(position).getIdUser(), Base64.URL_SAFE);
        final String emailUser = new String(bytesId);

        if (drawable != null) drawable.setTint(Color.rgb((int) rgb[0], (int) rgb[1], (int) rgb[2]));
        if (referenceImage != null && !referenceImage.isEmpty())
            MySystem.getImageIn(referenceImage, holder.imagePost::setImageBitmap);
        else holder.imagePost.getLayoutParams().height = 0;

        MySystem.getUserById(posts.get(position).getIdUser(), user -> holder.nameUser.setText(user.getName()));
        MySystem.getImageIn("profile_img_" + emailUser, holder.imageProfile::setImageBitmap);
        holder.itemView.setBackground(drawable);
        {
            for (int i = 0; i < rgb.length; i++) rgb[i] -= 40;
            drawable = ContextCompat.getDrawable(context, R.drawable.round_template);
            if (drawable != null)
                drawable.setTint(Color.rgb((int) rgb[0], (int) rgb[1], (int) rgb[2]));
        }
        holder.itemView.findViewById(R.id.constraintLayout3).setBackground(drawable);
        holder.title.setText(posts.get(position).getTitle());
        holder.text.setText(posts.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return posts;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagePost;
        private final ImageView imageProfile;
        private TextView nameUser;
        private TextView title;
        private TextView text;
        private final View itemView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_post_title);
            this.text = itemView.findViewById(R.id.item_post_text);
            this.imagePost = itemView.findViewById(R.id.item_post_img);
            this.imagePost.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final float value = 25 * Resources.getSystem().getDisplayMetrics().density;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), value);
                }
            });
            imageProfile = itemView.findViewById(R.id.item_post_img_profile);
            imageProfile.setClipToOutline(true);
            this.imagePost.setClipToOutline(true);
            nameUser = itemView.findViewById(R.id.item_post_name_user);
            this.itemView = itemView;
        }
    }
}
