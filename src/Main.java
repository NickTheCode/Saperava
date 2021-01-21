import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	private static enum State{
		closed_bomb, closed_empty, opened_empty, opened_bomb;
	}

	
	private static Random rnd = new Random();
	private static Scanner scn = new Scanner(System.in);
	
	private static State minefield [][];
	private static int bombs;
	private static int free_spaces;
	private static boolean players_alive[];
	private static int players_lives;
	private static int player_turn;
	private static int difficulty_choice;
	private static boolean end;
	
	
	private static boolean Is_sure() {
		String answer;
		System.out.println("Are you sure?");
		while(true) {
			answer = scn.next().toLowerCase();
			switch(answer) {
				case "yes":
					return true;
				case "no":
					return false;
				default:
					System.out.println("Don't understand. Repeat, please.");
					break;
			}
		}	
	}
	
	private static void Fieldsize () {
		int choice;
		System.out.println("Ok, what size of fied do you want?");
		while(true) {
			System.out.println("1) 5x5"
					+ "\n2) 7x7"
					+ "\n3) 9x9");
			choice = 0;
			choice = Mylibrary.Check_int();
			switch(choice) {
				case 1:
					minefield = new State [5][5];
					break;
				case 2:
					minefield = new State [7][7];
					break;
				case 3:
					minefield = new State [9][9];
					break;
				default:
					System.out.println("Don't understand, repeat please.");
			}
			if(choice > 0 && choice < 4 && Is_sure()) {
				break;
			}
		}
		
		
	}
	
	private static void Show_choice_of_bomb () {
		if(minefield.length == 5) {
			System.out.println(
					  "1) Easy - 5 bombs;\n"
					+ "2) Medium - 10 bombs;\n"
					+ "3) HARD - 15 bombs;");
		}
		else if(minefield.length == 7) {
			System.out.println(
					  "1) Easy - 10 bombs;\n"
					+ "2) Medium - 20 bombs;\n"
					+ "3) HARD - 30 bombs;");	
		}
		else {
			System.out.println(
					  "1) Easy - 16 bombs;\n"
					+ "2) Medium - 32 bombs;\n"
					+ "3) HARD - 48 bombs;");
		}
	}
	
	private static int Amout_of_bombs (int choice) {
		double mnozhitel = 0;
		int return_value;
		switch(minefield.length) {
			case 5:
				mnozhitel = 1;
				break;
			case 7:
				mnozhitel = 2;
				break;
			case 9:
				mnozhitel = 3.2;
				break;
			default:
				System.out.println("How did you enter here?..");
				mnozhitel = 0;
		}
		return_value = (int) (5 * mnozhitel * choice);
		return return_value;
	}
	
	private static void Difficult() {
		System.out.println("So, what difficuty do you want?");
		do {
			Show_choice_of_bomb();
			bombs = 0;
			try {
				difficulty_choice = scn.nextInt();
			}catch (InputMismatchException e) {
				System.out.println("What? Again, please.");
				scn.nextLine();
				continue;
			}	
			switch(difficulty_choice) {
				case 1:
					bombs = Amout_of_bombs(difficulty_choice);						
					System.out.println("Easy? You are noob, you know?");
					break;
				case 2:
					bombs = Amout_of_bombs(difficulty_choice);
					System.out.println("Medium is perfect for men, who can face their fears. ");
					break;
				case 3:
					System.out.println("HARD?! What is wrong with you?");
					bombs = Amout_of_bombs(difficulty_choice);
					break;
				default:
					System.out.println("Repeat please!");
					
			}
			if(bombs > 0 && Is_sure()) {
				System.out.println(bombs);
				break;
			}
		}while(true);
	}
	
	
	
	private static void Players() {
		int num = 0;
		System.out.println("Ok, what abour players? Max is 5.");
		do {
			System.out.println("How many of them?");
			try {
				num = scn.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("What? Again, please.");
				scn.nextLine();
				continue;
			}
			if(num <= 0 || num > 5) {
				System.out.println("Write normal value, please.");
				continue;
			}
			if(num == 1) {
				System.out.println("Forever alone...");
			}
			if(num == 5) {
				System.out.println("HOO BOY, PARTY IS GONNA BE HOT!");
			}
			if(Is_sure()) {
				players_alive = new boolean [num];
				players_lives = num;
				Arrays.fill(players_alive, true);
				return;
			}
		}while(true);
	}
	
	private static void Place_bombs() {
		for (State[] row: minefield) {
		    Arrays.fill(row, State.closed_empty);
		}
		for(int round = 0; round < bombs; ) {
			int i = rnd.nextInt(minefield.length);
			int j = rnd.nextInt(minefield[0].length);
			if(minefield[i][j] == State.closed_empty) {
				minefield[i][j] = State.closed_bomb;
				round++;
			}
		}
	}
	
	private static void Initialization() {
		Fieldsize();
		Difficult();
		free_spaces = minefield.length * minefield[0].length - bombs;
		Players();
		Place_bombs();
	}
	

	private static void Intro() {
		System.out.println("Hello, players! It's ''Sapper''. There are another rules. There is field with bombs. "
				+ "You must write coordinate of cell, where there is no bombs, or you will... Boom?");
		Initialization();
		System.out.println("Ok, let the battle begin!");
	}
	
	private static char Get_symbol(State state) {
		switch(state) {
			case closed_bomb:
				if(end) {
					return '!';
				}
			case closed_empty:	
				if(end) {
					return ' ';
				}
				else {
					return '?';
				}	
			case opened_empty:
				return 'O';
			case opened_bomb:
				return 'X';
			default:
				return ' ';
		}
	}
	
	private static void Show_minefield() {
		System.out.print("   ");
		for(int i = 1; i <= minefield.length; i++) {
			System.out.print(" " + i + " ");
		}
		System.out.println();
		for(int i = 0; i < minefield.length; i++) {
			System.out.print(" " + (char)('A' + i) + " ");
			for(int j = 0; j < minefield[0].length; j++) {	
				System.out.print("[" + Get_symbol(minefield[i][j]) + "]");
			}
			System.out.println();
		}
	}
	
	private static boolean Is_right_coordinate (String choice) {
		char one = choice.charAt(0);
		char two = choice.charAt(1);
		if(
			(one >= 'A' && one <= ('A'+minefield.length-1) && two >= '1' && two <= minefield.length + '0')
			||
			(one >= '1' && one <= minefield.length + '0' && two >= 'A' && two <= ('A'+minefield.length-1))
			) {
			return true;
		}
		return false;
	}
	
	private static boolean Examine_index(int i, int j) {
		try {
			if(minefield[i][j] == State.closed_bomb) {
				return true;
			}
			return false;
		}catch(Exception e) {
			return false;
		}		
	}
	
	private static void Check_bomb (int i, int j) {
		int k;
		for(k = 1; k < 4; k++) {
			if(Examine_index(i+k, j)) {
				break;
			}
			if(Examine_index(i-k, j)) {
				break;
			}
			if(Examine_index(i, j-k)) {
				break;
			}
			if(Examine_index(i, j+k)) {
				break;
			}
		}
		if(k <= 1) {
			System.out.println("Hoo boy, too hot, man!");
		}
		else if (k < 4) {
			System.out.println("It is rather warm...");
		}
		else {
			System.out.println("It is too cold.");
		}
	}
	


	private static boolean Open_case(int i, int j) {
		switch(minefield[i][j]) {
			case opened_empty:
				System.out.println("Oops, already opened. Try another.");
				return false;
			case opened_bomb:
				System.out.println("Do you want to mistake as your enemy? Try another.");
				return false;
			case closed_empty:
				Check_bomb(i, j);
				free_spaces--;
				minefield[i][j] = State.opened_empty;
				return true;
			case closed_bomb:
				System.out.println("BOOM! YOU BLEW UP! You are dead.");
				minefield[i][j] = State.opened_bomb;
				players_alive[player_turn] = false;
				players_lives--;
				return true;
			default:
				return false;
		}
	}
	
	private static boolean Find_coordinate (String choice) {
		int i;
		int j;
		if(Character.isDigit(choice.charAt(0))) {
			j = choice.charAt(0) - '1';
			i = choice.charAt(1) - 'A';
		}
		else {
			i = choice.charAt(0) - 'A';
			j = choice.charAt(1) - '1';
		}
		return Open_case(i, j);
	}
	
	private static void Player_step () {
		String choice;		
		while(true) {
			choice = scn.next().toUpperCase();
			if(choice.length() == 2) {
				if(Is_right_coordinate(choice)) {
					if(Find_coordinate(choice)) {
						return;
					}
				}
				else {
					System.out.println("Length is right, but letters are wrong.");
				}
			}
			else {
				System.out.println("Write normal coordinate, please!");
			}
		}
	}
	

	private static void Game() {
		player_turn = -1;
		while(
				(players_alive.length == 1 && players_lives == 1 && free_spaces > 0)
				||
				(players_alive.length > 1 && players_lives > 1 && free_spaces > 0)
			) {
			player_turn++;
			if(player_turn == players_alive.length) {
				player_turn = 0;
			}
			if(!players_alive[player_turn]) {
				System.out.println("And now... Oh, player " + (player_turn + 1) + " is dead. Rest in pieces, maybe?");
				continue;
			}
			Show_minefield();
			if(players_alive.length > 1) {
				System.out.println("And now it is player's " + (player_turn + 1) + " turn!" );
			}
			Player_step();
		}
	}
	
	private static int Check_player_winner () {
		int i = 0;
		for( ; i < players_alive.length; i++) {
			if(players_alive[i]) {
				i++;
				break;
			}
		}
		return i;
	}
	
	private static void Check_group_of_winners ( ) {
		System.out.print("Wow, we have many winers! It is ");
		for(int i = 0; i < players_alive.length; i++) {
			if(players_alive[i]) {
				System.out.print("player " + (i + 1));
			}
			if(i < 4) {
				System.out.print(", ");
			}
			else {
				System.out.println('.');
			}
		}
	}
	
	private static void Winner_showing () {
		if(players_alive.length == 1) {
			if(players_lives == 1 && difficulty_choice > 1) {
				System.out.println("God DAMN! You did it! You won, alone, in dangerous game! Haha, man, you are an epic ''Saperava''!");
			}
			else if (players_lives == 1 && difficulty_choice == 1) {
				System.out.println("Well, yeah, you won... But in easy mode. So, go replay, but take higher difficulty!");
			}
			else {
				System.out.println("You lose. But I respect you for your carouges to play alone.");
			}
		}
		else {
			if(players_lives == 1) {
				System.out.println("We have a great winner! And it is... player " + Check_player_winner() + "! Congratulations!");
			}
			else {
				Check_group_of_winners();
			}
		}
	}
	
	private static boolean Restart () {
		String answer;
		System.out.println("Want to restart? (yes/no)");
		while(true) {
			answer = scn.next().toLowerCase();
			switch(answer) {
				case "yes":
					return true;
				case "no":
					return false;
				default:
					System.out.println("Don't understand. Repeat, please.");
					break;
			}
		}	
	}
	
	
	public static void main(String[] args) {
		boolean restart = true;
		while(restart) {	
			Intro();
			end = false;
			Game();
			Winner_showing();
			end = true;
			Show_minefield();
			while(true) {
				restart = Restart();
				if(Mylibrary.Is_sure()) {
					break;
				}
			}
			continue;
		}
	}

}



/*
	Идет проверка, если closed_empty, меняет на open_empty и говорит, где ближайшая бомба; если closed_bomb, меняет на open_bomb и УНИЧТОЖАЕТ ИГРОКА; если идет на open_empty, ругает игрока и говорит ему идти ешо раз
	если open_bomb, говорит "Смерти ищешь?" или что-то подобное. Не будет следущего хода, если не открыл closed.
 	 1  2  3  4 
   A[?][?][?][?]
   B[?][?][?][?]
   C[?][Х][О][?]
 
 
 
 	игрок должен вписываать координату (А1==1А), КОМП ПРОВЕРЯЕТ на существование координаты и его длина (длина должна быть 2) и открывает ячейку.
 															String_name.charAt(1);													(обновить карту!)																			
 */















