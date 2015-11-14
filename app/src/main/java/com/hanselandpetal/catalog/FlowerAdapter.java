package com.hanselandpetal.catalog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanselandpetal.catalog.model.Flower;

import java.util.List;

public class FlowerAdapter extends ArrayAdapter<Flower> {

	private Context context;
	@SuppressWarnings("unused")
	private List<Flower> flowerList;

	public FlowerAdapter(Context context, int resource, List<Flower> objects) {
		super(context, resource, objects);
		this.context = context;
		this.flowerList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_flower, parent, false);

		//Display flower name in the TextView widget
		Flower flower = flowerList.get(position);
        TextView textView = (TextView) view.findViewById(R.id.textView1);
        textView.setText(flower.getName());

        //Display flower image in the Imageview widget
        ImageView  imageView = (ImageView) view.findViewById(R.id.imageView1);
        imageView.setImageBitmap(flower.getBitmap());

		return view;
	}

}
