package zad12;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectToDOMTest {
    
    @Test
    public void shouldCreateDocumentFromPublicObjectFields() throws TransformerException {
        //given
        InnerAggregate innerAggregate = new InnerAggregate(100d, 9999d);
        Aggregate aggregate = new Aggregate("aggregateNameField", innerAggregate);
        TestObject testObject = new TestObject("publicName", 1d, "privateName", aggregate);
        ObjectToDOM objectToDOM = new ObjectToDOM();
        //when
        Document document = objectToDOM.getDocument(testObject);    
        //then
        //assertThat(document.getChildNodes().)
    }

    
    class TestObject{
        public final String name;
        public final double primitive;
        private final String privateName;
        public final Aggregate aggregate;

        TestObject(String name, double primitive, String privateName, Aggregate aggregate) {
            this.name = name;
            this.primitive = primitive;
            this.privateName = privateName;
            this.aggregate = aggregate;
        }

        @Override
        public String toString() {
            return "TestObject{" +
                    "name='" + name + '\'' +
                    ", privateName='" + privateName + '\'' +
                    ", aggregate=" + aggregate +
                    '}';
        }
    }
    class Aggregate{
        public final String aggregateName;
        public final InnerAggregate innerAggregate;

        Aggregate(String aggregateName, InnerAggregate innerAggregate) {
            this.aggregateName = aggregateName;
            this.innerAggregate = innerAggregate;
        }

    }
    
    class InnerAggregate{
        public final Double publicDouble;
        private final double privateDouble;

        InnerAggregate(Double publicDouble, double privateDouble) {
            this.publicDouble = publicDouble;
            this.privateDouble = privateDouble;
        }
    }
    
}