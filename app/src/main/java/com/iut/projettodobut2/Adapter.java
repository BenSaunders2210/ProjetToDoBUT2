package com.iut.projettodobut2;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapteur pour transformer une Task en Vue qui sera affiché dans la ListView.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class Adapter extends BaseAdapter {
    private List<Task> listTasks;
    private Context context;
    private LayoutInflater inflater;

    public Adapter(Context context, List<Task> listMovies ) {
        this.listTasks = listMovies;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int i) {
        return this.listTasks.get(i);
    }

    @Override
    public int getCount() {
        return listTasks.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            // TODO : Enlever commentaire quand le layout existe.
            view = /*(View) inflater.inflate(R.layout.task_list_item, parent, false);*/ null;
        }else{
            view = (View) convertView;
        }

        // TODO : creation des tâches + adapter pour un Task + enlever commentaire quand le layout existe
/*        TextView name = (TextView) view.findViewById(R.id.movieItemTitle);
        TextView author = (TextView) view.findViewById(R.id.movieItemAuthor);
        TextView length = (TextView) view.findViewById(R.id.movieItemLength);
        ImageView image = (ImageView) view.findViewById(R.id.movieItemImage);*/

        Task currentMovie = listTasks.get(i);

/*        name.setText(currentMovie.getTitle());
        author.setText("Réalisateur: " + currentMovie.getAuthor());
        length.setText("Durée: " + currentMovie.getLength() + " min");
        image.setImageResource(currentMovie.getPosterPath());*/

        return view;
    }
}
