
package zad3;

public class LifeRules implements LifeRulesInterface {

	@Override
	public boolean isOn(boolean state, int numberOfLiveNeighbors) {
// zwykle reguly gry		
		if ( state ) {
			if ( ( numberOfLiveNeighbors > 1 ) && ( numberOfLiveNeighbors < 4 ) ) return true;
		} else {
			if ( numberOfLiveNeighbors == 3 ) return true;
		}
		return false;
	}

}
