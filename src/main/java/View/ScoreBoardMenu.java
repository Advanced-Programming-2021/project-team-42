package View;

import java.util.regex.Pattern;

public class ScoreBoardMenu extends Menu {

    public ScoreBoardMenu(Menu parentMenu) {
        super("ScoreBoard Menu", parentMenu);
        subMenus.put(Pattern.compile("scoreboard show"), this.showScores());
    }

    public Menu showScores() {
        return new Menu("Scoreboard Show", this) {
            @Override
            public void show() {
            }

            @Override
            public void execute() {

            }
        };
    }

    public void show() {

    }

    public void execute() {

    }

    public void run() {
    }

}
