package zad8;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplaySystemTest {


    @Test
    public void shouldDisplayThreeRows(){
        //given
        zad8.DisplaySystem displaySystem = new zad8.DisplaySystem();
        int displayId = displaySystem.registerDisplay(3);
        displaySystem.toDisplay(displayId, "first");
        String secondMessage = "second";
        displaySystem.toDisplay(displayId, secondMessage);
        displaySystem.toDisplay(displayId, "third");
        String fourthMessage = "fourth";
        displaySystem.toDisplay(displayId, fourthMessage);
        //when
        String[] messages = displaySystem.get(displayId);
        //then
        assertThat(messages[2]).isEqualTo(fourthMessage);
        assertThat(messages[0]).isEqualTo(secondMessage);

        displaySystem.deregisterDisplay(displayId);
        boolean result = displaySystem.toDisplay(displayId, "first");
        assertThat(result).isFalse();
    }

    @Test
    public void shouldDisplayForGroup(){
        //given
        zad8.DisplaySystem displaySystem = new zad8.DisplaySystem();
        int firstDisplayId = displaySystem.registerDisplay(3);
        int secondDisplayId = displaySystem.registerDisplay(2);
        int groupId = displaySystem.createGroup();
        displaySystem.addDisplayToGroup(firstDisplayId, groupId);
        displaySystem.addDisplayToGroup(secondDisplayId, groupId);
        //when
        String message = "message";
        displaySystem.toGroup(groupId, message);
        //then
        assertThat(displaySystem.get(firstDisplayId)[0]).isEqualTo(message);
        assertThat(displaySystem.get(secondDisplayId)[0]).isEqualTo(message);

        displaySystem.removeGroup(groupId);
        boolean result = displaySystem.addDisplayToGroup(firstDisplayId, groupId);
        assertThat(result).isFalse();

    }
    
    @Test
    public void shouldAddGroupToGroup(){
        //given
        zad8.DisplaySystem displaySystem = new zad8.DisplaySystem();
        int firstDisplayId = displaySystem.registerDisplay(3);
        int secondDisplayId = displaySystem.registerDisplay(2);
        int groupId = displaySystem.createGroup();
        displaySystem.addDisplayToGroup(firstDisplayId, groupId);
        displaySystem.addDisplayToGroup(secondDisplayId, groupId);

        int idGroupSecondDisplay = displaySystem.registerDisplay(10);
        int secondGroupId = displaySystem.createGroup();
        displaySystem.addDisplayToGroup(idGroupSecondDisplay, secondGroupId);
        
        displaySystem.addGroupToGroup(groupId, secondGroupId);
        
        int displayRecursiveId = displaySystem.registerDisplay(2);
        int groupRecursive = displaySystem.createGroup();
        
        displaySystem.addDisplayToGroup(displayRecursiveId, groupRecursive);
        displaySystem.addGroupToGroup(secondGroupId, groupRecursive);
        //when
        String message = "message";
        displaySystem.toGroup(groupId, message);
        //then
        assertThat(displaySystem.get(firstDisplayId)[0]).isEqualTo(message);
        assertThat(displaySystem.get(secondDisplayId)[0]).isEqualTo(message);
        assertThat(displaySystem.get(idGroupSecondDisplay)[0]).isEqualTo(message);
        assertThat(displaySystem.get(displayRecursiveId)[0]).isEqualTo(message);

        displaySystem.removeGroup(groupId);
        boolean result = displaySystem.addDisplayToGroup(firstDisplayId, secondGroupId);
        assertThat(result).isTrue();
        
    }
    

}