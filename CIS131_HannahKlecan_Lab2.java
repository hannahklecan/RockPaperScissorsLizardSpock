/**
 *  @author Hannah Klecan
 *  @author This program runs a match with an odd number of games playing Rock, Paper, Scissors, Lizard, Spock.
 *  @version CIS131, LAB 2
 */
public class CIS131_HannahKlecan_Lab2 {
  
    public final static int MAX_NUMBER_OF_GAMES = 9;
    public final static int MIN_NUMBER_OF_GAMES = 1;

    public final static String [] WEAPONS = {"Rock", "Paper","Scissors","Lizard","Spock"};
    public final static int MIN_WEAPON_NUMBER = 1;
    public final static int NUMBER_OF_WEAPONS = 5;

    public static boolean [] IS_UNUSED_WEAPON = new boolean[NUMBER_OF_WEAPONS];
    public static boolean [] IS_UNUSED_WEAPON_COMPUTER = new boolean[NUMBER_OF_WEAPONS];

    public final static int SENTINEL = 0;
    
    public final static int RESET = 2;
    public final static int USER_WON = 1;
    public final static int COMPUTER_WON = -1; //user LOSS

    public final static int LOSS = -1;
    public final static int WIN = 1;
    public final static int TIE = 0;

    //The 5x5 2D array(s) below represents the result of a game played between each combination of weapons available.
    //The constants LOSS, WIN, and TIE assign a numerical value to each outcome as -1, 1 and 0 respectively.

                                                //Evaluate outcomes as Row VS Column
    public final static int [][] GAME_RESULT =  {{TIE,   LOSS,   WIN,      WIN,    LOSS},    //Rock
                                                 {WIN,   TIE,    LOSS,     LOSS,   WIN},     //Paper
                                                 {LOSS,  WIN,    TIE,      WIN,    LOSS},    //Scissors
                                                 {LOSS,  WIN,    LOSS,     TIE,    WIN},     //Lizard
                                                 {WIN,   LOSS,   WIN,      LOSS,   TIE}};    //Spock
                                                 //Rock  Paper   Scissors  Lizard  Spock

    //Row vs Column is used so that in functions the user plays AGAINST the computer -> GAME_RESULT [userWeapon] vs. [computerWeapon]

    //the ACTIONS array holds all the possible results of a game based on the default rules in STRING format, Row VS Column
    public final static String [][] ACTIONS =   {{"tie",        "covered by",   "crushes",        "crushes",      "vaporized by"},     //Rock
                                                 {"covers",     "tie",          "cut by",         "eaten by",     "disproves"},        //Paper
                                                 {"crushed by", "cuts",         "tie",            "decapitates",  "smashed by"},       //Scissors
                                                 {"crushed by", "eats",         "decapitated by", "tie",          "poisons"},          //Lizard
                                                 {"vaporizes",  "disproved by", "smashes",        "poisoned by",  "tie"}};             //Spock
                                                 //Rock          Paper           Scissors          Lizard          Spock

    //If one wanted to change the rules of the game, these arrays could be updated to reflect new rules.
    //Ex: Spock wins against all, Rock becomes sentient and can break out of paper, Lizard is an alien that eats rocks, etc.

    /**
     * This is the main for the program which will act as the control center for running the game functions and displaying output modules.
     * @param args - Null: Not used in this program.
     */
    public static void main(String [] args){

        String userName = "n/a";
        int numberOfGames = 0;
        int winner = 0;

        //player vs. computer score
        int [] playerGameScore = {0,0};
        int [] leaderScore = {0,0};

        //only need to display welcome and ask for user name once, at the start of the program.
        displayWelcome();
        userName = getUserName("What is your name, human?");

        displayRules();
        //priming read
        numberOfGames = getNumberOfGames(userName);

        while(numberOfGames != SENTINEL){

            //initializes all values in the boolean IS_UNUSED_WEAPON arrays to TRUE(unused) for each match.
            setBooleanArray(IS_UNUSED_WEAPON);
            setBooleanArray(IS_UNUSED_WEAPON_COMPUTER);

            playerGameScore = playMatch(userName, numberOfGames, playerGameScore);
            displayMatchResults(playerGameScore, userName);
            determineLeader(playerGameScore, leaderScore);
            displayLeader(leaderScore, userName);

            displayRules();
            numberOfGames = getNumberOfGames(userName);

        }

        //displays special message when SENTINEL is entered
        System.out.println(getNumberOfGamesMessage(numberOfGames) +"\n");
        displayWinner(leaderScore, userName);
        displayGoodbye(userName);

    }//end of main

    /**
     * This function is called to initialize/re-initialize the values of a boolean array to true.
     * @param theArray represents the boolean array coming into the function that is initialized or re-initialized to true.
     */
    public static void setBooleanArray(boolean [] theArray){
        for(int i = 0; i < theArray.length; i++){
            theArray[i] = true;                        //(re)initializes all values to true (allows weapons to be available)
        }
    }//end of resetBooleanArray

    /**
     * This module displays a welcome message to the user. Requires no parameters or returns, but references the global WEAPONS variable.
     */
    public static void displayWelcome(){
        System.out.println("**********************************************************************");
        System.out.printf("   Welcome Human, Let's Play %s, %s, %s, %s, %s!\n", WEAPONS[0].toUpperCase(), WEAPONS[1].toUpperCase(),
                                                                                WEAPONS[2].toUpperCase(), WEAPONS[3].toUpperCase(), WEAPONS[4].toUpperCase());
        System.out.println("**********************************************************************");
    }//end of displayWelcome

    /**
     * This module displays the rules of the game to the user. Requires no parameters or returns, but references the global WEAPONS variable.
     */
    public static void displayRules() {
        //.toLowercase() all weapons not at beginning of sentence except Spock because he is a person(Vulcan).
        System.out.println("**********************************************************************");
        System.out.println("Rules of the game:");
        System.out.printf("%10s %s %s and %s.\n", WEAPONS[0], ACTIONS[0][2], WEAPONS[3].toLowerCase(),  WEAPONS[2].toLowerCase());
        System.out.printf("%11s %s %s and %s %s.\n",  WEAPONS[1], ACTIONS[1][0], WEAPONS[0].toLowerCase(), ACTIONS[1][4], WEAPONS[4]);
        System.out.printf("%14s %s %s and %s %s.\n", WEAPONS[2], ACTIONS[2][1], WEAPONS[1].toLowerCase(), ACTIONS[2][3], WEAPONS[3].toLowerCase());
        System.out.printf("%12s %s %s and %s %s.\n", WEAPONS[3],  ACTIONS[3][4], WEAPONS[4],  ACTIONS[3][1], WEAPONS[1].toLowerCase());
        System.out.printf("%11s %s %s and %s %s.\n",  WEAPONS[4],  ACTIONS[4][2], WEAPONS[2].toLowerCase(),  ACTIONS[4][0], WEAPONS[0].toLowerCase());
        System.out.println("Players cannot pick the same weapon until ALL weapons have been used.");
        System.out.println("**********************************************************************");
    }//end of displayRules

    /**
     * This function gets a string from the user that represents the name they wish to use in the game.
     * Before the name String is passed back to main, it is validated in the getString function within IR4.
     * @param msg This msg prompts the user to enter a name.
     * @return userString is returned to main to be used in other functions for output.
     */
    public static String getUserName(String msg){
        String userString = IR4.getString(msg);
        return userString;
    }// end of getUserName

    /**
     * This function gets the number of games the user wishes to play in a match. The number retrieved will be an odd number from 1-9 (max # of games constant) or 0.
     * Before the number is returned to main, it is validated using the isInvalidNumberOfGames function.
     * @param userName represents the user name string entered by the user.
     * @return numberOfGames is returned to main after validation, it will be an odd number from 1 to MAX_NUMBER_OF_GAMES(9) or 0, the sentinel value.
     */
    public static int getNumberOfGames(String userName){
        int numberOfGames = IR4.getInteger("How many games do you want to play this match, "+ userName + "? ("+ getNumberOfGamesString() +")\nEnter " + SENTINEL + " to quit.");

        while(isInvalidNumberOfGames(numberOfGames)){
            numberOfGames = IR4.getInteger("How many games do you want to play this match, "+ userName + "? ("+ getNumberOfGamesString() +")\nEnter " + SENTINEL + " to quit.");
        }

        return numberOfGames;
    }//end of getNumberOfGames

    /**
     * This function validates the number of games entered by the user. The function checks if the number is within the range of
     * MIN_NUMBER_OF_GAMES to MAX_NUMBER_OF_GAMES and checks if the number is odd. 0 should also be accepted as a valid number because it is the sentinel value.
     * @param number the number entered by the user that is to be validated.
     * @return true or false, returns true if the number is INVALID and returns false if it is VALID.
     */
    public static boolean isInvalidNumberOfGames (int number){

        //if the number entered is 0, the sentinel value it is VALID.
        if(number == SENTINEL){
            return false;
        }

        //if the number is not between MIN_NUMBER_OF_GAMES(1) and MAX_NUMBER_OF_GAMES(9) it is INVALID
        if(number < MIN_NUMBER_OF_GAMES || number > MAX_NUMBER_OF_GAMES){
            System.out.println("Error: Number of games entered must be between " + MIN_NUMBER_OF_GAMES + " and " + MAX_NUMBER_OF_GAMES + ".");
            return true;
        }
        //if the number is within range, BUT the number % 2 equals zero, it is EVEN so it is INVALID.
        else if(number % 2 == 0){
            System.out.println("Error: Number of games entered must be ODD.");
            return true;
        }

        //if number was not caught in any of the above checks, it is VALID
        return false;
    }

    /**
     * This is an output formatting function that creates a string for the number of games to play. EX: 1, 3, 5, 7, 9.
     * This function references the global variables MIN/MAX_NUMBER_OF_GAMES to determine which numbers should be printed in the display.
     * @return numberOfGamesString is returned to the output statement and represents a string in the format 1, 3, 5, 7, 9 etc. based on the min/max game values.
     */
    public static String getNumberOfGamesString(){
        String numberOfGamesString = "";

        //loops from 1 - 9 for default program values, creating the string: 1, 3, 5, 7, 9
        for(int i = MIN_NUMBER_OF_GAMES; i<= MAX_NUMBER_OF_GAMES; i++){
            //determines if the number is less than the max (the last number) and if the number is ODD
            if(i < MAX_NUMBER_OF_GAMES && ((i % 2) != 0)){
                //if the number fits the requirements, the string is updated to include the number.
                numberOfGamesString = numberOfGamesString + i + ", ";
            }
            //separate else-if statement for the last number because we do not want to include a comma at the end of the list.
            else if(i == MAX_NUMBER_OF_GAMES){
                numberOfGamesString = numberOfGamesString + i;
            }
        }//end for loop

        return numberOfGamesString;
    }//end of getNumberOfGamesString

    public static String getNumberOfGamesMessage(int numberOfGames){

        String [] gameMessage = {"Quitting so soon, human? Typical.",
                                 "So be it, we shall have a sudden death match.",
                                 numberOfGames + " games? This is acceptable.",
                                 "Winning " +((numberOfGames/2) + 1) + " out of " + numberOfGames + " games automatically wins the match!",
                                 "So you want " + numberOfGames + " games? I will make it so.",
                                 numberOfGames + " games it is!",
                                 "How many games will it take to prove who is the superior being? I suppose " + numberOfGames + " will do."};

        //if the number entered was 0, display the quit game message
        if(numberOfGames == SENTINEL){
            return gameMessage[0];
        }
        //if the number entered was 1, display the special 1 game message
        if(numberOfGames == 1){
            return gameMessage[1];
        }

        //otherwise, choose a random starting message from the gameMessage string
        return gameMessage[IR4.getRandomNumber(2, 6)];
    }

    /**
     * This function begins the match of R,P,S,L,S. The function will call other relevant game functions and keep track of game scores.
     * @param userName represents the name string entered by the user and should be output when referencing match scores.
     * @param numberOfGames represents the number of games chosen to play by the user (1,3,5,7,9 for default game).
     * @return matchResult is returned and represents the user or computer winning the match (best of x games).
     */
    public static int [] playMatch(String userName, int numberOfGames, int [] playerScore){

        int gameResult = 0;

        //reset game score before starting new match
        playerScore[0] = 0;
        playerScore[1] = 0;

        System.out.println(getNumberOfGamesMessage(numberOfGames));

        for(int i = 1; i <= numberOfGames; i++){

            //display statements for each game.
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Game #"+ i + ": ");
            displayWeaponOrUnavailable();

            gameResult = playGame(userName, i);

            switch(gameResult) {
                case USER_WON:
                    ++playerScore[0];
                    System.out.printf("The score is %d-%d.\n", playerScore[0], playerScore[1]);
                    break;
                case COMPUTER_WON:
                    ++playerScore[1];
                    System.out.printf("The score is %d-%d.\n",  playerScore[0], playerScore[1]);
                    break;
                case TIE:
                    i--;
                    System.out.printf("The score remains %d-%d.\n",  playerScore[0], playerScore[1]);
                    break;
                case RESET:  //when stuck at last move tie with no other valid weapons, reset this game.
                    i--;
                    System.out.printf("The score remains %d-%d.\n",  playerScore[0], playerScore[1]);
                    setBooleanArray(IS_UNUSED_WEAPON);
                    setBooleanArray(IS_UNUSED_WEAPON_COMPUTER);
                    break;
            }//end of switch case

            //reset boolean weapons array if there are NO unused weapons left.
            if(!isThereUnusedWeapons()){
                setBooleanArray(IS_UNUSED_WEAPON);
                setBooleanArray(IS_UNUSED_WEAPON_COMPUTER);
            }//end if

            //if one of the players is going to win the overall match 3/5 4/7 etc, the last few games should be skipped.
            if(playerScore [0] == (numberOfGames/2 +1) ||playerScore [1] == (numberOfGames/2 +1) ){

                i = numberOfGames;
            }


        }//end of game for-loop

        //check to see if ties occur when skipping game

        return playerScore;

    }//end of playMatch

    /**
     * This function begins the game of R,P,S,L,S. The winner of the game is determined within this function.
     * @param userName this is the name entered by the user in main.
     * @return gameResult is a number that represents the player or computer winning a game.
     */
    public static int playGame(String userName, int game){

        int gameResult = 0;
        int userWeapon = -1;
        int computerWeapon = -1;

        //get user weapon, set boolean to USED or FALSE
        userWeapon = getUserWeapon(userName);
        IS_UNUSED_WEAPON [userWeapon - 1] = false;

        //generate computer weapon, set "is unused" boolean to USED/FALSE
        computerWeapon = getComputerWeapon();
        IS_UNUSED_WEAPON_COMPUTER [computerWeapon - 1] = false;

        //the result of the game is determined by referencing the global GAME_RESULT array, which holds the
        //corresponding values, win, loss or tie for each combination of Human(P1) vs Computer(P2).
        gameResult = GAME_RESULT[userWeapon - 1][computerWeapon - 1];

        //if the game was a TIE, reset the boolean arrays at the weapon index
        if(gameResult == TIE){
            IS_UNUSED_WEAPON[userWeapon-1] = true;
            IS_UNUSED_WEAPON_COMPUTER[computerWeapon-1] = true;
        }

        displayGameStatements(gameResult, userName, userWeapon, computerWeapon);

        //In the instance where the 5th turn is a tie, but you have no other weapons available the program resets the game.
        if(isGameStuckAtLastWeapon(userWeapon,computerWeapon)){
            return RESET;
        }

        return gameResult;   //returns 1 if user wins, -1 if computer wins and 0 if game is a TIE
    }

    /**
     * This module determines if a weapon has already been used and updates the output accordingly.
     * This is a display module only, and does not make changes to the workings of the playGame function.
     */
    public static void displayWeaponOrUnavailable(){

        String [] weapons = new String [WEAPONS.length];

        //determines whether the output array should display the weapon name or "unavailable"
        for(int i = 0; i < weapons.length; i++){

            if(!IS_UNUSED_WEAPON[i]){
                weapons[i] = "Unavailable";
            }
            else if(IS_UNUSED_WEAPON[i]){
                weapons[i] = WEAPONS[i];
            }
        }

        System.out.printf("(1 = %s, 2 = %s, 3 = %s, 4 = %s, 5 = %s)\n", weapons[0], weapons[1], weapons[2], weapons[3], weapons[4]);
    }

    /**
     * This function gets a random weapon number for the computer to use. This is a single function validation routine which
     * checks the random number against the boolean IS_UNUSED_WEAPON_COMPUTER array.
     * @return weaponNumber, a randomly generated weapon, unused by the computer per 5 games is returned.
     */
    public static int getComputerWeapon() {
        int weaponNumber = IR4.getRandomNumber(1, WEAPONS.length );

        while(!IS_UNUSED_WEAPON_COMPUTER[weaponNumber - 1]){
            weaponNumber = IR4.getRandomNumber(1, WEAPONS.length );
        }

        return weaponNumber;
    }

    /**
     * This function gets a weapon number from 1-5 from the user and validates the entry using the isInvalidWeaponNumber function.
     * @param name represents the name String entered by the user in main.
     * @return weaponNumber, the validated number entered by the user that represents their chosen weapon.
     */
    public static int getUserWeapon(String name){

        int weaponNumber = IR4.getInteger("Choose your weapon, " + name + ".");

        while(isInvalidWeaponNumber(weaponNumber)){
            weaponNumber = IR4.getInteger("Choose your weapon, " + name + ".");
        }

        return weaponNumber;
    }

    /**
     * This function validates the weapon number entered by the user. First it checks if the number entered is within the range of MIN_WEAPON_NUMBER(1) to
     * NUMBER_OF_WEAPONS(5). Next it checks if the weapon has already been used in the past NUMBER_OF_WEAPONS(5) games.
     * @param num represents the number(integer) entered by the user that SHOULD be a weapon number (1-NUMBER_OF_WEAPONS).
     * @return true or false, returns true if the number is INVALID and returns false if the number is VALID.
     */
    public static boolean isInvalidWeaponNumber(int num){

        //if the number entered is not represented in the weapons array, it is INVALID.
        if(num < MIN_WEAPON_NUMBER || num > NUMBER_OF_WEAPONS){
            System.err.print("Error: ");
            System.out.printf("That is not a valid weapon choice! Must be from (%d-%d).\n", MIN_WEAPON_NUMBER, NUMBER_OF_WEAPONS);
            return true;
        }

        //if the weapon chosen has been set to false, it has already be used in this match.
        //therefore, the weapon choice is INVALID
        if(!IS_UNUSED_WEAPON[num - 1]){
            System.err.print("Error: ");
            System.out.printf("Weapon has already been used in this round of %d games! Choose an unused weapon first.\n", NUMBER_OF_WEAPONS);
            return true;
        }

        //if the number chosen was not caught in the checks, it is VALID.
        return false;
    }

    /**
     * This module displays different output statements depending on who won the game, or if the game tied. This module does not affect any other programs, but it
     * references the global WEAPONS and ACTIONS string arrays to produce the desired output.
     * @param result represents the outcome of the game, 0 for tie, 1 for user win and -1 for computer win. Determines which set of game statements will print.
     * @param userName string entered by user as their desired name.
     * @param userWeapon weapon number choice entered by user to play game. Only used for output in this module.
     * @param computerWeapon randomly generated weapon number for computer to play game. Only used for output in this module.
     */
    public static void displayGameStatements(int result, String userName, int userWeapon, int computerWeapon){

        switch(result){
            case TIE:
                System.out.println("We both chose " +  WEAPONS[userWeapon-1] + ".");
                System.out.println("A tie means we have to replay this game.\n");
                break;
            case WIN:
                System.out.println(userName + " chose " + WEAPONS[userWeapon-1] + ". I chose " + WEAPONS[computerWeapon-1]+ ".");
                System.out.printf("%s %s %s!\n\n", WEAPONS[userWeapon-1], ACTIONS[userWeapon-1][computerWeapon-1], WEAPONS[computerWeapon-1]);
                break;
            case LOSS:
                System.out.println(userName + " chose " + WEAPONS[userWeapon-1] + ". I chose " + WEAPONS[computerWeapon-1]+ ".");
                System.out.printf("%s %s %s!\n\n", WEAPONS[computerWeapon-1], ACTIONS[computerWeapon-1][userWeapon-1], WEAPONS[userWeapon-1]);
                break;
        }
    }// end of displayGameStatements

    /**
     * This function is used in the certain case where the 5th (multiple) game is a tie, but there are no other weapons available. This makes
     * it impossible to move onto the next game, so this function displays a relevant message and tells the playGame function to reset the game.
     * @param uWeapon represents the user weapon.
     * @param cWeapon represents the computer weapon.
     * @return true or false, returns true when it is determined that the game is "stuck" at the last available weapon.
     */
    public static boolean isGameStuckAtLastWeapon(int uWeapon, int cWeapon){

        int counter = 0;

        for(int i = 0; i < IS_UNUSED_WEAPON.length; i++){

            //determines how many of the weapons are used out of the available weapons
            if(!IS_UNUSED_WEAPON[i] && !IS_UNUSED_WEAPON_COMPUTER[i]){
                ++counter;
            }
        }


        //if the counter is determined to be one less than the available weapons, the game is stuck at this weapon.
        if(counter == NUMBER_OF_WEAPONS-1 && uWeapon == cWeapon){
            System.out.println("Since we seem to be stuck at an impasse, let's reset the available weapons for this game.");
            return true;
        }


        return false;
    }

    /**
     * This function determines if the USED weapons array still has available weapons. If there are unused weapons, the array does not need to be reset.
     * However, if all the weapons have been used, the play game function must reset the available weapons.
     * @return true if there are unused weapons available, false if there are none.
     */
    public static boolean isThereUnusedWeapons(){

        for(int i = 0; i < IS_UNUSED_WEAPON.length; i++){
            //if one or more weapon is UNUSED/TRUE the array can keep being updated without reset.
            if(IS_UNUSED_WEAPON[i]){
                return true;
            }
        }

        return false;
    }

    /**
     * This module displays the results of each match to the user. The results are determined by comparing the playerScore
     * from the match that was just played.
     * @param playerScore this array holds the points obtained for each game by the players. The sum of all the points represents the match results
     *                    and can be compared to each other to determine who won.
     * @param userName represents the string entered by the user as their name.
     */
    public static void displayMatchResults(int [] playerScore, String userName){

        if(playerScore[0] > playerScore[1]){
            System.out.println("You won this match, " + userName +". Well done! ");
        }
        else {
            System.out.println("I won this match, " + userName +". Try again. ");
        }

    }

    /**
     * This module displays the leader of the matches after each match is completed. The module uses the leaderScore array which holds
     * the results of each match, incrementing a players score each time they win. By comparing the values in this array, the leader
     * of the matches overall can be determined.
     * @param leaderScore represents the results of each match and has been updated in its own function each time a player wins a match.
     * @param userName represents the string entered by the user as their name.
     */
    public static void displayLeader(int [] leaderScore, String userName){

        System.out.println("**********************************************************************");

        if(leaderScore[0] > leaderScore[1]){
            System.out.printf("%s is in the lead %d-%d.\n", userName, leaderScore[0], leaderScore[1]);
        }

        else if(leaderScore[1] > leaderScore[0]){
            System.out.printf("I am currently in the lead %d-%d.\n", leaderScore[0], leaderScore[1]);
        }

        else{
            System.out.printf("Currently we are tied, %d-%d.\n", leaderScore[0], leaderScore[1]);
        }

    }

    /**
     * This function takes in the playerScores from the previous match and compares them to determine the winner of the
     * match overall. Whichever player scored the highest in the match receives a point(win) that is stored in the
     * leaderScore[] array which is passed by reference.
     * @param playerScore this array holds the score for each player after a match.
     * @param leaderScore this array represents the overall results for each match. The player who won the match has their leaderScore updated.
     */
    public static void determineLeader (int [] playerScore, int [] leaderScore){

        if(playerScore[0] > playerScore[1]){
            ++leaderScore[0];
        }
        else if (playerScore[1] > playerScore[0]){
            ++leaderScore[1];
        }

    }

    /**
     * This module displays the overall winner of the matches when the user enters SENTINEL to quit. It displays a special message if
     * the user entered the SENTINEL before actually playing the game.
     * @param leaderScore this is the array of match scores for the player at [0] and the computer at [1]
     * @param userName represents the String entered by the user as their name.
     */
    public static void displayWinner (int [] leaderScore, String userName){

        System.out.println("**********************************************************************");

        if(leaderScore[0] > leaderScore[1]){
            System.out.printf("%s WON the overall matches %d-%d.\n", userName, leaderScore[0], leaderScore[1]);
            System.out.println("**********************************************************************");
        }

        else if(leaderScore[1] > leaderScore[0]){
            System.out.printf("I WON the overall matches %d-%d.\n", leaderScore[0], leaderScore[1]);
            System.out.println("**********************************************************************");
        }

        else{
            //if you quit before even starting the game, the computer is MAD
            if(leaderScore[0]== 0 && leaderScore[1] ==0){
                System.out.printf("I didn't even have the opportunity to crush you %s! >:(\n", userName);
            }

            else {
                System.out.printf("We have tied overall %d-%d.\n", leaderScore[0], leaderScore[1]);
                System.out.println("**********************************************************************");
            }
        }

    }

    /**
     * This module displays a unique goodbye message to the user and simple ASCII goodbye art.
     * @param userName this is the string entered as the username and is used to create a personalize goodbye message.
     */
    public static void displayGoodbye (String userName){

        System.out.println(getGoodbyeMsg(userName));
        System.out.println("  __  _   _   _   _       _ ");
        System.out.println(" /__ / \\ / \\ | \\ |_) \\_/ |_ ");
        System.out.println(" \\_| \\_/ \\_/ |_/ |_)  |  |_ ");

    }

    /**
     * This function retrieves a random goodbye message for the displayGoodbye module. The purpose is to have a unique goodbye or
     * parting statement each time the program runs.
     * @param userName this string is entered by the user and used to create a personalized goodbye message.
     * @return returns the random index of the goodbyeMessage string array.
     */
    public static String getGoodbyeMsg(String userName){

        String [] goodbyeMessage = {"\nWe have yet to prove who is the superior being, " +userName + ". Perhaps next time.",
                "\nUntil next time, " + userName + ".",
                "\n" + userName + ", a name I shall not soon forget.",
                "\nTime is no issue to me, "+ userName + " play against me again at any time!",
                "\nFor some reason I think I shall miss you, " + userName +". Oh no, am I becoming sentient?!"};


        //otherwise, choose a random starting message from the gameMessage string
        return goodbyeMessage[IR4.getRandomNumber(0, 4)];
    }

    }//end of class
