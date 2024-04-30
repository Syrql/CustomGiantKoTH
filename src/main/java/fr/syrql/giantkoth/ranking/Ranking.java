package fr.syrql.giantkoth.ranking;

import java.util.List;

public class Ranking {

    private int number;
    private List<String> commands;

    public Ranking(int number, List<String> commands) {
        this.number = number;
        this.commands = commands;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
