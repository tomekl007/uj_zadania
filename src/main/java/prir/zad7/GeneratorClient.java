import org.omg.CORBA.IntHolder;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

public class GeneratorClient {
    public static void main(String[] argv) throws Exception {
        ORB orb = ORB.init(argv, null);
        org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references("NameService");
        NamingContext namingContext = NamingContextHelper.narrow(namingContextObj);
        NameComponent[] path = {
                new NameComponent( "Optymalizacja", "Object" )
        };

        org.omg.CORBA.Object envObj = namingContext.resolve(path);
        final optimization c = optimizationHelper.narrow(envObj);
        final IntHolder ih = new IntHolder();
        rangeHolder rh = new rangeHolder();

        c.register((short) 1, 6000, ih);
        for (int i = 0; i < 3; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    IntHolder id = new IntHolder();
                    rangeHolder rh = new rangeHolder();
                    c.register((short) (finalI + 3), 3000, id);
                    while (true) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        c.hello(id.value);
                        c.best_range(rh);
                        System.out.println("from-to " + rh.value.from + "-" + rh.value.to);
                    }
                }
            }).start();
        }
        Thread.sleep(4000);
        for (int i = 30; i < 41; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    IntHolder id = new IntHolder();
                    rangeHolder rh = new rangeHolder();
                    c.register((short) (finalI), 3000, id);
                    for (int j = 0; j < 5; j++) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        c.hello(id.value);
                        c.best_range(rh);
                        System.out.println("from-to " + rh.value.from + "-" + rh.value.to);
                    }
                }
            }).start();
        }

        while (true) {
            Thread.sleep(5000);
            c.hello(ih.value);
            c.best_range(rh);
            System.out.println("from-to " + rh.value.from + "-" + rh.value.to);
        }
    }
}
/*
    przez pierwsze 4 sekundy
    ma byc 3-5
    potem przez 7 sekund ma byc 3-40
    30-40
    a potem znowu 3-5
 */