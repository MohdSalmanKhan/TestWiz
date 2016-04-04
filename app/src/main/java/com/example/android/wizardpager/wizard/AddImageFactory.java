package com.example.android.wizardpager.wizard;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.wizardpager.R;
import com.example.android.wizardpager.wizard.Interfaces.*;
import com.example.android.wizardpager.wizard.Interfaces.CommonListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by salman on 18/03/16.
 */
public class AddImageFactory implements FormWidgetFactory  {
    ExpandableHeightGridView gridView;

    GridAdapter gridAdapter;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener) throws Exception {
        List<View> views = new ArrayList<View>();

        gridView = (ExpandableHeightGridView) LayoutInflater.from(context).inflate(R.layout.layout_grid, null);

        gridAdapter = new GridAdapter(context, R.layout.layout_grid);
        gridAdapter.add("");
        gridView.setTag(R.id.key, jsonObject.getString("key"));
        gridView.setTag(R.id.type, jsonObject.getString("type"));
        gridView.setTag(R.id.est_plt_images_grid, "");
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(listener);
        gridView.setExpanded(true);


        if(jsonObject.has("value")){
            String path = jsonObject.getString("value");
            String allPaths[] = path.split(",");
            for(String s : allPaths){
                gridAdapter.add(s);
            }
            gridAdapter.notifyDataSetChanged();
        }

        views.add(gridView);
        return views;
    }

    public class GridAdapter extends ArrayAdapter<String>{
        private LayoutInflater mInflater;
        private ImageView squareImageView;
        private Context context;

        public GridAdapter(Context context, int resource) {
            super(context, resource);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_grid_image, null);
            }
            squareImageView = (ImageView) ViewHolder.get(convertView, R.id.platinum_grid_image);

            if ( position == 0){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    squareImageView.setImageDrawable(context.getDrawable(R.drawable.add_photo));
                } else if ( Build.VERSION.SDK_INT >= 16 ){
                    squareImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.add_photo));
                } else {
                    squareImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.add_photo));
                }
            }
            else{
                String url = getItem(position);
                float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
                Bitmap bitmap = ImageUtils.loadBitmapFromFile(url, (int) pixels , (int) pixels);
                squareImageView.setImageBitmap(bitmap);
            }


            return convertView;
        }
    }

    public static class ViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }

}
