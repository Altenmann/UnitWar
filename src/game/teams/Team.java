package game.teams;

import java.awt.*;

public class Team {

    protected Color teamColor, secondaryColor;

    public Team(Color teamColor, Color secondaryColor) {
        this.teamColor = teamColor;
        this.secondaryColor = secondaryColor;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
