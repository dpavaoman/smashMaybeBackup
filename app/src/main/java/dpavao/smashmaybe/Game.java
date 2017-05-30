package dpavao.smashmaybe;

import java.io.Serializable;

/**
 * Created by dpava on 5/27/2017.
 */

public class Game implements Serializable{

    private String name;
    private int banner_id;
    private int background_color;


    public Game(String name, int banner_id, int background_color) {
        this.name = name;
        this.banner_id = banner_id;
        this.background_color = background_color;
    }

    public int getBanner_id(){
        return banner_id;
    }

    public void setBanner_id(int id){
        this.banner_id=id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name=name;
    }

    public int getBackgroundColor (){
        return background_color;
    }

    public void setBackground_color (int background_color){
        this.background_color=background_color;
    }

}

