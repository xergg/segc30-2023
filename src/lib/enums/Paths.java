package lib.enums;

public enum Paths {
        //Users
        USER_DIRECTORY("./server-data/users"),
        USER_DATA("./users.dat"),

        //Wines
        WINE_DIRECTORY( "./server-data/wines"),
        WINE_DATA("./wines.dat"),
        
        //Image 
        IMAGES("./server-data/server-images");

    private final String path;

    Paths (String path) {
        this.path = path ;
    }

    public String getPath()
    {
        return path;
    }
}