

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
  
  c.register((short)1, 100, ih);
     System.out.println("get id : "+ ih.value);

 }
}
