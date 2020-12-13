/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import common.ValidationException;
import dal.RedditAccountDAL;
import entity.RedditAccount;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import static logic.AccountLogic.ID;

/**
 *
 * @author surko
 */
public class RedditAccountLogic extends GenericLogic<RedditAccount, RedditAccountDAL> {

    public static final String COMMENT_POINTS = "comment_points";
    public static final String LINK_POINTS = "link_points";
    public static final String CREATED = "created";
    public static final String NAME = "name";
    public static final String ID = "id";
    
    public RedditAccountLogic() {
        super(new RedditAccountDAL());
    }
@Override
    public List<RedditAccount> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public RedditAccount getWithId(int id) {
        return get( () -> dal().findById( id ) );
    }
    public RedditAccount getRedditAccountWithName ( String name) {
        
         return get( () -> dal().findByName(name));
    
    }
     public List<RedditAccount> getRedditAccountsWithLinkPoints ( int linkPoints) {
        
         return get( () -> dal().findByLinkPoints(linkPoints));
    }
     
      public List<RedditAccount> getRedditAccountsWithCommentPoints ( int commentPoints) {
        
        return get( () -> dal().findByCommentPoints(commentPoints));
    }
      
       public List<RedditAccount> getRedditAccountsWithCreated ( Date created) {
        
        return get( () -> dal().findByCreated(created));
    }
     
    @Override
       public RedditAccount createEntity ( Map<String, String[]> parameterMap) {
        
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        
        RedditAccount entity = new RedditAccount();
        
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }
        
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
        
        String comment_points = parameterMap.get(COMMENT_POINTS)[0];
        String link_points = parameterMap.get(LINK_POINTS)[0];
        String created = parameterMap.get(CREATED)[0];
        String name = parameterMap.get(NAME)[0];
        
        validator.accept (comment_points, 45);
        validator.accept (link_points, 45);
        validator.accept (created, 45);
        validator.accept (name, 45);
        
        entity.setCommentPoints(Integer.parseInt(comment_points));
        entity.setLinkPoints(Integer.parseInt(link_points));
        entity.setCreated(convertStringToDate(created));
        entity.setName(name);
        
        return entity;
    }
       
    @Override
    public List<String> getColumnNames() {
        
        return Arrays.asList("ID", "name", "created", "link_points", "comment_points");
    }

    @Override
    public List<String> getColumnCodes() {
         return Arrays.asList(ID, NAME, CREATED, LINK_POINTS, COMMENT_POINTS);
    }

    @Override
    public List<?> extractDataAsList(RedditAccount e) {
        return Arrays.asList(e.getId(), e.getName(), e.getCreated(), e.getLinkPoints(),e.getCommentPoints());
    }

    

    
    
    
}
