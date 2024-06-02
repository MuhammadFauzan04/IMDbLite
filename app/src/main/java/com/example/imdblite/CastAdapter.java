package com.example.imdblite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private List<Cast> castList;
    private Context context;

    public CastAdapter(Context context) {
        this.context = context;
        this.castList = new ArrayList<>();
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Cast cast = castList.get(position);
        holder.tvCastName.setText(cast.getName());
        Glide.with(context).load(cast.getProfilePath()).into(holder.ivCast);
    }

    @Override
    public int getItemCount() {
        return castList != null ? castList.size() : 0;
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        TextView tvCastName;
        ImageView ivCast;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCastName = itemView.findViewById(R.id.tv_cast);
            ivCast = itemView.findViewById(R.id.iv_cast);
        }
    }
}
