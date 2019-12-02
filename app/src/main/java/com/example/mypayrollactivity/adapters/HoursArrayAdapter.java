package com.example.mypayrollactivity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mypayrollactivity.R;
import com.example.mypayrollactivity.myClasses.DailyInfoModel;

import java.util.List;

/**
 * Created by Robert Brewer on 11/15/2017.
 */

public class HoursArrayAdapter extends ArrayAdapter<DailyInfoModel> {


	private int textColor;


	public class DailyPayViewHolder {
        DailyPayViewHolder(){}
        ImageView imgIconH;
        TextView textViewDateH;
        TextView textViewEventNumberH;
        TextView textViewEventNameH;
        TextView textViewTimeH;
        TextView textViewBHourH;
        TextView textViewExtraHourH;
    }

    private final List<DailyInfoModel> objects;
    private final Integer[] pics;
    private final Activity context;

   //the custom layout ofr the listView goes in the constructor's super parentheses (R.Layout.row_itemList)

    public HoursArrayAdapter(Activity context, List<DailyInfoModel> objects, Integer[] pics) {
        super(context, R.layout.row_itemlist, objects);
       // res = resourceLocal;
        this.context = context;
        this.pics = pics;
        this.objects = objects;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        // Get the data item for this position
        DailyInfoModel user = getItem(position);

         // Check if an existing view is being reused, otherwise inflate the view
        DailyPayViewHolder holder; // view lookup cache stored in tag

        if(convertView == null)
        {
            // If there's no view to re-use, inflate a brand new view for row
            holder = new DailyPayViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemlist, parent, false);

            holder.imgIconH = (ImageView)convertView.findViewById(R.id.imageView);
            holder.textViewDateH = (TextView)convertView.findViewById(R.id.textViewDate);
            holder.textViewEventNameH = (TextView)convertView.findViewById(R.id.textViewEventName);
            holder.textViewEventNumberH = (TextView)convertView.findViewById(R.id.textViewEventNumber);
            holder.textViewTimeH = (TextView)convertView.findViewById(R.id.textViewTime);
            holder.textViewBHourH = (TextView)convertView.findViewById(R.id.textViewrHours);//regular hours
            holder.textViewExtraHourH = (TextView)convertView.findViewById(R.id.textViewoHours);//overtime hours
            convertView.setTag(holder);
        }
        else
        {
            holder = (DailyPayViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the holder object
        // into the template view (row_itemlist.xml layout).



	    holder.imgIconH.setImageResource(pics[getImageInt(user.getDay())]);
	    String myColors = null;
        switch(getImageInt(user.getDay())){
        	case 0://SUN red
		        myColors = "#FFFF0000";
		        break;
	        case 1:// ****Mon orange
		        myColors = "#FFFFBB33";
	            break;
	        case 2://TUES pink
		        myColors = "#FFFFC0CB";
		        break;
	        case 3://WED green
		        myColors = "#FF008000";
		        break;
	        case 4://****THUR  aqua blue
		        myColors = "#FF0099CC";
		        break;
	        case 5://FRI light blue
		        myColors = "#FF000080";
		        break;
	        case 6://***SAT purple  "#FFAA66CC"
		        myColors = "#FFAA66CC";
		        break;
	        default:

        }




	    holder.textViewDateH.setTextColor(Color.parseColor(myColors));
	    holder.textViewEventNumberH.setTextColor( Color.parseColor(myColors));
	    holder.textViewEventNameH.setTextColor(Color.parseColor(myColors));
	    holder.textViewTimeH.setTextColor(Color.parseColor(myColors));
	    holder.textViewBHourH.setTextColor(Color.parseColor(myColors));
	    holder.textViewExtraHourH.setTextColor(Color.parseColor(myColors));


        holder.textViewDateH.setText(user.getDate());
        holder.textViewEventNumberH.setText( "Event Number: " + user.getEventNumber());
        holder.textViewEventNameH.setText("Event Name: " + user.getEventName());
        holder.textViewTimeH.setText("Hours: " + user.getTime());
        holder.textViewBHourH.setText( "Regular Hours = " + String.format("%.2f", user.getRhours()));
        holder.textViewExtraHourH.setText("OT Hours = " + String.format("%.2f", user.getOhours()));
        // Return the completed view to render on screen
        return convertView;
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


}
