package Enums;

import Classes.Vector2d;

import java.security.InvalidParameterException;

public enum MapDirection {
    N, NE, E, SE, S, SW, W, NW;

    public Vector2d toUnitVector() {
        switch (this) {
            case N:
                return new Vector2d(0, 1);
            case NE:
                return new Vector2d(1, 1);
            case E:
                return new Vector2d(1, 0);
            case SE:
                return new Vector2d(1, -1);
            case S:
                return new Vector2d(0, -1);
            case SW:
                return new Vector2d(-1, -1);
            case W:
                return new Vector2d(-1, 0);
            case NW:
                return new Vector2d(-1, 1);
            default:
                throw new InvalidParameterException("invalid parameter");
        }
    }

    public MapDirection intToMapDirection(int number) {
        switch (number) {
            case 0:
                return N;
            case 1:
                return NE;
            case 2:
                return E;
            case 3:
                return SE;
            case 4:
                return S;
            case 5:
                return SW;
            case 6:
                return W;
            case 7:
                return NW;
            default:
                throw new InvalidParameterException("invalid parameter");
        }
    }
}
