package CirclesTest;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable
{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
    int getRadius();

}

class MovablePoint implements Movable
{
    int x;
    int y;
    int xSpeed;
    int ySpeed;

    MovablePoint(int x, int y, int xSpeed, int ySpeed)
    {
        this.x=x;
        this.y=y;
        this.xSpeed=xSpeed;
        this.ySpeed=ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        //x y
        if(this.y+ySpeed>MovablesCollection.maxY)
        {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds",x,y+ySpeed));
        }
        this.y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(this.y-ySpeed<0)
        {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds",x,y-ySpeed));
        }
        this.y-=ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(this.x+xSpeed>MovablesCollection.maxX)
        {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds",x+xSpeed,y));
        }
        this.x+=xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(this.x-xSpeed<0)
        {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds",x-xSpeed,y));
        }
        this.x-=xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public int getRadius() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)",x,y);
    }
}

class MovableCircle implements Movable
{

    int radius;
    MovablePoint centar;

    MovableCircle(int radius, MovablePoint center)
    {
        this.radius=radius;
        this.centar=center;
    }
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(centar.getCurrentYPosition()>MovablesCollection.maxY)
        {
            throw new ObjectCanNotBeMovedException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection"
            ,centar.x,centar.y,radius));
        }
        this.centar.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(centar.getCurrentYPosition()<0)
        {
            throw new ObjectCanNotBeMovedException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection"
                    ,centar.x,centar.y,radius));
        }
        this.centar.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(this.centar.getCurrentXPosition()>MovablesCollection.maxX)
        {
            throw new ObjectCanNotBeMovedException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection"
                    ,centar.x,centar.y,radius));
        }
        this.centar.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(this.centar.getCurrentXPosition()<0)
        {
            throw new ObjectCanNotBeMovedException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection"
                    ,centar.x,centar.y,radius));
        }
        this.centar.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return centar.x;
    }

    @Override
    public int getCurrentYPosition() {
        return centar.y;
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d",centar.x,centar.y,radius);
    }
}

class ObjectCanNotBeMovedException extends Exception
{
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

class MovableObjectNotFittableException extends Exception
{
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

class MovablesCollection
{
    Movable [] movable;
    static int maxX;
    static int maxY;
    MovablesCollection(int x_MAX, int y_MAX)
    {
        this.maxX=x_MAX;
        this.maxY=y_MAX;
        this.movable=new Movable[0];
    }

    public static void setxMax(int max) {
        MovablesCollection.maxX=max;
    }

    public static void setyMax(int max) {
        MovablesCollection.maxY=max;
    }
    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(m.getType().equals(TYPE.CIRCLE))
        {
            if(m.getCurrentXPosition()-m.getRadius()<0
            ||m.getCurrentXPosition()+m.getRadius()>maxX
            ||m.getCurrentYPosition()+m.getRadius()>maxY
            ||m.getCurrentYPosition()-m.getRadius()<0)
            {
                throw new MovableObjectNotFittableException(String.format(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection"
                        ,m.getCurrentXPosition(),m.getCurrentYPosition(),m.getRadius())));
            }
        }
//        else if (m.getType().equals(TYPE.POINT)) {
//            if(m.getCurrentXPosition()<0||m.getCurrentXPosition()>maxX||
//            m.getCurrentYPosition()<0||m.getCurrentYPosition()>maxY)
//            {
//                throw new MovableObjectNotFittableException("");
//            }
//        }
        this.movable= Arrays.copyOf(movable,movable.length+1);
        movable[movable.length-1]=m;
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction)
    {
        Arrays.stream(movable)
                .filter(m->m.getType().equals(type))
                .forEach(m->{
                    try {
                        switch (direction)
                        {
                            case UP :
                                m.moveUp();
                                break;
                            case DOWN:
                                m.moveDown();
                                break;
                            case LEFT:
                                m.moveLeft();
                                break;
                            case RIGHT:
                                m.moveRight();
                                break;
                        }
                    }
                    catch (ObjectCanNotBeMovedException e)
                    {
                        System.out.println(e.getMessage());
                    }

                });
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n",movable.length));
        Arrays.stream(movable)
                .forEach(m->sb.append(m).append("\n"));
        //sb.setLength(sb.length()-1);
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
