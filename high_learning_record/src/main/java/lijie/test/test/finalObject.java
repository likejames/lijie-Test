package lijie.test.test;

public class finalObject {

    final static person p = new person("A",3);
    public static void main(String[] args) {
        p.setAge(8);
        System.out.println(p);
        //person p1 = new person("B",0);
        //p = p1;
    }

}

class person{//person中设置属性name ,age ,并生成get ,set 方法，重写Tostring方法
    String name ;
    int age ;

    public person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.age;
    }

}