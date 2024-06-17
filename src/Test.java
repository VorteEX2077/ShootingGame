class Demo {
    public void xyz() {
        System.out.println("Hello from Demo");
    }
}

public class Test extends Demo{

   public void abc() {
       System.out.println("Hello from TEst");
   }
}

class Main {
    public static void main(String[] args) {
        Demo demo = new Test();
       // demo.abc();
    }
}
