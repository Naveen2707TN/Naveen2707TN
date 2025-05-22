package com.voiceawin.vaplayer.Adapter;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.voiceawin.vaplayer.Activity.VideoActivity;
import com.voiceawin.vaplayer.Modul.VideoFile;
import com.voiceawin.vaplayer.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyHolder> {

    public Context context;
    public ArrayList<VideoFile> videoFiles;

    public VideoAdapter(Context context, ArrayList<VideoFile> videoFiles) {
        this.context = context;
        this.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public VideoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.MyHolder holder, int position) {
        holder.file_name.setText(videoFiles.get(position).getFileName());
        holder.location.setText(videoFiles.get(position).getDuration());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetTheme);
                View bsv = LayoutInflater.from(context).inflate(R.layout.video_detail,null);
                bsv.findViewById(R.id.down_arrow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bsv.findViewById(R.id.share_img);
                bsv.findViewById(R.id.delete_img);
                bsv.findViewById(R.id.info_img);

                bsv.findViewById(R.id.s_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareFiles(position);
                        bottomSheetDialog.dismiss();
                    }
                });
                bsv.findViewById(R.id.d_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFile(position);
                        bottomSheetDialog.dismiss();
                    }
                });
                bsv.findViewById(R.id.i_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProperties(position);
                        bottomSheetDialog.dismiss();
                    }
                });


                bottomSheetDialog.setContentView(bsv);
                bottomSheetDialog.show();
            }
        });
        Glide.with(context).
                load(new File(videoFiles.get(position).getPath())).
                into(holder.thumb);
        int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("pos",i);
                intent.putExtra("name",videoFiles.get(i).getFileName());
                intent.putExtra("duration",videoFiles.get(i).getDuration());
                intent.putExtra("path",videoFiles.get(i).getPath());
                intent.putExtra("size",videoFiles.get(i).getSize());
                intent.putExtra("res",videoFiles.get(position).getResolution());
                context.startActivity(intent);
            }
        });
    }

    private void shareFiles(int i){
        Uri uri = Uri.parse(videoFiles.get(i).getPath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(Intent.createChooser(intent,"share"));
        Toast.makeText(context, "loading . . .", Toast.LENGTH_SHORT).show();
    }

    private void deleteFile(int  i){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this file now ?")
                .setMessage(videoFiles.get(i).getFileName())
                .setNegativeButton("Cancel", (dialog, which) -> {
                   dialog.dismiss();
                }).setPositiveButton("Ok",(dialog, which) -> {
                    Uri delete = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,Long.parseLong(videoFiles.get(i).getId()));
                    File file = new File(videoFiles.get(i).getPath());
                    boolean deleted = file.delete();
                    if (deleted){
                        context.getApplicationContext().getContentResolver().delete(delete,null,null);
                        videoFiles.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i,videoFiles.size());
                        Toast.makeText(context, "file deleted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "cant able to delete the file", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    public void showProperties(int i){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.file_detail);

        String name = videoFiles.get(i).getFileName();
        String path = videoFiles.get(i).getPath();
        String str = path.replace("/storage/emulated/0/","");
        String size = videoFiles.get(i).getSize();
        String can = readableFileSize(Long.parseLong(size));
        String duration = videoFiles.get(i).getDuration();
        String resolution = videoFiles.get(i).getResolution();

        TextView nt = dialog.findViewById(R.id.file_name_detail);
        TextView pt = dialog.findViewById(R.id.loaction_detail);
        TextView st = dialog.findViewById(R.id.size_detail);
        TextView dt = dialog.findViewById(R.id.duration_detail);
        TextView rt = dialog.findViewById(R.id.resolution_detail);

        nt.setText(name);
        pt.setText(str);
        st.setText(can);
        dt.setText(duration);
        rt.setText(resolution);

        dialog.show();
    }
    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView thumb,more;
        TextView file_name,location;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            more = itemView.findViewById(R.id.moe_icon);
            file_name = itemView.findViewById(R.id.file_name);
            location = itemView.findViewById(R.id.loaction_video);
        }
    }

    public void updateVideoFile(ArrayList<VideoFile> file){
        videoFiles = new ArrayList<>();
        videoFiles.addAll(file);
        notifyDataSetChanged();
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
