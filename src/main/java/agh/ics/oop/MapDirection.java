package agh.ics.oop;

public enum MapDirection {
    NORTH (0,1),
    NORTHEAST (1,1),
    EAST (1,0),
    SOUTHEAST (1,-1),
    SOUTH (0,-1),
    SOUTHWEST (-1,-1),
    WEST (-1,0),
    NORTHWEST (-1,1);

    private final Vector2d vector2d;

    MapDirection(int x, int y) {
        vector2d = new Vector2d(x,y);
    }

    public Vector2d toVector(){
        return vector2d;
    }


    @Override
    public String toString() {
        return switch (this){
            case EAST -> "E";
            case NORTHEAST -> "NE";
            case WEST -> "W";
            case SOUTHEAST -> "SE";
            case NORTH -> "N";
            case SOUTHWEST -> "SW";
            case SOUTH -> "S";
            case NORTHWEST -> "NW";
        };
    }
}
