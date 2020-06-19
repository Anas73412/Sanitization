package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import in.sanitization.sanitization.Model.Socity_model;
import in.sanitization.sanitization.util.JsonParseSuggestion;


public class SocietyAdapter extends ArrayAdapter<String> {
    private List<String> suggestions;
    String city;

    public SocietyAdapter(Activity context,String city, String nameFilter) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        suggestions = new ArrayList<String>();
    }


    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int index) {
        return suggestions.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                JsonParseSuggestion jp = new JsonParseSuggestion();
                if (constraint != null) {
                    List<Socity_model> new_suggestions = jp.getParseJsonSociety(city,constraint.toString());
                    suggestions.clear();
                    for (int i = 0; i < new_suggestions.size(); i++) {
                        suggestions.add(new_suggestions.get(i).getSociety());
                    }
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {

                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
