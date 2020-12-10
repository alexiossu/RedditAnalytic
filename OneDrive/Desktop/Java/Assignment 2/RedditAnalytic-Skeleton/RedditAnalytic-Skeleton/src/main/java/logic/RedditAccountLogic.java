/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.RedditAccountDAL;
import entity.RedditAccount;
import java.util.List;
import java.util.Map;

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
    public List<String> getColumnNames() {
        
        return null;
    }

    @Override
    public List<String> getColumnCodes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<?> extractDataAsList(RedditAccount e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RedditAccount createEntity(Map<String, String[]> parameterMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RedditAccount> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RedditAccount getWithId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
