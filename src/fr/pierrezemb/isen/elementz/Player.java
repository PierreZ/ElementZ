package fr.pierrezemb.isen.elementz;

/**
 * Created by pierrezemb on 19/02/15.
 * Used by Gson for an easy parsing
 */
public class Player {
    public String name;
    public int score;

    @Override
    public String toString(){
        return "name : "+this.name+", score : "+score;
    }
}