package logic;

import common.ValidationException;
import dal.PostDAL;
import entity.Post;
import entity.RedditAccount;
import entity.Subreddit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class PostLogic extends GenericLogic<Post, PostDAL> {

    /**
     * create static final variables with proper name of each column. this way you will never manually type it again,
     * instead always refer to these variables.
     *
     * by using the same name as column id and HTML element names we can make our code simpler. this is not recommended
     * for proper production project.
     */
    public static final String ID = "id";
    public static final String REDDIT_ACCOUNT = "reddit_account_id";
    public static final String SUBREDDIT_ACCOUNT = "subreddit_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String POINTS = "points";
    public static final String COMMENT_COUNT = "comment_count";
    public static final String TITLE = "title";
    public static final String CREATED = "created";

    PostLogic() {
        super( new PostDAL() );
    }

    @Override
    public List<Post> getAll() {
        return get( () -> dal().findAll() );
    }

    @Override
    public Post getWithId( int id ) {
        return get( () -> dal().findById( id ) );
    }

    public Post getPostWithUniqueId(String uniqueId) {
        return get( () -> dal().findByUniqueId( uniqueId ));
    }
    public List<Post> getPostWithPoints(int points){
        return get( () -> dal().findByPoints( points ));
    }
    public List<Post> getPostsWithCommentCount(int commentCount){
        return get( () -> dal().findByCommentCount( commentCount ));
    }
    public List<Post> getPostsWithAuthorID(int id){
        return get( () -> dal().findByAuthor( id ));
    }
    public List<Post> getPostsWithTitle(String title){
        return get( () -> dal().findByTitle( title ));
    }
    public List<Post> getPostsWithCreated(Date created){
        return get( () -> dal().findByCreated( created ));
    }

    @Override
    public Post createEntity( Map<String, String[]> parameterMap ) {
        //do not create any logic classes in this method.

        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Post entity = new Post();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer< String> validator = ( value, length ) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.

        String uniqueId = parameterMap.get(UNIQUE_ID)[0];
        int points = Integer.parseInt(parameterMap.get(POINTS)[0]);
//        int commentCount = Integer.parseInt(parameterMap.get(COMMENT_COUNT)[0]);
        String title = parameterMap.get(TITLE)[0];
        Date created = new Date();
        RedditAccount redditAccountId = new RedditAccount(Integer.parseInt(parameterMap.get(REDDIT_ACCOUNT)[0]));
        Subreddit subredditId = new Subreddit(Integer.parseInt(parameterMap.get(SUBREDDIT_ACCOUNT)[0]));
//        List<Comment> commentList = parameterMap.get(COMMENT_COUNT);


        //TODO validate the data

        //set values on entity
        entity.setUniqueId(uniqueId );
        entity.setPoints(points);
//        entity.setCommentCount(commentCount);
        entity.setTitle(title);
        entity.setCreated(created);
        entity.setRedditAccountId(redditAccountId);
        entity.setSubredditId(subredditId);

        return entity;
    }

    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnCodes and extractDataAsList
     *
     * @return list of all column names to be displayed.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "ID", "Reddit Account", "Subreddit Account", "Unique Id", "Points", "Comment Count", "Title", "Created" );
    }

    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( ID, REDDIT_ACCOUNT, SUBREDDIT_ACCOUNT, UNIQUE_ID, POINTS, COMMENT_COUNT, TITLE, CREATED );
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList( Post e ) {
        return Arrays.asList( e.getId(), e.getRedditAccountId(),e.getSubredditId(),e.getUniqueID(),e.getPoints(),e.getCommentCount(), e.getTitle(), e.getCreated());
    }
}