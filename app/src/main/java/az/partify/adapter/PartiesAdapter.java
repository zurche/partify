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
import az.partify.model.Party;

/**
 * Created by az on 22/05/16.
 */
public class PartiesAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Party> mPartyList;

    public PartiesAdapter(Context context, ArrayList<Party> partyList) {
        mContext = context;
        mPartyList = partyList;
    }

    @Override
    public int getCount() {
        return mPartyList.size();
    }

    @Override
    public Party getItem(int position) {
        return mPartyList.get(position);
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
            convertView = inflater.inflate(R.layout.party_list_item_layout, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.party_name);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Party objectItem = mPartyList.get(position);

        if(objectItem != null) {
            viewHolder.textViewItem.setText(objectItem.name);
        }

        return convertView;
    }

    static class ViewHolderItem {
        TextView textViewItem;
    }
}
