package agh.ics.oop;

public class World {
    public static void main(String[] args){
        System.out.println("start");
        Animal animal = new Animal(50,new Vector2d(0,0));
        animal.move(MoveDirection.ROT315);
        animal.move(MoveDirection.ROT90);
        System.out.println("================");
        Map map = new Map(4,5,true);
        Vector2d testV = new Vector2d(0,2);
        System.out.println(map.wrapPosition(testV));
        testV = new Vector2d(-1,2);
        System.out.println(map.wrapPosition(testV));
        testV = new Vector2d(1,-1);
        System.out.println(map.wrapPosition(testV));
//        Application.launch(App.class, args);
        //działa?
    }

}
