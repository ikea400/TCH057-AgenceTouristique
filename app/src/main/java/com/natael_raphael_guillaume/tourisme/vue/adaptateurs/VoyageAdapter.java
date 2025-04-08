package com.natael_raphael_guillaume.tourisme.vue.adaptateurs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VoyageAdapter extends ArrayAdapter<Voyage> {

    private Context context;
    private int viewResourceId;
    private List<Voyage> voyages;
    private Resources resources;
    private final OkHttpClient client = new OkHttpClient();

    public VoyageAdapter(@NonNull Context context, int viewResourceId, @NonNull List<Voyage> voyages) {
        super(context, viewResourceId, voyages);
        this.context = context;
        this.viewResourceId = viewResourceId;
        this.voyages = voyages;
        this.resources = context.getResources();
    }

    @Override
    public int getCount() {
        return voyages.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(viewResourceId, parent, false);
        }

        final Voyage voyage = voyages.get(position);

        if (voyage != null) {
            TextView titre = view.findViewById(R.id.detailTitre);
            TextView destination = view.findViewById(R.id.detailDestination);
            TextView prix = view.findViewById(R.id.detailPrix);
            TextView description = view.findViewById(R.id.detailDescription);
            ImageView image = view.findViewById(R.id.detailImage);

            titre.setText(voyage.getNom_voyage());
            destination.setText(voyage.getDestination());
            prix.setText(String.format(Locale.getDefault(), "%.2f$", voyage.getPrix()));
            description.setText(voyage.getDescription());

            // get the image from the web using okhttp inside a thread to not freeze the app
            Glide.with(view)
                    .load(voyage.getImage_url())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))//this line optional - you can skip this line
                    .into(image);
            /*new Thread(() -> {
                Request request = new Request.Builder().url(voyage.getImage_url()).build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful() || response.body() == null) {
                        throw new IOException("Error with image thread " + response);
                    }
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    // update UI on the main thread
                    image.post(() -> {
                        if (bitmap != null) {
                            image.setImageBitmap(bitmap);
                        } else {
                            image.setImageResource(R.drawable.ic_launcher_foreground); // placeholder if image fails to load
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    image.post(() -> image.setImageResource(R.drawable.ic_launcher_foreground));
                }
            }).start();*/
        }
        return view;
    }
}