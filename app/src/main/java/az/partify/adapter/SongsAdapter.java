package az.partify.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import az.partify.R;
import az.partify.model.Song;

/**
 * Created by az on 22/05/16.
 */
public class SongsAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Song> mSongList;

    public SongsAdapter(Context context, ArrayList<Song> partyList) {
        mContext = context;
        mSongList = partyList;
    }

    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Song getItem(int position) {
        return mSongList.get(position);
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
            convertView = inflater.inflate(R.layout.song_list_item_layout, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.songNameTV = (TextView) convertView.findViewById(R.id.song_name);
            viewHolder.songArtistTV = (TextView) convertView.findViewById(R.id.song_artist);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Song tmpSong = mSongList.get(position);

        if(tmpSong != null) {
            viewHolder.songNameTV.setText(tmpSong.songName);
            viewHolder.songArtistTV.setText("(" + tmpSong.songArtist + ")");
        }

        return convertView;
    }

    static class ViewHolderItem {
        TextView songNameTV;
        TextView songArtistTV;
    }
}
