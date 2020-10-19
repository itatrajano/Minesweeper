package minesweeper;

import java.util.Random;
import java.util.Scanner;

enum PlaceState {
    MINE, FREE, ALERT
}

enum PlaceFlag {
    FLAG(true),
    NO_FLAG(false);

    boolean flag;

    PlaceFlag(boolean flag) {
        this.flag = flag;
    }
}

enum Visibility {
    VISIBLE(true),
    INVISIBLE(false);

    boolean isVisible;

    Visibility(boolean isVisible) {
        this.isVisible = isVisible;
    }
}

class Place {
    private boolean mine;
    private byte mineAlert;
    private PlaceState placeState;
    private PlaceFlag placeFlag;
    private Visibility visibility;


    public Place() {
        mine = false;
        mineAlert = 0;
        placeState = PlaceState.FREE;
        placeFlag = PlaceFlag.NO_FLAG;
        visibility = Visibility.INVISIBLE;
    }

    protected boolean hasMine() {
        return mine;
    }

    protected void setMine() {
        mine = true;
        placeState = PlaceState.MINE;
    }

    protected void setMineAlert(byte a) {
        this.mineAlert = a;
        placeState = (a != 0) ? PlaceState.ALERT : PlaceState.FREE;
    }

    protected byte getMineAlert() {
        return this.mineAlert;
    }

    protected PlaceState getPlaceState() {
        return placeState;
    }

    protected void toggleFlag() {
        placeFlag = placeFlag.flag ? PlaceFlag.NO_FLAG : PlaceFlag.FLAG; //Inverte o valor do boolean
    }

    protected PlaceFlag getPlaceFlag() {
        return placeFlag;
    }

    protected void toggleVisibility() {
        visibility = visibility.isVisible ? Visibility.INVISIBLE : Visibility.VISIBLE;
    }

    protected Visibility getVisibility() {
        return visibility;
    }
}

class MineField {
    private final Place[][] places;

    public MineField(int height, int width ) {
        places = new Place[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                places[i][j] = new Place();
            }
        }
    }

    public void setMineInAField(int a, int b) {
        places[a][b].setMine();
    }

    public boolean hasMine(int a, int b) {
        return places[a][b].hasMine();
    }

    public void printField() {
        for (int i = 0; i < places.length; i++) {

            //Esse bloco acrescenta as linhas decorativas da parte de cima
            if (i == 0) {
                System.out.print(" |");
                for (int j = 0; j < places[0].length; j++) {
                    System.out.print(j + 1);
                }
                System.out.println('|');
                System.out.print("-|");
                for (int j = 0; j < places[0].length; j++) {
                    System.out.print('-');
                }
                System.out.println('|');
            }

            for (int j = 0; j < places[i].length; j++) {

                //Esse bloco adiciona a linha decorativa esquerda
                if (j == 0) {
                    System.out.printf("%d|", i + 1);
                }

                if (!places[i][j].getVisibility().isVisible) { //Se for invisível
                    if (places[i][j].getPlaceFlag() == PlaceFlag.NO_FLAG) { //Se não estiver marcada como suspeita "NO_FLAG"
                        System.out.print('.');
                    } else { //Se estiver marcada como suspeita "FLAG"
                        System.out.print('*');
                    }

                } else { //Se for visível
                    switch (places[i][j].getPlaceState()) {
                        case FREE:
                            System.out.print('/'); //Se não for marcada
                            break;
                        case MINE:
                            System.out.print('X');
                            break;
                        case ALERT:
                            System.out.print(places[i][j].getMineAlert());
                            break;
                    }
                }

                //Esse bloco adiciona a linha decorativa direita
                if (j == places[i].length - 1) {
                    System.out.print('|');
                }
            }
            System.out.println();
        }

        //Esse bloco adiciona a linha decorativa inferior
        System.out.print("-|");
        for (int j = 0; j < places[0].length; j++) {
            System.out.print('-');
        }
        System.out.println('|');

    }

    public void randomMines ( int numberOfMines){
            Random random = new Random();
            for (int i = 0; i < numberOfMines; i++) {
                boolean isMineSet = false;
                while (!isMineSet) {
                    int a = random.nextInt(places.length); //Sorteia uma linha
                    int b = random.nextInt(places[0].length); //Sorteia uma coluna
                    if (!hasMine(a, b)) { //Se não houver uma mina, planta uma.
                        setMineInAField(a, b);
                        isMineSet = true;
                    }
                }
            }
        }

    public void updateMinesAlert() {
        int height = places.length;
        int width = places[0].length;

        //ATUALIZA OS 4 CANTOS (3 Places)
        //Canto Superior direito (0,0)
        if (!places[0][0].hasMine()) {
            byte mineAlertCounter = 0;
            if (places[0][1].hasMine()) {
                mineAlertCounter++;
            }
            if ((places[1][1].hasMine())) {
                mineAlertCounter++;
            }
            if ((places[1][0].hasMine())) {
                mineAlertCounter++;
            }
            places[0][0].setMineAlert(mineAlertCounter);
        }
        //Canto superior esquerdo (0, width-1)
        if (!places[0][width - 1].hasMine()) {
            byte mineAlertCounter = 0;
            if (places[0][width - 2].hasMine()) {
                mineAlertCounter++;
            }
            if ((places[1][width - 2].hasMine())) {
                mineAlertCounter++;
            }
            if ((places[1][width - 1].hasMine())) {
                mineAlertCounter++;
            }
            places[0][width - 1].setMineAlert(mineAlertCounter);
        }
        //Canto inferior esquerdo (height - 1, 0)
        if (!places[height - 1][0].hasMine()) {
            byte mineAlertCounter = 0;
            if (places[height - 2][0].hasMine()) {
                mineAlertCounter++;
            }
            if ((places[height - 2][1].hasMine())) {
                mineAlertCounter++;
            }
            if ((places[height - 1][1].hasMine())) {
                mineAlertCounter++;
            }
            places[height -1][0].setMineAlert(mineAlertCounter);
        }
        //Canto inferior direito (height - 1, width-1)
        if (!places[height - 1][width - 1].hasMine()) {
            byte mineAlertCounter = 0;
            if (places[height - 2][width - 1].hasMine()) {
                mineAlertCounter++;
            }
            if ((places[height - 2][width - 2].hasMine())) {
                mineAlertCounter++;
            }
            if ((places[height - 1][width - 2].hasMine())) {
                mineAlertCounter++;
            }
            places[height -1][width - 1].setMineAlert(mineAlertCounter);
        }

        //ATUALIZA OS LIMITES SUPERIOR E INFERIOR, EXCETO O PRIMEIRO E O ÚLTIMO LOCAL (5 Places)
        //Linha Superior
        for (int i = 1; i < width - 1; i++) {
            byte mineAlertCounter = 0;
            if ((!places[0][i].hasMine())) {
                //Local à Esquerda
                if (places[0][i - 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo à Esquerda
                if (places[1][i - 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo
                if (places[1][i].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo à Direito
                if (places[1][i + 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local à Direita
                if (places[0][i + 1].hasMine()) {
                    mineAlertCounter++;
                }
                places[0][i].setMineAlert(mineAlertCounter);
            }
        }
        //Linha Inferior
        for (int i = 1; i < width - 1; i++) {
            byte mineAlertCounter = 0;
            if ((!places[height - 1][i].hasMine())) {
                //Local à Esquerda
                if (places[height - 1][i - 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Acima à Esquerda
                if (places[height - 2][i - 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Acima
                if (places[height - 2][i].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Acima à Direito
                if (places[height - 2][i + 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local à Direita
                if (places[height - 1][i + 1].hasMine()) {
                    mineAlertCounter++;
                }
                places[height - 1][i].setMineAlert(mineAlertCounter);
            }
        }

        //ATUALIZA OS LIMITES ESQUERDO E DIREITO, EXCETO O PRIMEIRO E O ÚLTIMO LOCAL (5 Places)
        //Coluna Esquerda
        for (int i = 1; i < height - 1; i++) {
            byte mineAlertCounter = 0;
            if (!places[i][0].hasMine()) {
                //Local Acima
                if (places[i - 1][0].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Acima à Direito
                if (places[i - 1][1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local à Direita
                if (places[i][1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo à Direita
                if (places[i + 1][1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo
                if (places[i + 1][0].hasMine()) {
                    mineAlertCounter++;
                }
                places[i][0].setMineAlert(mineAlertCounter);
            }
        }
        //Coluna Direita
        for (int i = 1; i < height - 1; i++) {
            byte mineAlertCounter = 0;
            if (!places[i][width - 1].hasMine()) {
                //Local Acima
                if (places[i - 1][width - 1].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Acima à Esquerda
                if (places[i - 1][width - 2].hasMine()) {
                    mineAlertCounter++;
                }
                //Local à Esquerda
                if (places[i][width - 2].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo à Esquerda
                if (places[i + 1][width - 2].hasMine()) {
                    mineAlertCounter++;
                }
                //Local Abaixo
                if (places[i + 1][width - 1].hasMine()) {
                    mineAlertCounter++;
                }
                places[i][width - 1].setMineAlert(mineAlertCounter);
            }
        }

        //ATUALIZA O TODOS OS Places NO INTERIOR DE MineField, EXCLUINDO AS BORDAS
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (!places[i][j].hasMine()) {
                    byte mineAlertCounter = 0;
                    //Local Acima à Esquerda
                    if (places[i - 1][j - 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local Acima
                    if (places[i - 1][j].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local Acima à Direita
                    if (places[i - 1][j + 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local à Esquerda
                    if (places[i][j - 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local à Direita
                    if (places[i][j + 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local Abaixo à Esquerda
                    if (places[i + 1][j - 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local Abaixo
                    if (places[i + 1][j].hasMine()) {
                        mineAlertCounter++;
                    }
                    //Local Abaixo à Direita
                    if (places[i + 1][j + 1].hasMine()) {
                        mineAlertCounter++;
                    }
                    places[i][j].setMineAlert(mineAlertCounter);
                }
            }
        }
    }

    public void toggleFlag(int a, int b) {
        places[a][b].toggleFlag();
    }

    public boolean isFlagged(int a, int b) {
        return places[a][b].getPlaceFlag().flag;
    }

    public PlaceState getPlaceState(int a, int b) {
        return places[a][b].getPlaceState();
    }

    public Visibility isVisible(int a, int b) {
        return places[a][b].getVisibility();
    }

    public void toggleVisibility(int a, int b) {
        places[a][b].toggleVisibility();
    }

    public void revealNeighbors(int a, int b) { //Retorna "hasExploded = true" se houver ativado uma mina
        int height = places.length;
        int width = places[0].length;

        if (places[a][b].getPlaceState() == PlaceState.ALERT) {
            places[a][b].toggleVisibility();
        }

        if (places[a][b].getPlaceState() == PlaceState.MINE) {
            //Torna todas as minas visíveis
            for (Place[] p : places){
                for (Place j : p) {
                    if (j.hasMine()) {
                        j.toggleVisibility();
                    }
                }
            }
        }

        if (places[a][b].getPlaceState() == PlaceState.FREE) {
            places[a][b].toggleVisibility();

            //VERIFICA TODOS OS VIZINHOS
            //Antes, testa se o vizinho é válido. Bordas e cantos não tem todos os vizinhos.
            //Acima à Esquerda
            if (a - 1 >= 0 && b - 1 > 0) { //Verifica se o vizinho é válido e está invisível
                if (!places[a - 1][b - 1].getVisibility().isVisible) {
                    revealNeighbors(a - 1, b - 1);
                }
            }
            //Acima
            if (a - 1 >= 0) {
                if (!places[a - 1][b].getVisibility().isVisible) {
                    revealNeighbors(a - 1, b);
                }
            }
            //Acima à Direita
            if (a - 1 > 0 && b + 1 < width) {
                if (!places[a - 1][b + 1].getVisibility().isVisible) {
                    revealNeighbors(a - 1, b + 1);
                }
            }
            //À Esquerda
            if (b - 1 >= 0) {
                if (!places[a][b - 1].getVisibility().isVisible) {
                    revealNeighbors(a, b - 1);
                }
            }
            //À Direita
            if (b + 1 < width) {
                if (!places[a][b + 1].getVisibility().isVisible) {
                    revealNeighbors(a, b + 1);
                }
            }
            //Abaixo à Esquerda
            if (a + 1 < height && b - 1 >= 0) {
                if (!places[a + 1][b - 1].getVisibility().isVisible) {
                    revealNeighbors(a + 1, b - 1);
                }
            }
            //Abaixo
            if (a + 1 < height) {
                if (!places[a + 1][b].getVisibility().isVisible) {
                    revealNeighbors(a + 1, b);
                }
            }
            //Abaixo à Direita
            if (a + 1 < height && b + 1 < width) {
                if (!places[a + 1][b + 1].getVisibility().isVisible) {
                    revealNeighbors(a + 1, b + 1);
                }
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //System.out.print("How many mines do you want on the field? ");
        //int numberOfMines = scanner.nextInt();
        int numberOfMines = 3;

        final int width = 9;
        final int height = 9;

        int freePlaces = width * height - numberOfMines;
        int visibleFreePlaces = 0;
        boolean foundAllMines = false;

        MineField minefield = new MineField(height,width);
        //minefield.randomMines(numberOfMines);
        minefield.setMineInAField(2,0); // 1 , 3
        minefield.setMineInAField(2,2); // 3 , 3
        minefield.setMineInAField(8,1); // 2 , 9

        minefield.updateMinesAlert();
        minefield.printField();

        int minesFound = 0;
        int placesFlagged = 0;
        while (minesFound < numberOfMines && placesFlagged != numberOfMines) {
            System.out.print("Set/unset mines marks or claim a cell as free (x and y coordinates): ");
            //********** ATENÇÃO! EIXOS X E Y TROCADOS! **********

            String input = scanner.nextLine();
            String[] inputArray = input.split(" ");
            int a = Integer.parseInt(inputArray[0]) - 1; //Coordenada "a" (-1 para ajustar à MineField) EIXO X COLUNAS
            int b = Integer.parseInt(inputArray[1]) - 1; //Coordenada "b" (-1 para ajustar à MineField) EIXO Y LINHAS
            String command = inputArray[2];

            //SE O LOCAL ESCOLHIDO AINDA NÃO ESTIVER VISÍVEL
            if (!minefield.isVisible(b, a).isVisible) {
                //COMANDO "mine"
                if (command.equals("mine")) {
                    if (!minefield.isFlagged(b, a)) {
                        placesFlagged++;
                    } else {
                        placesFlagged--;
                    }
                    minefield.toggleFlag(b, a);
                    minefield.printField();
                }

                //COMANDO "free"
                if (command.equals("free")) {
                    minefield.revealNeighbors(b, a);
                    minefield.printField();
                    if (minefield.hasMine(b, a)) {
                        break;
                    } else {
                        //Conta a quantidade de Places visíveis e armazena na variável visibleFreePlaces
                        int tempFreePlaces = 0;
                        for (int i = 0; i < height; i++) {
                            for (int j = 0; j < width; j++) {
                                if (minefield.isVisible(i, j) == Visibility.VISIBLE) {
                                    tempFreePlaces++;
                                }
                            }
                        }
                        visibleFreePlaces = tempFreePlaces;
                        if (visibleFreePlaces == freePlaces) { //Verifica se o jogo foi vencido.
                            break;
                        }
                    }
                }
            }
            //SE O LOCAL ESCOLHIDO JÁ ESTIVER VISÍVEL
            else {
                System.out.println("This place is already free!");
            }
        }

        if (foundAllMines || visibleFreePlaces == freePlaces) {
            System.out.println("Congratulations! You found all mines!");
        } else {
            System.out.println("Game Over!");
        }

    }
}