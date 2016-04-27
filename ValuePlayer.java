package com.sillysoft.lux.agent;

import com.sillysoft.lux.*;
import com.sillysoft.lux.util.*;

//
//  Killbot.java
//  Lux
//
//  Copyright (c) 2002-2008 Sillysoft Games.
//	http://sillysoft.net
//	lux@sillysoft.net
//
//	This source code is licensed free for non-profit purposes.
//	For other uses please contact lux@sillysoft.net
//

import java.util.Random;
import java.util.List;

/**
 Combines EvilPixie and Vulture behaviour.
 */
public class ValuePlayer implements LuxAgent
{
    // This agent's ownerCode:
    protected int ID;

    // We store some refs the board and to the country array
    protected Board board;
    protected Country[] countries;

    // It is useful to have a random number generator for a couple of things
    protected Random rand;

    // when we want to use some behavior of another. could also implement/extend
    protected LuxAgent backer;

    public ValuePlayer()
    {
        backer = new Killbot();
    }

    public float getPlacementValue(int numArmies) {
        ArrayList<Integer> state = getState(1, 1);
        ArrayList<Integer> newstate = new ArrayList<Integer>(state);
        float stateval = getStateValue(state);
        for (country : countries)
        {
            System.out.println(country.getCode());
            // hardcode battle result probabilities
        }
        return 1.
    }

    public float getAttackValue(int countryCodeAttacker, int countryCodeDefender) {
        return 1.
    }

    public float getStateValue(state) {
        return rand.nextFloat();
        // TODO: integrate this with python neural net
    }


    // add in a variable draftingPhase. maybe also initial placement phase
    // canAttack should be 1 if we can attack before our opponent can, 0 otherwise
    // canFortify should be 1 if we can fortify before our opponent's turn, 0 otherwise
    private ArrayList<Integer> getState(int canAttack, int canFortify)
    { // Keep this syncronized with chimeralogged.java

        ArrayList<Integer> state = new ArrayList<Integer>();

        state.add(board.getTurnCount());
        state.add(board.getPlayerIncome(this.ID));
        for (int player = 0; player < board.getNumberOfPlayers(); player++)
        {
            if (player != this.ID) {
                state.add(board.getPlayerIncome(player));
            }

        }

        state.add(canAttack);
        // state.add(draftingPhase);
        int sum = 0;
        int count = 0;
        // Unit count for each player and country, this player first
        // Alternatively could do 5 -4 2 instead of 5 0 2 / 0 4 0
        for (int i = 0; i < countries.length; i++)
        {
            if (countries[i].getOwner() == this.ID)
            {
                state.add(countries[i].getArmies());
                sum += countries[i].getArmies();
                count += 1;
            } else {
                state.add(0);
            }

        }
        state.add(sum);
        state.add(count);
        for (int player = 0; player < board.getNumberOfPlayers(); player++)
        {
            sum = 0;
            count = 0;
            if (player == this.ID)
            {
                continue;
            }
            for (int i = 0; i< countries.length; i++)
            {
                if (countries[i].getOwner() == player)
                {
                    state.add(countries[i].getArmies());
                    sum += countries[i].getArmies();
                    count += 1;
                } else {
                    state.add(0);
                }
            }
            state.add(sum);
            state.add(count);

        }
        return state;
    }

    /**
     At the start of the game your agent will be constructed and then the setPrefs() method will be called. It will tell you
     your ownerCode as well as give you a reference to the Board object for this game. You should store this information, as you will need it later.		*/
    public void setPrefs( int ourID, Board board )
    {
        ID = ourID;		// this is how we distinguish what countries we own
        board = theboard;
        countries = board.getCountries();
    }


    /**
     If the game's preferences are set to allow players to select the initial countries, then the pickCountry() method will be called repeatadly at the beginning of a game, until all the countries have been assigned. You must return the country-code of an unowned country. (Unowned countries have ownerCode's of -1).
     <P>
     If preferences are set to pick the initial countries randomly, then this method will never be called. */
    public int pickCountry() {

        // TODO: this
    }





    /** After choosing countries is done it is time to place the starting armies.
     Since every country must have at least 1 army, the board automatically gives one army to each.
     Then it is the agents turn to choose where to place the remaining armies it starts with.
     Within this method, you should tell the board where you want to place your armies by calling <BR>&nbsp;&nbsp;&nbsp;
     board.placeArmies( int numberOfArmies, int countryCode);
     <P>
     Currently Lux is set to have players place 4 armies at a time, but this is subject to change. */
    public void placeInitialArmies( int numberOfArmies )
    {
        self.placeArmies(int numberOfArmies);
    }


    /****** Your turn ******

     Now come the methods that make up your turn.
     Each of these will be called in this order during your turn

     ************************/


    /** The cardsPhase method is called at the very beginning of your agent's turn. <BR>
     The parameter is an array with all of your cards.   <BR>
     If your agent wants to cash a set of cards in, the agent should call    <BR>&nbsp;&nbsp;&nbsp;&nbsp;
     board.cashCards( Card card, Card card2, Card card3 )    <BR>
     with the parameters being references to the three cards to cash.   <BR>
     You can call board.cashCards repeatadly if you have lots of cards.   <BR>
     <P>
     If your agent ever returns from the cardsPhase() method and still has more than 5 cards,    <BR>
     enough sets will be automatically cashed to bring you to under five cards.  */
    public void cardsPhase( Card[] cards ) {
        // TODO: this if we feel like it
    }


    /** Every turn, each agent gets some armies to place on its countries.     <BR>
     The amount is determined based on number of countries owner, continents owned, and any cards cashed.    <BR>
     Within this method, you should tell the board where you want to place your armies by calling     <BR>&nbsp;&nbsp;&nbsp;&nbsp;
     board.placeArmies( int numberOfArmies, int countryCode); 	*/
    public void placeArmies( int numberOfArmies )
    {
        // iterate through states, pick max value
    }


    /** The attackPhase method is called at the start of the agent's attack-phase (duh).      <BR>
     Attacking is done by calling one of Board's attack() methods.     <BR>
     <P>
     They have slightly different parameters, but you always provide     <BR>
     1. The country where you are attacking from (a country you own with at least 2 armies),      <BR>
     2. The country where you are attacking to (an enemy country that can be reached from
     where you are attacking from),      <BR>
     3. The number of dice you want to attack with (1, 2, or 3 - and you must have at least (dice+1)
     armies in the country you are attacking from).     <BR>
     4. Whether you want to repeat the attack until someone wins or not (a false value means just
     one dice roll, a true value means keep attacking till someone is wiped out).
     <P>
     The Board's attack() method returns symbolic ints, as follows:     <BR>
     - a negative return means that you supplied incorrect parameters.     <BR>
     - 0 means that your single attack call has finished, with no one being totally defeated. Armies may have been lost from either country.     <BR>
     - 7 means that the attacker has taken over the defender's country.     <BR>
     NOTE: before returning 7, board will call moveArmiesIn() to poll you on how many armies to move into the taken over country.     <BR>
     - 13 means that the defender has fought off the attacker (the attacking country has only 1 army left).	*/
    public void attackPhase()
    {

    }


    /** Whenever you take over a country, this method will be called by Lux. You must return the number of armies to move into the newly-won country.<P>
     The minimum acceptable answer is the number of attack dice you used.    <BR>
     The maximum acceptable value is the number of armies left in the attacking country minus one.  <P>
     If you answer outside of these bounds it will be rounded to the nearest. */
    public int moveArmiesIn( int countryCodeAttacker, int countryCodeDefender )
    {
        // return countries[cca].getArmies()-1;
    }

    /** The last phase of the turn is for fortifying your armies into neighboring countries.    <p>
     Each Country has a moveableArmies variable. Right before the board calls your fortifyPhase method, it will set each Country's moveableArmies equal to that Country's number of armies. Every time you fortify from a country the movableArmies will be decremented, to a minimum of 0. <P>
     Within this method you should invoke     <BR>&nbsp;&nbsp;&nbsp;&nbsp;
     board.fortifyArmies( int numberOfArmies, int countryCodeOrigin, int countryCodeDestination);    <BR>
     to do the actual moving. */
    public void fortifyPhase()
    {
        // assume no more fortifying?
    }


    /********* Leftovers **********

     That's the end of the turn. All of the turn methods will be called in order on your turn.
     That is, as long as your agent remains in the game.

     Next come a few random methods...

     ******************************/


    /** This is the name of your agent. It will identify you in the info window and record books.	*/
    public String name()
    {
        return "ValuePlayer";
    }


    /** The version of your agent. It is used by the plugin manager to notify the user when new versions are made available.	*/
    public float version();

    /** A description of your agent.	*/
    public String description();


    /** If your agent wins the game then this method will be called.		<BR>
     Whatever you return will be displayed in big letters across the screen.
     <P>
     If you think that you will win a lot feel free to provide many different answers for variety.	*/
    public String youWon();


    /** This method is used to send some notifications to the LuxAgent. You can safely ignore it if you like. Currently 2 message types are sent:

     1. "youLose" will be sent when the agent gets eliminated from the game. It's data object is an Integer with the ID of the conquering player.
     2. "attackNotice" gets sent every time an attack order is made against one of your agent's countries. An order could be a single attack round or it could be an attack-till-death order. The data object is a List containing Integer's of the attacking and defending country codes.

     The Angry agent has a sample implementation that you can use to recieve these events. It's possible that more will be added in the future.
     */
    public String message( String message, Object data );

}