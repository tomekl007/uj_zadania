

import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class GeneratorClient {
 public static void main( String[] argv ) throws Exception {
  ORB orb = ORB.init( argv, null );
  org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references( "NameService" );
  NamingContext namingContext = NamingContextHelper.narrow( namingContextObj );

  NameComponent[] path = {
    new NameComponent( "optimization", "Object" )
  };

  org.omg.CORBA.Object envObj = namingContext.resolve( path );
  optimization c = optimizationHelper.narrow( envObj );
  IntHolder ih = new IntHolder();
     IntHolder ih2 = new IntHolder();
     IntHolder ih3 = new IntHolder();
  
     c.register((short)1, 10000, ih);
     c.register((short)2, 5000, ih2);
     c.register((short)3, 4000, ih3);
     System.out.println("get id : "+ ih.value);
     Thread.sleep(1000);
     c.hello(ih3.value);
     Thread.sleep(5000);
     rangeHolder rh = new rangeHolder();
     c.best_range(rh);
     System.out.println("rh.from " +  rh.value.from);
     System.out.println("rh.to " +  rh.value.to);

 }
  
    
    
}
