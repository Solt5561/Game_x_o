import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameActionListener implements ActionListener {
    private GameButton button;
    private int row;
    private int cell;

    public GameActionListener(int rowNum, int cellNum, GameButton gameButton) {
        this.row = rowNum;
        this.cell = cellNum;
        this.button = gameButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();

        if (board.isTurnable(row, cell)){
            updateByPlayersDate(board);

            if (board.checkWin()){
            }else
            if (board.isFull()){
                board.getGame().showMessage("Ничья!");
                board.emptyField();
            }else{
                updateByAiData(board);
            }
        }else
            board.getGame().showMessage("Error");
    }

    private void updateByAiData(GameBoard board) {
        int maxPoint = -1;

        int x = -1;
        int y = -1;

        for (int i = 0; i < GameBoard.dimension * GameBoard.dimension; i++) {
            int j_ = i % GameBoard.dimension;
            int i_ = i / GameBoard.dimension;

            if (board.isTurnable(i_, j_)) {
                board.updateGameField(i_, j_);
                if (board.checkWin()) {
                    x = i_;
                    y = j_;
                    board.getGame().showMessage("Компьютер выйграл!");
                    return;
                }

                int point = 0;
                point = point + getPoint(i_, j_, board);

                board.getGame().passTurn();
                board.setNullGameField(i_, j_);

                board.updateGameField(i_, j_);
                if (board.checkWin()) {
                    point = point + 10;
                }

                board.getGame().passTurn();
                board.setNullGameField(i_, j_);

                if (maxPoint < point) {
                    maxPoint = point;
                    x = i_;
                    y = j_;
                }
            }

        }

        board.updateGameField(x, y);
        int cellIndex = GameBoard.dimension *x + y;
        board.getGameButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        board.getGame().passTurn();
    }

    private void updateByPlayersDate(GameBoard board) {
        board.updateGameField(row, cell);

        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()){
            button.getBoard().getGame().showMessage("Вы выйграли");
        }else {
            board.getGame().passTurn();
        }
    }

    private static int getPoint(int i, int j, GameBoard board) {
        int result = 0;

        result += getOnePoint(i - 1, j - 1, board);
        result += getOnePoint(i - 1, j, board);
        result += getOnePoint(i - 1, j + 1, board);
        result += getOnePoint(i, j - 1, board);
        result += getOnePoint(i, j + 1, board);
        result += getOnePoint(i + 1, j - 1, board);
        result += getOnePoint(i + 1, j, board);
        result += getOnePoint(i + 1, j + 1, board);

        return result;
    }

    private static int getOnePoint(int i, int j, GameBoard board) {

        if (i < 0 || i >= GameBoard.dimension || j < 0 || j >= GameBoard.dimension) {
            return 0;
        }
        if (board.getGameField(i,j) == board.getGame().getCurrentPlayer().getPlayerSign()) {
            return 2;
        }
        if ((board.getGameField(i,j) != board.getGame().getCurrentPlayer().getPlayerSign())
                && (board.getGameField(i,j) != board.nullSymbol)) {
            return 0;
        }
        return 1;
    }

}


