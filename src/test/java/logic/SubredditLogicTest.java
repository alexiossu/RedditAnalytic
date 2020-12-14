package logic;

import common.TomcatStartUp;
import common.ValidationException;
import dal.EMFactory;
import entity.Subreddit;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Shariar
 */
class SubredditLogicTest {

    private SubredditLogic subreddit;
    private Subreddit expectedEntity;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/RedditAnalytic", "common.ServletListener" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        subreddit = LogicFactory.getFor( "Subreddit" );
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the Subreddit to not rely on any logic functionality , just for testing
        Subreddit entity = new Subreddit();
        entity.setName("Junit 5 Test" );
        entity.setUrl( "http://localhost/junit" );
        entity.setSubscribers( 10 );

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //add an Subreddit to hibernate, Subreddit is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedEntity = em.merge( entity );
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            subreddit.delete( expectedEntity );
        }
    }

    @Test
    final void testGetAll() {
        //get all the Subreddit from the DB
        List<Subreddit> list = subreddit.getAll();
        //store the size of list, this way we know how many Subreddits exits in DB
        int originalSize = list.size();

        //make sure Subreddit was created successfully
        assertNotNull( expectedEntity );
        //delete the new Subreddit
        subreddit.delete( expectedEntity );

        //get all Subreddits again
        list = subreddit.getAll();
        //the new size of Subreddits must be one less
        assertEquals( originalSize - 1, list.size() );
    }

    /**
     * helper method for testing all Subreddit fields
     *
     * @param expected
     * @param actual
     */
    private void assertSubredditEquals(Subreddit expected, Subreddit actual ) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getName(), actual.getName() );
        assertEquals( expected.getUrl(), actual.getUrl() );
        assertEquals( expected.getSubscribers(), actual.getSubscribers() );
    }

    @Test
    final void testGetWithId() {
        //using the id of test Subreddit get another Subreddit from logic
        Subreddit returnedSubreddit = subreddit.getWithId( expectedEntity.getId() );

        //the two Subreddits (testSubreddits and returnedSubreddits) must be the same
        assertSubredditEquals( expectedEntity, returnedSubreddit );
    }

    @Test
    final void testGetSubredditWithName() {
        Subreddit returnedSubreddit = subreddit.getSubredditWithName( expectedEntity.getName() );

        //the two Subreddits (testSubreddits and returnedSubreddits) must be the same
        assertSubredditEquals( expectedEntity, returnedSubreddit );
    }

    @Test
    final void testGetSubredditWIthURL() {
        Subreddit returnedSubreddit = subreddit.getSubredditWithURL( expectedEntity.getUrl() );

        //the two Subreddits (testSubreddits and returnedSubreddits) must be the same
        assertSubredditEquals( expectedEntity, returnedSubreddit );
    }

    @Test
    final void testGetSubredditWithSubscriberCount() {
        int foundFull = 0;
        List<Subreddit> returnedSubreddit = subreddit.getSubredditWithSubscribers( expectedEntity.getSubscribers() );
        for( Subreddit Subreddit: returnedSubreddit ) {
            //all Subreddits must have the same password
            assertEquals( expectedEntity.getSubscribers(), Subreddit.getSubscribers() );
            //exactly one Subreddit must be the same
            if( Subreddit.getId().equals( expectedEntity.getId() ) ){
                assertSubredditEquals( expectedEntity, Subreddit );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( SubredditLogic.NAME, new String[]{ "Test Create Entity" } );
        sampleMap.put( SubredditLogic.URL, new String[]{ "http://testCreateSubreddit" } );
        sampleMap.put( SubredditLogic.SUBSCRIBERS, new String[]{ "100" } );

        Subreddit returnedSubreddit = subreddit.createEntity( sampleMap );
        subreddit.add( returnedSubreddit );

        returnedSubreddit = subreddit.getSubredditWithName( returnedSubreddit.getName() );

        assertEquals( sampleMap.get( SubredditLogic.NAME )[ 0 ], returnedSubreddit.getName() );
        assertEquals( sampleMap.get( SubredditLogic.URL )[ 0 ], returnedSubreddit.getUrl() );
        assertEquals( sampleMap.get( SubredditLogic.SUBSCRIBERS )[ 0 ], String.valueOf(returnedSubreddit.getSubscribers()) );

        subreddit.delete( returnedSubreddit );
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( SubredditLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
        sampleMap.put( SubredditLogic.NAME, new String[]{ expectedEntity.getName() } );
        sampleMap.put( SubredditLogic.URL, new String[]{ expectedEntity.getUrl() } );
        sampleMap.put( SubredditLogic.SUBSCRIBERS, new String[]{ String.valueOf(expectedEntity.getSubscribers()) } );

        Subreddit returnedSubreddit = subreddit.createEntity( sampleMap );

        assertSubredditEquals( expectedEntity, returnedSubreddit );
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( SubredditLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( SubredditLogic.NAME, new String[]{ expectedEntity.getName() } );
            map.put( SubredditLogic.URL, new String[]{ expectedEntity.getUrl() } );
            map.put( SubredditLogic.SUBSCRIBERS, new String[]{ String.valueOf(expectedEntity.getSubscribers()) } );
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.ID, null );
        assertThrows( NullPointerException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.NAME, null );
        assertThrows( NullPointerException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.URL, null );
        assertThrows( NullPointerException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.URL, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.SUBSCRIBERS, null );
        assertThrows( NullPointerException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.SUBSCRIBERS, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> subreddit.createEntity( sampleMap ) );
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( SubredditLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( SubredditLogic.NAME, new String[]{ expectedEntity.getName() } );
            map.put( SubredditLogic.URL, new String[]{ expectedEntity.getUrl() } );
            map.put( SubredditLogic.SUBSCRIBERS, new String[]{ String.valueOf(expectedEntity.getSubscribers()) } );
        };

        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            //from 97 inclusive to 123 exclusive
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.ID, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.ID, new String[]{ "12b" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.NAME, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.NAME, new String[]{ generateString.apply( 101 ) } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.URL, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.URL, new String[]{ "http://"+generateString.apply( 249 ) } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( SubredditLogic.SUBSCRIBERS, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );
        sampleMap.replace( SubredditLogic.SUBSCRIBERS, new String[]{ "-1" } );
        assertThrows( ValidationException.class, () -> subreddit.createEntity( sampleMap ) );
    }

    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( SubredditLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( SubredditLogic.NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( SubredditLogic.URL, new String[]{ "http://"+generateString.apply( 1 ) } );
        sampleMap.put( SubredditLogic.SUBSCRIBERS, new String[]{ "0" } );

        //idealy every test should be in its own method
        Subreddit returnedSubreddit = subreddit.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( SubredditLogic.ID )[ 0 ] ), returnedSubreddit.getId() );
        assertEquals( sampleMap.get( SubredditLogic.NAME )[ 0 ], returnedSubreddit.getName() );
        assertEquals( sampleMap.get( SubredditLogic.URL )[ 0 ], returnedSubreddit.getUrl() );
        assertEquals( sampleMap.get( SubredditLogic.SUBSCRIBERS )[ 0 ], String.valueOf(returnedSubreddit.getSubscribers()) );

        sampleMap = new HashMap<>();
        sampleMap.put( SubredditLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( SubredditLogic.NAME, new String[]{ generateString.apply( 100 ) } );
        sampleMap.put( SubredditLogic.URL, new String[]{ "http://"+generateString.apply( 248 ) } );
        sampleMap.put( SubredditLogic.SUBSCRIBERS, new String[]{ "0" } );

        //idealy every test should be in its own method
        returnedSubreddit = subreddit.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( SubredditLogic.ID )[ 0 ] ), returnedSubreddit.getId() );
        assertEquals( sampleMap.get( SubredditLogic.NAME )[ 0 ], returnedSubreddit.getName() );
        assertEquals( sampleMap.get( SubredditLogic.URL )[ 0 ], returnedSubreddit.getUrl() );
        assertEquals( sampleMap.get( SubredditLogic.SUBSCRIBERS )[ 0 ], String.valueOf(returnedSubreddit.getSubscribers()) );
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = subreddit.getColumnNames();
        assertEquals( Arrays.asList( "ID", "Name", "URL", "Subscribers" ), list );
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = subreddit.getColumnCodes();
        assertEquals( Arrays.asList( SubredditLogic.ID, SubredditLogic.NAME, SubredditLogic.URL, SubredditLogic.SUBSCRIBERS ), list );
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = subreddit.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getName(), list.get( 1 ) );
        assertEquals( expectedEntity.getUrl(), list.get( 2 ) );
        assertEquals( expectedEntity.getSubscribers(), list.get( 3 ) );
    }
}
