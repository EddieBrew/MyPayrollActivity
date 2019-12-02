package com.example.mypayrollactivity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
	private static final String TAG = "RecyclerViewAdapter";
    //variables

	private List<DailyInfoModel> list = new ArrayList<>();
	private Integer[] pics;
	private Context mContext;
	ImageView imageView1;
	public RecyclerViewAdapter(Context mContext, List<DailyInfoModel> list, Integer[] pics ) {
		this.mContext = mContext;
		this.pics = pics;
		this.list = list;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_layout, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		imageView1 = view.findViewById(R.id.imageView1);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


		holder.imgIconH.setImageResource(pics[getImageInt(list.get(position).getDay())]);
		holder.textViewDateH.setText(list.get(position).getDate());
		holder.textViewEventNumberH.setText( "Event Number: " + list.get(position).getEventNumber());
		holder.textViewEventNameH.setText("Event Name: " + list.get(position).getEventName());
		holder.textViewTimeH.setText("Hours: " + list.get(position) .getTime());
		holder.textViewBHourH.setText( "Regular Hours = " + String.format("%.2f", list.get(position).getRhours()));
		holder.textViewExtraHourH.setText("OT Hours = " + String.format("%.2f", list.get(position).getOhours()));


		String myColors = " " ;
		switch(getImageInt(list.get(position).getDay())){
			case 0 :
				myColors = "#FFFF0000";//red
				break;
			case 1:
				myColors = "#FFFFBB33";//orange
				break;
			case 2:
				myColors = "#FFFFC0CB";//pink
				break;
			case 3:
				myColors = "#FF008000";//green
				break;
			case 4 :
				myColors = "#FF0099CC";//aqua blue
				break;
			case 5:
				myColors = "#FF000080";//navy blue
				break;
			case 6:
				myColors = "#FFAA66CC";//purple
				break;
			default:
		}


		holder.textViewDateH.setTextColor(Color.parseColor(myColors));
		holder.textViewEventNumberH.setTextColor(Color.parseColor(myColors));
		holder.textViewEventNameH.setTextColor(Color.parseColor(myColors));
		holder.textViewTimeH.setTextColor(Color.parseColor(myColors));
		holder.textViewBHourH.setTextColor(Color.parseColor(myColors));
		holder.textViewExtraHourH.setTextColor(Color.parseColor(myColors));


		switch(getYear(list.get(position).getDate())){
			case 2017:imageView1.setImageResource(R.drawable.year2017);
				break;
			case 2018:imageView1.setImageResource(R.drawable.year2018);
				break;
			case 2019: imageView1.setImageResource(R.drawable.year2019);
				break;
			case 2020: imageView1.setImageResource(R.drawable.year2020);
				break;
			default:
		}



	}


	@Override
	public int getItemCount() {
		return list.size();
	}




	public class ViewHolder extends RecyclerView.ViewHolder{
		ImageView imgIconH;
		TextView textViewDateH;
		TextView textViewEventNumberH;
		TextView textViewEventNameH;
		TextView textViewTimeH;
		TextView textViewBHourH;
		TextView textViewExtraHourH;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			imgIconH = (ImageView)itemView.findViewById(R.id.imageView);
			textViewDateH = (TextView)itemView.findViewById(R.id.textViewDate);
			textViewEventNameH = (TextView)itemView.findViewById(R.id.textViewEventName);
			textViewEventNumberH = (TextView)itemView.findViewById(R.id.textViewEventNumber);
			textViewTimeH = (TextView)itemView.findViewById(R.id.textViewTime);
			textViewBHourH = (TextView)itemView.findViewById(R.id.textViewrHours);//regular hours
			textViewExtraHourH = (TextView)itemView.findViewById(R.id.textViewoHours);//overtime hours

		}
	}

	public int getImageInt(String day){

		if(day.equals("SUN")) return 0 ;
		if(day.equals("MON")) return 1 ;
		if(day.equals("TUE")) return 2 ;
		if(day.equals("WED")) return 3 ;
		if(day.equals("THU")) return 4 ;
		if(day.equals("FRI")) return 5 ;
		if(day.equals("SAT")) return 6 ;

		return 6;
	}

	Integer getYear(String year){
		String delimStr = "/";  //date format is mm/dd/yyyy
		String[] words = year.split(delimStr);

		return Integer.parseInt(words[2]);

	}
}
