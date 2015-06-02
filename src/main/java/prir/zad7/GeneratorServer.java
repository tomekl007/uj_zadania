

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class GeneratorServer {
 public static void main( String[] argv ) {
  try {
    ORB orb = ORB.init( argv, null );
    POA rootpoa = (POA)orb.resolve_initial_references( "RootPOA" );
    rootpoa.the_POAManager().activate();
      
    optimizationImpl cimpl = new optimizationImpl();
    org.omg.CORBA.Object ref = rootpoa.servant_to_reference( cimpl );

    System.out.println( orb.object_to_string( ref ) );

    org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references( "NameService" );
    NamingContext nCont = NamingContextHelper.narrow( namingContextObj ); 
    NameComponent[] path = {
       new NameComponent( "optimization", "Object" )
    };

    nCont.rebind( path, ref );
    orb.run();
  }
  catch ( Exception e ) { }
 }
}
