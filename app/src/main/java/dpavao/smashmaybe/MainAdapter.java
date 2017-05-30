package dpavao.smashmaybe;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class MainAdapter extends RecyclerView.Adapter<MainAdapter.GameViewHolder> {

    private Context mContext;
    private List<Game> mDataSet;


    public MainAdapter(Context context, List<Game> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        final Game game = mDataSet.get(position);
        holder.gameBanner.setImageResource(game.getBanner_id());
        holder.gameName.setText(game.getName());
        holder.gameBanner.setBackgroundColor(game.getBackgroundColor());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),activity_test.class);
                intent.putExtra("gameInfo", game);
                v.getContext().startActivity(intent);


            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void remove(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void add(int position, Game game) {
        mDataSet.add(position, game);
        notifyItemInserted(position);
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView gameName;
        ImageView gameBanner;
        Game currentGame;



        GameViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.gameCard1);
            gameName = (TextView) itemView.findViewById(R.id.gameName1);
            gameBanner = (ImageView) itemView.findViewById(R.id.gameBanner1);

        }
    }
}