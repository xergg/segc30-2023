package lib.enums;

public enum Paths {
        //Users
        USER_DIRECTORY("./data/server-data/users"),
        USER_DATA(Paths.USER_DIRECTORY.getPath() + "./users.dat"),

        //Wines
        WINE_DIRECTORY( "./data/server-data/wines"),
        WINE_DATA(Paths.WINE_DIRECTORY.getPath() + "./wines.dat"),
        
        //Image 
        IMAGES("./data/server-data/server-images");

    private final String path;

    Paths (String path) {
        this.path = path ;
    }

    public String getPath()
    {
        return path;
    }
}