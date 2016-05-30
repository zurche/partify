package az.partify.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.partify.R;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by az on 22/05/16.
 */
public class TracksAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Track> mTracks;

    public TracksAdapter(List<Track> tracks, Context context) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.track_list_item_layout, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.songNameTV = (TextView) convertView.findViewById(R.id.track_name);
            viewHolder.songArtistTV = (TextView) convertView.findViewById(R.id.track_artist);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Track track = mTracks.get(position);

        if(track != null) {
            viewHolder.songNameTV.setText(track.name);
            viewHolder.songArtistTV.setText("(" + track.artists.get(0).name + ")");
        }

        return convertView;
    }

    static class ViewHolderItem {
        TextView songNameTV;
        TextView songArtistTV;
    }
}
