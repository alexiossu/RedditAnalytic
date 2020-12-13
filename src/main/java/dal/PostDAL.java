package dal;

import entity.Post;
import entity.RedditAccount;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class PostDAL extends GenericDAL<Post> {

    public PostDAL() {
        super( Post.class );
    }

    @Override
    public List<Post> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Post.findAll", null );
    }

    @Override
    public Post findById( int id ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "id", id );
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Post.findById", map );
    }

    public Post findByUniqueId( String uniqueId ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "uniqueId", uniqueId );
        return findResult( "Post.findByUniqueId", map );
    }

    public List<Post> findByPoints( int points ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "points", points );
        return findResults( "Post.findByPoints", map );
    }

    public List<Post> findByCommentCount( int commentCount ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "commentCount", commentCount );
        return findResults( "Post.findByCommentCount", map );
    }
    public List<Post> findByTitle( String title ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "title", title );
        return findResults( "Post.findByTitle", map );
    }


    public List<Post> findByCreated( Date created ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "created", created );
        return findResults( "Post.findByCreated", map );
    }

    public List<Post> findByAuthor(int id ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "id", id);
        return findResults( "Post.findByAuthor", map );
    }


}
