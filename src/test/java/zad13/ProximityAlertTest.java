package zad13;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProximityAlertTest {
    
    @Test
    public void shouldSimulateCollisionsCorrectly(){
        //given
        ProximityAlert proximityAlert = new ProximityAlert();
        proximityAlert.setCollisionDistance(2);
        int idFirst = proximityAlert.registerObject(new ProximityAlertInterface.ProximityWarningListener() {
            @Override
            public void proximityWarning(int id) {
                System.out.println("warning for idFirst " + id);
            }
        }, 1);
        int idCollide = proximityAlert.registerObject(new ProximityAlertInterface.ProximityWarningListener() {
            @Override
            public void proximityWarning(int id) {
                System.out.println("warning for idCollide " + id);
                assertThat(id).isEqualTo(2);
            }
        }, 1);
        int idThird = proximityAlert.registerObject(new ProximityAlertInterface.ProximityWarningListener() {
            @Override
            public void proximityWarning(int id) {
                System.out.println("warning for idThird " + id);
            }
        }, 1);
        //when
        proximityAlert.newPosition(idFirst, new ProximityAlertInterface.Position2D(1, 1));
        proximityAlert.newPosition(idCollide, new ProximityAlertInterface.Position2D(2,2));
        proximityAlert.newPosition(idThird, new ProximityAlertInterface.Position2D(5,5));
        //then


    }

}