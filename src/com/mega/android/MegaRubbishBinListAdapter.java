package com.mega.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MegaRubbishBinListAdapter extends BaseAdapter implements OnClickListener{
	
	Context context;
	List<ItemFileBrowser> rowItems;
	int positionClicked;
	ArrayList<Integer> imageIds;
	ArrayList<String> names;


	public MegaRubbishBinListAdapter(Context _context, List<ItemFileBrowser> _items) {
		this.context = _context;
		this.rowItems = _items;
		this.positionClicked = -1;
		this.imageIds = new ArrayList<Integer>();
		this.names = new ArrayList<String>();
		
		Iterator<ItemFileBrowser> it = rowItems.iterator();
		while (it.hasNext()){
			ItemFileBrowser item = it.next();
			imageIds.add(item.getImageId());
			names.add(item.getName());
		}
	}
	
	/*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView textViewFileName;
        TextView textViewFileSize;
        TextView textViewUpdated;
        ImageButton imageButtonThreeDots;
        RelativeLayout itemLayout;
        ImageView arrowSelection;
        RelativeLayout optionsLayout;
        ImageButton optionUndo;
        ImageButton optionDeletePermanently;
        int currentPosition;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		final int _position = position;
		
		ViewHolder holder = null;
		
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);
	    float density  = ((Activity)context).getResources().getDisplayMetrics().density;
		
	    float scaleW = Util.getScaleW(outMetrics, density);
	    float scaleH = Util.getScaleH(outMetrics, density);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_rubbishbin_list, parent, false);
			holder = new ViewHolder();
			holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.rubbishbin_list_item_layout);
			holder.imageView = (ImageView) convertView.findViewById(R.id.rubbishbin_list_thumbnail);
			holder.textViewFileName = (TextView) convertView.findViewById(R.id.rubbishbin_list_filename);
			holder.textViewFileSize = (TextView) convertView.findViewById(R.id.rubbishbin_list_filesize);
			holder.textViewUpdated = (TextView) convertView.findViewById(R.id.rubbishbin_list_updated);
			holder.imageButtonThreeDots = (ImageButton) convertView.findViewById(R.id.rubbishbin_list_three_dots);
			holder.optionsLayout = (RelativeLayout) convertView.findViewById(R.id.rubbishbin_list_options);
			holder.optionUndo = (ImageButton) convertView.findViewById(R.id.rubbishbin_list_option_undo);
			holder.optionUndo.setPadding(Util.px2dp((87*scaleW), outMetrics), Util.px2dp((10*scaleH), outMetrics), 0, 0);
			holder.optionDeletePermanently = (ImageButton) convertView.findViewById(R.id.rubbishbin_list_option_delete_permanently);
			holder.optionDeletePermanently.setPadding(Util.px2dp((75*scaleW), outMetrics), Util.px2dp((10*scaleH), outMetrics), Util.px2dp((30*scaleW), outMetrics), 0);
			holder.arrowSelection = (ImageView) convertView.findViewById(R.id.rubbishbin_list_arrow_selection);
			holder.arrowSelection.setVisibility(View.GONE);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.currentPosition = position;

		ItemFileBrowser rowItem = (ItemFileBrowser) getItem(position);
		
		holder.textViewFileName.setText(rowItem.getName());
		holder.textViewFileSize.setText("100 KB");
		holder.textViewUpdated.setText("Updated: 1 week ago");
		holder.imageView.setImageResource(rowItem.getImageId());
		
		holder.imageButtonThreeDots.setTag(holder);
		holder.imageButtonThreeDots.setOnClickListener(this);
		
		if (positionClicked != -1){
			if (positionClicked == position){
				holder.arrowSelection.setVisibility(View.VISIBLE);
				LayoutParams params = holder.optionsLayout.getLayoutParams();
				params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
				holder.itemLayout.setBackgroundColor(context.getResources().getColor(R.color.file_list_selected_row));
				holder.imageButtonThreeDots.setImageResource(R.drawable.three_dots_background_grey);
				ListView list = (ListView) parent;
				list.smoothScrollToPosition(_position);
			}
			else{
				holder.arrowSelection.setVisibility(View.GONE);
				LayoutParams params = holder.optionsLayout.getLayoutParams();
				params.height = 0;
				holder.itemLayout.setBackgroundColor(Color.WHITE);
				holder.imageButtonThreeDots.setImageResource(R.drawable.three_dots_background_white);
			}
		}
		else{
			holder.arrowSelection.setVisibility(View.GONE);
			LayoutParams params = holder.optionsLayout.getLayoutParams();
			params.height = 0;
			holder.itemLayout.setBackgroundColor(Color.WHITE);
			holder.imageButtonThreeDots.setImageResource(R.drawable.three_dots_background_white);
		}
		
		holder.optionUndo.setTag(holder);
		holder.optionUndo.setOnClickListener(this);
		
		holder.optionDeletePermanently.setTag(holder);
		holder.optionDeletePermanently.setOnClickListener(this);
		
		return convertView;
	}

	@Override
    public int getCount() {
        return rowItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }    
    
    public int getPositionClicked (){
    	return positionClicked;
    }
    
    public void setPositionClicked(int p){
    	positionClicked = p;
    }

	@Override
	public void onClick(View v) {

		ViewHolder holder = (ViewHolder) v.getTag();
		int currentPosition = holder.currentPosition;
		
		switch(v.getId()){
			case R.id.rubbishbin_list_three_dots:{
				if (positionClicked == -1){
					positionClicked = currentPosition;
					notifyDataSetChanged();
				}
				else{
					if (positionClicked == currentPosition){
						positionClicked = -1;
						notifyDataSetChanged();
					}
					else{
						positionClicked = currentPosition;
						notifyDataSetChanged();
					}
				}
				break;
			}
			case R.id.rubbishbin_list_option_undo:{
				Toast.makeText(context, "Undo_position"+currentPosition, Toast.LENGTH_SHORT).show();
				positionClicked = -1;
				notifyDataSetChanged();
				break;
			}
			case R.id.rubbishbin_list_option_delete_permanently:{
				Toast.makeText(context, "Delete permanently_position"+currentPosition, Toast.LENGTH_SHORT).show();
				positionClicked = -1;
				notifyDataSetChanged();
				break;
			}
		}
	}
}