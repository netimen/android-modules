import com.bookmate.bus.Bus;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BusTest {

    private Bus bus;
    private int event1GotParam;
    private int event2GotParam;

    @Before
    public void setUp() throws Exception {
        bus = new Bus();
        event1GotParam = event2GotParam = -1;
    }

    @Test
    public void testEventListener() {
        bus.register(TestEvent.class, new Bus.EventListener<TestEvent>() {
            @Override
            public void onEvent(TestEvent event) {
                event1GotParam = event.param;
            }
        });
        bus.register(TestEvent.class, new Bus.EventListener<TestEvent>() {
            @Override
            public void onEvent(TestEvent event) {
                event2GotParam = event.param;
            }
        });
        final int eventParam = 5;
        bus.event(new TestEvent(eventParam));
        assertEquals(eventParam, event1GotParam);
        assertEquals(eventParam, event2GotParam);
    }

    @Test
    public void testRequestReturnsDefaultResult() {
        assertEquals(bus.request(new TestRequestDefaultResult()), TestRequestDefaultResult.RESULT);
    }

    @Test
    public void testRequestReturnsNull() {
        assertNull(bus.request(new TestRequest(0)));
    }

    @Test
    public void testRequestGotCorrectResult() {
        bus.register(TestRequest.class, new Bus.RequestProcessor<Integer, TestRequest>() {
            @Override
            public Integer process(TestRequest request) {
                return request.param;
            }
        });
        final Integer param = 5;
        assertEquals(bus.request(new TestRequest(param)), param);
    }

    private static class TestRequestDefaultResult extends Bus.Request<Integer> {

        public static final Integer RESULT = 0;

        @Override
        protected Integer defaultResult() {
            return RESULT;
        }
    }

    private static class TestRequest extends Bus.Request<Integer> {
        private final int param;

        private TestRequest(int param) {
            this.param = param;
        }
    }

    private static class TestEvent {
        public final int param;

        private TestEvent(int param) {
            this.param = param;
        }
    }
}