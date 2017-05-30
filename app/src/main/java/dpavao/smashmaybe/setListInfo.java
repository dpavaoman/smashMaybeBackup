package dpavao.smashmaybe;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dpava on 5/28/2017.
 */

public class setListInfo {

    public static List<Game> main (int game_count, List<Integer> tourney_info){
        List<Game> games;
        games = new ArrayList<>();
        int game_id;

        Game melee = new Game("Super Smash Bros. Melee", R.drawable.melee_banner_white, Color.parseColor("#F44336"));
        Game wiiu = new Game("Super Smash Bros. Wii U", R.drawable.wii_u_banner_white, Color.parseColor("#03A9F4"));
        Game smash = new Game("Super Smash Bros. 64", R.drawable.smash_64_banner_white, Color.parseColor("#FFEB3B"));
        Game projectm = new Game("Super Smash Bros. PM", R.drawable.pm_banner_white, Color.parseColor("#673AB7"));


        for (int i = 0; i< game_count; i++){
            game_id = tourney_info.get(i);
            switch (game_id){
                case 1: games.add(melee);
                    break;
                case 2: games.add(projectm);
                    break;
                case 3: games.add(wiiu);
                    break;
                case 4: games.add(smash);
                    break;
            }
        }

        return games;

    }


}
/*
1 = melee
2 = PM (rip)
3 = wiiu
4 = smash46
6 = other ???
7 = sfV
8 = pokken
10 = league
14 = rocket league
15 = brawlhalla
18 = ultimate marvel vs capcom 3
20 = splatoon
21 = guilty gear xrd
23 = mortal kombat xl
24 = Rivals of Aether
29 = smash 3ds
31 = overwatch
35 = injustice 2
180= 100% Orange Juice
 */
