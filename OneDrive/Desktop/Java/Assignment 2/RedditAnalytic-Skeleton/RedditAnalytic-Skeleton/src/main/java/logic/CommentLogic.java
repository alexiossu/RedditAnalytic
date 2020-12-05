/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import common.ValidationException;
import dal.CommentDAL;
import dal.DataAccessLayer;
import entity.Comment;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author surko
 */
public class CommentLogic extends GenericLogic<Comment, CommentDAL>{

    public static final String REPLYS = "replys";
    public static final String IS_REPLY = "is_reply";
    public static final String POINTS = "points";
    public static final String CREATED = "created";
    public static final String TEXT = "text";
    public static final String ID = "id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String REDDIT_ACCOUNT_ID = "reddit_account_id";
    public static final String POST_ID = "post_id";
    
    public CommentLogic(CommentDAL dal) {
        super( new CommentDAL());
    }
    
    @Override
    public List<Comment> getAll() {       
       return get (() -> dal().findAll());
    } 
    
    @Override
    public Comment getWithId(int id) {    
        return get( () -> dal().findById( id ) );
    }

    public Comment getCommentWithUniqueId(String uniqueId) {  
        return get( () -> dal().findByUniqueId(uniqueId));
    }
    
    public List<Comment> getCommentsWithText(String text) {  
        return get( () -> dal().findByText(text));
    }
    
    public List<Comment> getCommentsWithCreated(Date created) {  
        return get( () -> dal().findByCreated(created));
    }
    
    public List<Comment> getCommentsWithPoints(int points) {  
        return get( () -> dal().findByPoints(points));
    }
    
    public List<Comment> getCommentsWithReplys(int replys) {  
        return get( () -> dal().findByReplys(replys));
    }
    
    public List<Comment> getCommentsWithIsReply(boolean isReply) {  
        return get( () -> dal().findByIsReply(isReply));
    }
    
    @Override
    // entity - check it. 
    
    public Comment createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        
        // New entity object
        Comment comment = new Comment();
        
        if ( parameterMap.containsKey(ID)) {
            try {
                entity.setId( Integer.paseInt( parameterMap.get (ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException (ex);
            }
        }
        // Error checking
        ObjIntConsumer<String> validator = (value, length) -> {
            if ( value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if ( value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length ) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };
        
        // Get more data for UNIQUE_ID, REDDIT_ACCOUNT_ID, POST_ID? 
        
        String text = parameterMap.get ( TEXT)[0];
        String created = parameterMap.get (CREATED)[0];
        String is_reply = parameterMap.get ( IS_REPLY)[0];
     
        
        return null;
    }
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Unique_id", "Reddit_account_id", "Post_id", "Text",
                "Created", "Points", "Is_reply", "Replys");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, UNIQUE_ID, REDDIT_ACCOUNT_ID, POST_ID, TEXT, 
                CREATED, POINTS, IS_REPLY, REPLYS);       
    }

    @Override
    public List<?> extractDataAsList(Comment e) {
        return Arrays.asList(e.getId(), e.getUniqueId(), e.getRedditAccountId(), e.getPostId(), e.getText(),
                e.getCreated(), e.getPoints(), e.getIsReply(), e.getReplys());
    }   
}
