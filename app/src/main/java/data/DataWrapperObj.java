package data;

import java.util.List;

public class DataWrapperObj {

    private LevelObj[] levelObjs;
    private List<String> passedLevels;
    private List<String> lockedLevels;


    public LevelObj[] getLevelObjs() {
        return levelObjs;
    }

    public void setLevelObjs(LevelObj[] levelObjs) {
        this.levelObjs = levelObjs;
    }

    public List<String> getPassedLevels() {
        return passedLevels;
    }

    public void setPassedLevels(List<String> passedLevels) {
        this.passedLevels = passedLevels;
    }

    public List<String> getLockedLevels() {
        return lockedLevels;
    }

    public void setLockedLevels(List<String> lockedLevels) {
        this.lockedLevels = lockedLevels;
    }
}
