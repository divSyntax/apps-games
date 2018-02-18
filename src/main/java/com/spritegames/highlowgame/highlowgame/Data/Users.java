package com.spritegames.highlowgame.highlowgame.Data;

/**
 * Created by carto on 2/15/2018.
 */

public class Users
{
    public int turns = 3;
    public int tier = 1;
    public int myturns = 3;


    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }


    public Users()
    {

    }

    public Users(int turns, int tier) {
        this.turns = turns;
        this.tier = tier;
    }




}
