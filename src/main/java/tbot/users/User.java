package tbot.users;

public class User {

    private String nameGame;
    private String namePlatform;
    private State state = State.START;

    public String getNameGame(){ return nameGame ;}

    public String getNamePlatform() {
        return namePlatform;
    }

    public State getState() {
        return  state;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public void setNamePlatform(String namePlatform) {
        this.namePlatform = namePlatform;
    }

    public void setState(State state) {
        this.state = state;
    }
}
