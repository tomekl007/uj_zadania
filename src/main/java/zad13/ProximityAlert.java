package zad13;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tomasz.lelek on 25/03/15.
 */
class ProximityAlert implements ProximityAlertInterface {

    private double limit;
    protected Map<Integer, WarningListener> warningListeners = new LinkedHashMap<>();
    private static final AtomicInteger listenersCounter = new AtomicInteger();

    public ProximityAlert() {
    }

    @Override
    public void setCollisionDistance(double limit) {
        this.limit = limit;
    }

    @Override
    public int registerObject(ProximityWarningListener pwl, double limit) {
        int id = listenersCounter.incrementAndGet();
        warningListeners.put(id, new WarningListener(pwl, limit));
        return id;
    }

    @Override
    public boolean deregisterObject(int id) {
        if(!warningListeners.containsKey(id)) return false;

        System.out.println("deregistering id : " + id);
        warningListeners.remove(id);
        return true;
    }

    @Override
    public void newPosition(int id, Position2D pos) {
        WarningListener warningListener = warningListeners.get(id);
        if(warningListener != null){
            warningListener.setPosition2D(pos);
        }
        detectCollisions();
    }

    private void detectCollisions() {
        System.out.println("Test for collision  ");

        for(Map.Entry<Integer, WarningListener> entry : warningListeners.entrySet()){
            List<Integer> idsCollision = entry.getValue().collideWith(warningListeners);
            System.out.println("idsCollision : " + idsCollision);
            for(Integer id : idsCollision){
                disableListener(id);
            }
        }
        
        
    }

    private void disableListener(Integer id) {
        System.out.println("disable Listener : " + id );
        warningListeners.get(id).setEnable(false);
    }

    class WarningListener {
        private Position2D position2D;
        private final ProximityWarningListener proximityWarningListener;
        private final double limit;
        private boolean enable;

        public WarningListener(ProximityWarningListener proximityWarningListener, double limit) {
            this.proximityWarningListener = proximityWarningListener;
            this.limit = limit;
            this.enable = true;
        }

        public Position2D getPosition2D() {
            return position2D;
        }

        public void setPosition2D(Position2D position2D) {
            this.position2D = position2D;
        }

        public List<Integer> collideWith(Map<Integer, WarningListener> warningListeners) {
            List<Integer> collisions = new LinkedList<>();
            for(Map.Entry<Integer, WarningListener> entry : warningListeners.entrySet()) {
                if (isACollision(entry)) {
                    double distance = position2D.distance(entry.getValue().getPosition2D());
                    System.out.println("distance :" + distance +" ,limit : " + limit);//todo which limit should i take ?
                    if (distance >= entry.getValue().limit) {
                        collisions.add(entry.getKey());
                        entry.getValue().proximityWarningListener.proximityWarning(entry.getKey());
                    }
                }
            }
            return collisions;
        }

        private boolean isACollision(Map.Entry<Integer, WarningListener> entry) {
            return entry.getValue().getPosition2D() != null
                    && position2D != null 
                    && !this.equals(entry.getValue())
                    && this.isEnable() 
                    && entry.getValue().isEnable();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WarningListener that = (WarningListener) o;

            if (Double.compare(that.limit, limit) != 0) return false;
            if (position2D != null ? !position2D.equals(that.position2D) : that.position2D != null) return false;
            if (proximityWarningListener != null ? !proximityWarningListener.equals(that.proximityWarningListener) : that.proximityWarningListener != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = position2D != null ? position2D.hashCode() : 0;
            result = 31 * result + (proximityWarningListener != null ? proximityWarningListener.hashCode() : 0);
            temp = Double.doubleToLongBits(limit);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
