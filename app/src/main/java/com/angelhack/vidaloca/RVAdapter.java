package com.angelhack.vidaloca;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tejas on 5/14/2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.VidaVideoViewHolder>{

    List<VidaVideo> mVideos;

    RVAdapter(List<VidaVideo> videos){
        this.mVideos = videos;
    }

    @Override
    public VidaVideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_item, viewGroup, false);
        VidaVideoViewHolder pvh = new VidaVideoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(VidaVideoViewHolder videoViewHolder, int i) {
        videoViewHolder.videoTitle.setText(mVideos.get(i).mTitle);
        videoViewHolder.videoDescription.setText(mVideos.get(i).mDescription);
        videoViewHolder.videoPhoto.setImageResource(mVideos.get(i).photoId);
        videoViewHolder.videoDuration.setText(mVideos.get(i).mDuration);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public static class VidaVideoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView videoTitle;
        TextView videoDescription;
        ImageView videoPhoto;
        TextView videoDuration;

        VidaVideoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            videoTitle = (TextView)itemView.findViewById(R.id.person_name);
            videoDescription = (TextView)itemView.findViewById(R.id.person_age);
            videoPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            videoDuration = (TextView) itemView.findViewById(R.id.time_stamp);
        }
    }

}
