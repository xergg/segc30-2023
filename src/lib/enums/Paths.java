package lib.enums;

/**
 * Enum that has explicit paths
 */
public enum Paths {
        //Users
        USER_DIRECTORY("./data/server-data/users"),
        USER_DATA(Paths.USER_DIRECTORY.getPath() + "./users.dat"),

        //Wines
        WINE_DIRECTORY( "./data/server-data/wines"),
        WINE_DATA(Paths.WINE_DIRECTORY.getPath() + "./wines.dat"),
        
        //Image 
        IMAGES("./data/server-data/server-images"),

        //Client 
        CLIENT_DIRECTORY("./client-images");

    private final String path;

    /**
     * constructor with a given path
     * @param path given path
     */
    Paths (String path) {
        this.path = path ;
    }


    /**
     * returns a path
     * @return given path
     */
    public String getPath()
    {
        return path;
    }
}