package com.sillysoft.lux.agent;

import com.sillysoft.lux.*;
import com.sillysoft.lux.util.*;
import java.util.*;
import java.io.*;


public class ChimeraLogged implements LuxAgent
{

    // We use a backing agent to send many commands to.
// The type of backing agent is randomly picked at creation
    protected LuxAgent backer;

    // Use a static rand so that many instances will get different agent types
    private static Random staticRand = new Random();

    // We store a reference to the Board/countries/ID
    private Board board;
    private Country[] countries;
    private int ID;

    // We will write this down as our data
    private ArrayList<ArrayList> states;

    // The list of possible agent types.
    static protected String[] possibleAgentTypes;

    public ChimeraLogged()
    {

        states = new ArrayList<ArrayList>();

    }



    public void setPrefs(int ID, Board board )
    {
        this.board = board;
        this.countries = board.getCountries();
        this.ID = ID;

        possibleAgentTypes = new String[] {
                "Boscoe",
                "EvilPixie",
                "Killbot",
                "Quo"
        };
        /*  also consider:
                "Nefarious",
                "BotOfDoom",
                "Brainiac",
                "Trotsky",
                "Reaper",
                "Bort",
                "Sparrow"
         */

        // Try a maximum of 10 times to load an agent
        for (int i = 0; i < 10 && backer == null; i++)
        {
            try
            {
                String tryAgent = possibleAgentTypes[ staticRand.nextInt(possibleAgentTypes.length) ];
			System.out.println("Try to load agent type "+tryAgent);
                backer = board.getAgentInstance(tryAgent);
            }
            catch (Throwable e)
            {
                System.out.println("Error:");
                System.out.println(e);
			//System.out.println("Chimera could not load a "+tryAgent+". Will try another type...");
            }
        }
        if (backer == null) {
            backer = new Cluster();
            System.out.println("Couldn't load! We are just loading cluster!!!!");
        }

        backer.setPrefs(ID, board);
    }

    public String name()
    {	return "ChimeraLogged";	}

    public String realName()
    {	return backer.name();	}

    public float version()
    {	return 1.0f;	}
    public String description()
    {	return "Chimera has many different heads.";	}

    public String message( String message, Object data )
    {
        if ("youLose".equals(message))
        {
            logResults(0);
            board.sendEmote("reveals the shattered core of a "+backer.name(), this);
        }
        return backer.message(message, data);
    }

    public String youWon()
    {
        logResults(1);
        return backer.youWon()+"\n("+backer.name()+")";
    }

    // For all of the gameplay methods we just pass them to our backer:
    public int pickCountry()
    {
        return backer.pickCountry();
    }
    public void placeInitialArmies( int numberOfArmies )
    {
        backer.placeInitialArmies(numberOfArmies);
    }
    public int moveArmiesIn( int countryCodeAttacker, int countryCodeDefender )
    {
        return backer.moveArmiesIn(countryCodeAttacker, countryCodeDefender);
    }
    public void fortifyPhase()
    {
        //System.out.println("Fortify Phase");
        backer.fortifyPhase();
        appendState(0, 0);
    }
    public void cardsPhase( Card[] cards )
    {
        backer.cardsPhase(cards);
    }
    public void placeArmies( int numberOfArmies )
    {
        //System.out.println("Place Armies");
        backer.placeArmies(numberOfArmies);
        appendState(1, 1);
    }
    public void attackPhase()
    {
        //System.out.println("Attack Phase");
        backer.attackPhase();
        appendState(0, 1);
    }

    // add in a variable draftingPhase. maybe also initial placement phase
    // canAttack should be 1 if we can attack before our opponent can, 0 otherwise
    // canFortify should be 1 if we can fortify before our opponent's turn, 0 otherwise
    private void appendState(int canAttack, int canFortify)
    {

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
            state.add(sum);
            state.add(count);

        }
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
                state.add(sum);
                state.add(count);

            }

        }
        //System.out.println("Appending a state");
        //System.out.println(state);
        states.add(state);

        /*try{
            FileOutputStream fos= new FileOutputStream("C:\\Users\\ericgorlin\\Documents\\CSProjects\\Risk\\data.txt");
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(state);
            oos.close();
            fos.close();
        } catch(IOException ioe){
            System.out.println("fail fail");
            ioe.printStackTrace();
        }*/
    }

    private void logResults(int result)
    {

        try {
            PrintStream out = new PrintStream(new FileOutputStream("C:\\Program Files (x86)\\Lux\\Support\\data.txt", true));
            for (int i = 0; i < this.states.size(); i++) {
                this.states.get(i).add(0, result);
                out.println(this.states.get(i));
                out.flush();
            }
            out.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Couldn't find file to write to:");
            System.out.println("p" + this.ID + "data.txt");
        }
    }


}
